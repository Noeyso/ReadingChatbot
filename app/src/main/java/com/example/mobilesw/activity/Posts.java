package com.example.mobilesw.activity;

public class Posts {
    public String postimage, description, uid,time,date,profilename,fullname;

    public Posts(){

    }

    public Posts (String uid,String time, String postimage, String description, String date, String profilename, String fullname){
        this.postimage = postimage;
        this.description = description;
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.profilename = profilename;
        this.fullname = fullname;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
