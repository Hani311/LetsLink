package com.example.androidproject;

public class Chatlist {

    public String id;
    public String from;

    public Chatlist(){ }

    public Chatlist(String from, String id) {
        this.from = from;
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
