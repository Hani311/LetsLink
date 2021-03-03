package com.example.androidproject;

public class FindFriends {
    //strings for what we want to show about the profiles in the findfriend activity, can easly add more stuff ;
    public String username,imageURL;
     public FindFriends(){

     }

    public FindFriends(String username, String imageURL) {
        this.username = username;
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
