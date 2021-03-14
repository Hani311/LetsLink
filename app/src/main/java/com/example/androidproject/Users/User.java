package com.example.androidproject.Users;

public class User {
    
    private String ID;
    private String username;
    private String imageURL;
    private String status;


    public User(String ID, String username, String imageURL, String status) {
        this.ID = ID;
        this.username = username;
        this.imageURL = imageURL;
        this.status=status;
    }

    public User(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
