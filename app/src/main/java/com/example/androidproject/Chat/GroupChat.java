package com.example.androidproject.Chat;

public class GroupChat {

    public String sender;
    public String senderName;
    public String message;
    public String timestamp;
    public String senderUri;

    public GroupChat(){}

    public GroupChat(String sender, String senderName, String message, String timestamp, String senderUri) {
        this.sender = sender;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
        this.senderUri = senderUri;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSenderUri(String senderUri) {
        this.senderUri = senderUri;
    }

    public String getSenderUri() {
        return senderUri;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
