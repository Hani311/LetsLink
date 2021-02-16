package com.example.androidproject;

public class User {
    
    private String ID;
    private String username;
    private String imageURL;


    public User(String ID, String username, String imageURL) {
        this.ID = ID;
        this.username = username;
        this.imageURL = imageURL;
    }

    public User(){

    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }


}
