package com.example.mobilesw.info;

import java.io.Serializable;

public class BookInfo implements Serializable { 
    private String title;
    private String author;
    private String img;
    private String publisher;
    private String pubdate;
    private String description;

    public BookInfo(String title,String author,String img,String publisher,String pubdate,String description){
        this.title = title;
        this.author = author;
        this.img = img;
        this.publisher = publisher;
        this.pubdate = pubdate;
        this.description = description;
    }

    public String getTitle(){return title;};
    public void setTitle(String title){this.title = title;};
    public String getAuthor(){return author;};
    public void setAuthor(String author){this.author = author;};
    public String getImg(){return img;};
    public void setImage(String img){this.img = img;};
    public String getPublisher(){return publisher;};
    public void setPublisher(String publisher){this.publisher = publisher;};
    public String getPubdate(){return pubdate;};
    public void setPubdate(String pubdate){this.pubdate = pubdate;};
    public String getDescription(){return description;};
    public void setDescription(String description){this.description = description;};
}
