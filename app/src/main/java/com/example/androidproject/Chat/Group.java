package com.example.androidproject.Chat;

import com.example.androidproject.Users.User;

import java.util.ArrayList;

public class Group {

    private String groupID;
    private String title;
    private String description;
    private String groupAdmin;
    private String groupIcon;
    private String timeCreated;

    public Group(){}

    public Group(String groupID, String title, String description, String groupAdmin, String groupIcon, String timeCreated) {
        this.groupID = groupID;
        this.title = title;
        this.description = description;
        this.groupAdmin = groupAdmin;
        this.groupIcon = groupIcon;
        this.timeCreated = timeCreated;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupIcon() {
        return groupIcon;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getGroupID() {
        return groupID;
    }


    public String getGroupAdmin() {
        return groupAdmin;
    }
}
