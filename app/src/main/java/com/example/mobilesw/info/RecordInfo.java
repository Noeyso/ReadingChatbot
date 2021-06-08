package com.example.mobilesw.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordInfo implements Serializable {
    private String title;
    private String readtime;
    private String description;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;
    private String id;

    public RecordInfo(String title, String readtime, String description, ArrayList<String> contents, String publisher, Date createdAt, String id){
        this.title = title;
        this.readtime = readtime;
        this.description = description;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.id = id;
    }

    public RecordInfo(String title,  String readtime,String description, ArrayList<String> contentsList, String uid, Date date) {
        this.title = title;
        this.readtime = readtime;
        this.description = description;
        this.contents = contentsList;
        this.publisher = uid;
        this.createdAt = date;

    }

    public Map<String, Object> getRecordInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("title",title);
        docData.put("readtime",readtime);
        docData.put("description",description);
        docData.put("contents",contents);
        docData.put("publisher",publisher);
        docData.put("createdAt",createdAt);
        return  docData;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getReadtime(){
        return this.readtime;
    }
    public void setReadtime(String readtime){
        this.title = readtime;
    }
    public ArrayList<String> getContents(){
        return this.contents;
    }
    public void setContents(ArrayList<String> contents){
        this.contents = contents;
    }
    public String getDescription() {return this.description;}
    public void setDescription(String description){
        this.description = description;
    }
    public String getPublisher(){
        return this.publisher;
    }
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public Date getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }
    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
}
