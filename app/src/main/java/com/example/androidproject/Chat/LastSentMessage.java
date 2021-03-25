package com.example.androidproject.Chat;

public class LastSentMessage {

    private String lastMsg;
    private String from;
    private Boolean seen;

    public LastSentMessage(String from, String lastMsg, Boolean seen) {
        this.lastMsg = lastMsg;
        this.from = from;
        this.seen = seen;
    }

    public LastSentMessage() {
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getLastMsg() {
        return lastMsg;
    }


    public String getFrom() {
        return from;
    }

    public boolean getSeen() {
        return seen;
    }
}
