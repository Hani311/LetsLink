package com.example.androidproject

import android.widget.ImageView

class User(username:String, email:String, passwordEncr:String) {

    var username:String
    get() {
        return username
    }
        set(value) {
            username=value
        }

    var email:String
    get() {
        return email
    }
        set(value) {
            email=value
        }

    var passwordEncr:String
    get() {
        return passwordEncr
    }
        set(value) {
            passwordEncr=value
        }

}