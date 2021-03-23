package com.example.androidproject.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.example.androidproject.Users.User
import com.example.androidproject.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {

    lateinit var auth:FirebaseAuth
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val binding:ActivitySignUpBinding= DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        auth=FirebaseAuth.getInstance()
        val newUsername=binding.usernameInput
        val newEmail=binding.emailInput
        val newPassword=binding.passwordInput
        val confirmPassword=binding.passwordInputConfirmation

        binding.signUpButton.setOnClickListener(){
            var safe =true
            val regex = "^(.+)@(.+)$"
            val pattern:Pattern= Pattern.compile(regex)
            val matcher:Matcher=pattern.matcher(newEmail.text.toString())

            if(newUsername.text.toString().isEmpty()){
                safe=false
                newUsername.error="Please enter a username"
            }else if (newEmail.text.toString().isEmpty() or !matcher.matches()){
                safe=false

                newEmail.error="Please enter an email"
            }else if(newPassword.text.toString().isEmpty()){
                safe=false

                newPassword.error="Please enter a password"
            }else if(confirmPassword.text.toString().isEmpty()) {
                safe=false

                confirmPassword.error="Please re-enter your password"
            }


            if(safe){
                createNewUser(newUsername.text.toString(), newEmail.text.toString(), newPassword.text.toString())
            }
        }

        binding.root
    }

    private fun createNewUser(username: String, email: String, pass: String) {


        //create instance of firebase and add user info

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this@SignUpActivity, OnCompleteListener<AuthResult?> { task ->

            if (!task.isSuccessful) {

                val e = task.exception as FirebaseAuthException?
                Toast.makeText(this@SignUpActivity, "Failed Registration: " + e!!.message, Toast.LENGTH_SHORT).show()
                Toast.makeText(baseContext, "Sign up failed. Please try again in a few minutes", Toast.LENGTH_SHORT).show()
            } else {
                //creating a user and a Database reference to the info about the new User

                val fBU: FirebaseUser? = auth.currentUser
                val userID = fBU?.uid

                reference = FirebaseDatabase.getInstance().getReference("Users").child(userID!!)

                val hashMap = HashMap<String, String>()
                hashMap.put("ID", userID)
                hashMap.put("username", username)
                hashMap.put("imageURL", "default")
                hashMap.put("status", "offline")

                reference.setValue(hashMap).addOnCompleteListener {

                    if (it.isSuccessful) {

                        Toast.makeText(this@SignUpActivity, "data inserted successfully", Toast.LENGTH_LONG).show()
                        val currentUser = this.auth.currentUser!!

                        val i = Intent(this@SignUpActivity, MainActivity::class.java)
                        startActivity(i)

                        finish()
                    }
                }




            }
        })


    }
}
