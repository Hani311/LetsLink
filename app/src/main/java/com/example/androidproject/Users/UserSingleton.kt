package com.example.androidproject.Users

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class UserSingleton private constructor(){

    lateinit var auth:FirebaseAuth

    init {
        println("Singleton class invoked.")
        auth= FirebaseAuth.getInstance()
    }

    /*
    fun getCurrentUser(): User {
        return currentUser
    }
    
     */

}