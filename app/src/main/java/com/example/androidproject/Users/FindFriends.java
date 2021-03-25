package com.example.androidproject.Users;

public class FindFriends {
    //strings for what we want to show about the profiles in the findfriend activity, can easly add more stuff ;
    public String username;
    public String imageURL;
  //  public String bio;

    /*  public String getBio() {
         return bio;
     }

    public void setBio(String bio) {
         this.bio = bio;
     }


    */
     public FindFriends(){

     }

    public FindFriends(String username, String imageURL) {
        this.username = username;
        this.imageURL = imageURL;
      //  this.bio=bio;
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
