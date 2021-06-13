// 게시판 글 수정, 삭제시의 동작 담당 Activity
package com.example.mobilesw.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilesw.adapter.BoardDeleter;
import com.example.mobilesw.info.PostInfo;
import com.example.mobilesw.adapter.OnPostListener;
import com.example.mobilesw.R;
import com.example.mobilesw.view.ReadContentsView;

public class BoardActivity extends AppCompatActivity {
    private PostInfo postInfo;
    private BoardDeleter boardDeleter;
    private ReadContentsView readContentsVIew;
    private LinearLayout contentsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_board);

        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");
        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsVIew = findViewById(R.id.readContentsView);

        boardDeleter = new BoardDeleter(this);
        boardDeleter.setOnPostListener(onPostListener);
        uiUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && requestCode == Activity.RESULT_OK){
            postInfo = (PostInfo)data.getSerializableExtra("postInfo");
            contentsLayout.removeAllViews();
            uiUpdate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                boardDeleter.storageDelete(postInfo);
                return true;
            case R.id.modify:
                // MakePost에서 다시 업로드
                myStartActivity(PostActivity.class, postInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            Log.e("로그 ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    private void uiUpdate(){
        readContentsVIew.setPostInfo(postInfo);
    }

    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent, 0);
    }
}