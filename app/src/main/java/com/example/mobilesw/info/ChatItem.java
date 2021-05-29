package com.example.mobilesw.info;

public class ChatItem {

    String id;
    String message;
    String time;
    Long timestamp;

    public ChatItem(String id, String message, String time, Long timestamp) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.timestamp = timestamp;
    }

    public ChatItem() {
    }

    //Getter & Setter
    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


}

