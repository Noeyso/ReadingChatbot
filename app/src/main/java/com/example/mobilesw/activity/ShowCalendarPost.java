package com.example.mobilesw.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mobilesw.R;

import java.util.HashMap;

public class ShowCalendarPost extends AppCompatActivity {
    private HashMap<String,String> info;

    private LinearLayout layout_cbook;
    private TextView tv_select_date,c_book_title,c_book_author,tv_des_user,tv_des;
    private ImageView c_book_image;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cpost);

        sp = getApplicationContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
        String str = sp.getString("name", "");
        System.out.println("calendar 에서 user의 이름 : "+str);
        str+="님의 한줄평!";

        Intent intent = getIntent();
        info = (HashMap<String, String>) intent.getSerializableExtra("cPostInfo");
        c_book_title = findViewById(R.id.c_book_title);
        c_book_author = findViewById(R.id.c_book_author);
        c_book_image  = findViewById(R.id.c_book_image);
        tv_select_date = findViewById(R.id.tv_select_date);
        tv_des_user = findViewById(R.id.tv_des_user);
        tv_des = findViewById(R.id.tv_des);

        c_book_title.setText(info.get("title"));
        c_book_author.setText(info.get("author"));
        String image = info.get("image");
        if(!image.equals("")){
            //Picasso.get().load(image).into(holder.img);
            Glide.with(this).load(image).into(c_book_image);
        }
        tv_select_date.setText(info.get("date"));
        tv_des.setText(info.get("comment"));
        tv_des_user.setText(str);

    }
}
