package com.example.androidproject.Chat;

public class Chat {

    public String sender;
    public String receiver;
    public String message;
    public boolean seen;
    public String timeSent;

    public Chat(){}

    public Chat(String sender, String receiver, String message, boolean seen, String timeSent) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.seen = seen;
        this.timeSent = timeSent;
    }

    public void setSeen(boolean seen) { this.seen=seen; }

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
        return seen;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
}
