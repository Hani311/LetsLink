package com.example.androidproject;

public class Data {


    private String userID;
    private int notifIcon;
    private String sented;
    private String notifBody;
    private String notifTitle;

    public Data(String userID, int notifIcon, String sented, String notifBody, String notifTitle) {
        this.userID = userID;
        this.notifIcon = notifIcon;
        this.sented = sented;
        this.notifBody = notifBody;
        this.notifTitle = notifTitle;
    }

    public Data(){}

    public String getUserID() {
        return userID;
    }

    public int getNotifIcon() {
        return notifIcon;
    }

    public String getSented() {
        return sented;
    }

    public String getNotifBody() {
        return notifBody;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setNotifIcon(int notifIcon) {
        this.notifIcon = notifIcon;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public void setNotifBody(String notifBody) {
        this.notifBody = notifBody;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

}
