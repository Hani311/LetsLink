package com.example.androidproject.Chat;

public class Chat {

    public String sender;
    public String receiver;
    public String message;
    public boolean isSeen;

    public Chat(){}

    public Chat(String sender, String receiver, String message, Boolean isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen=isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSeen() {
        return isSeen;
    }
}
