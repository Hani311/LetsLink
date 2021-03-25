package com.example.androidproject.Users;


public class UserSingleton {

    private static UserSingleton singleton=null;

    private String currenUserUri;
    private String currentUserName;

    private UserSingleton() {
    }

    public static UserSingleton getInstance() {

        if (singleton == null) {
            synchronized (UserSingleton.class) {
                if (singleton == null) {
                    singleton = new UserSingleton();
                }
            }
        }
        return singleton;
    }

    public void setCurrenUserUri(String currenUserUri) {
        this.currenUserUri = currenUserUri;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getCurrenUserUri() {
        return currenUserUri;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }
}