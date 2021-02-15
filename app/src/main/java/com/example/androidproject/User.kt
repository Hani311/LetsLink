package com.example.androidproject

class User() {


    constructor(ID: String, username: String, imageUrl: String) : this() {
        this.ID=ID
        this.username=username
        this.imageUrl=imageUrl
    }

    var ID:String
    get() {
        return ID
    }
        set(value) {
            ID=value
        }

    var username:String
    get() {
        return username
    }
        set(value) {
            username=value
        }

    var imageUrl:String
    get() {
        return imageUrl
    }
        set(value) {
            imageUrl=value
        }

}