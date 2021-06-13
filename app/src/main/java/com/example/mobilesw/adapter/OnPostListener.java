package com.example.mobilesw.adapter;

import com.example.mobilesw.info.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
