package com.example.androidproject.Notifications;

import com.example.androidproject.DB.Data;

public class NotifSender {

    public Data data;
    public String to;

    public NotifSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public String getTo() {
        return to;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
