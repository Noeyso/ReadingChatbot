package com.example.mobilesw.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilesw.R;
import com.example.mobilesw.activity.BookInfoActivity;
import com.example.mobilesw.adapter.BookAdapter;
import com.example.mobilesw.info.BookInfo;
import com.example.mobilesw.info.NaverAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FragSearch extends Fragment {

    private ArrayList<BookInfo> bookList;
    private String apiURL = "https://openapi.naver.com/v1/search/book.json?";
    private String query ="";
    private int start=1;

    RecyclerView rcv;
    LinearLayoutManager llm;

    BookAdapter bookAdapter;

    private Button btn_search;
    private EditText edit_search;

    public FragSearch() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_search, container, false);

        rcv = view.findViewById(R.id.search_list);
        llm = new LinearLayoutManager(getContext());
        rcv.setHasFixedSize(true);//각 아이템이 보여지는 것을 일정하게
        rcv.setLayoutManager(llm);//앞서 선언한 리싸이클러뷰를 레이아웃 매니저에 붙힌다.

        btn_search = view.findViewById(R.id.btn_search);
        edit_search= view.findViewById(R.id.edit_search);


        bookList = new ArrayList<BookInfo>();

        btn_search.setOnClickListener(onClickListener);
        edit_search.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch(keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        bookList.clear();
                        query =edit_search.getText().toString();
                        new BookThread().execute();
                        return true;
                }
                return false;
            }
        });


//        edit_search.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                start = 1;
//                bookList.clear();
//                query = edit_search.getText().toString();
//                new BookThread().execute();
//                return false;
//            }
//        });
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_search:
                    bookList.clear();
                    query =edit_search.getText().toString();
                    new BookThread().execute();
            }
        }
    };
    //BackThread 생성
    public class BookThread extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            return NaverAPI.main(apiURL,query,start,0);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("결과.........."+s);
            try {
                JSONArray jArray=new JSONObject(s).getJSONArray("items");
                for(int i=0; i<jArray.length(); i++){
                    JSONObject obj=jArray.getJSONObject(i);

                    String title = obj.getString("title");
                    title = title.replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>","");
                    String author = obj.getString("author");
                    String image = obj.getString("image");
                    String publisher = obj.getString("publisher");
                    String pubdate = obj.getString("pubdate");
                    String description = obj.getString("description");
                    if(pubdate.length()==8){
                        pubdate = pubdate.substring(0,4)+"-"+pubdate.substring(4,6)+"-"+pubdate.substring(6,8);
                    }
                    BookInfo bookInfo=new BookInfo(title,author,image,publisher,pubdate,description);
                    bookList.add(bookInfo);
                }
                bookAdapter=new BookAdapter(getContext(),bookList); //앞서 만든 리스트를 어댑터에 적용시켜 객체를 만든다.
                bookAdapter.setOnItemClickListener(
                        new BookAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                BookInfo obj = bookList.get(pos);
                                Intent intent = new Intent(getContext(), BookInfoActivity.class);

                                //목록에서 클릭한 게시물에 대한 내용을 전달
                                intent.putExtra("bookInfo", (Serializable) obj);
                                startActivity(intent);
                            }
                        }
                );
                rcv.setAdapter(bookAdapter);
                //rcv.scrollToPosition(start);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
