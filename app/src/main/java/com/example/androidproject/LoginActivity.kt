package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ToolbarBindingAdapter
import androidx.fragment.app.FragmentTransaction
import com.example.androidproject.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    lateinit var loading:ProgressBar
    lateinit var auth:FirebaseAuth
    lateinit var firebaseUser:FirebaseUser

    override fun onStart() {
        super.onStart()

        auth=FirebaseAuth.getInstance()


        try {
            /*
            firebaseUser = auth.currentUser!!

            if (firebaseUser != null) {

                //Toast.makeText(this@LoginActivity, firebaseUser, Toast.LENGTH_SHORT)

                val i = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(i)
            }

             */
        }

        catch (e:NullPointerException){
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val binding:ActivityLoginBinding=DataBindingUtil.setContentView(this, R.layout.activity_login)



        val usernameText=binding.usernameInput
        val userPassword=binding.passwordInput
        val signUpButton=binding.signUpButton
        val loginButton=binding.logInButton
        loading=binding.loading

        signUpButton.setOnClickListener(){
            signUp()

        }

        loginButton.setOnClickListener(){

            logIn(usernameText, userPassword)
            //logIn(usernameText, userPassword)
        }

        binding.root
    }

    private fun signUp() {

        val intent=Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun logIn(usernameText: TextInputEditText, userPassword: TextInputEditText) {

        if(usernameText.text.toString().isEmpty()){

            usernameText.error="Please enter your username or email"
            return
        }
        else if(userPassword.text.toString().isEmpty()){

            userPassword.error="Please enter a password"
            return
        }
        else
        loading.visibility=View.VISIBLE
        auth.signInWithEmailAndPassword(usernameText.text.toString(), userPassword.text.toString()).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful){

                var currentUser=auth.currentUser

                val i:Intent=Intent(this@LoginActivity, MainActivity::class.java)
                val username=usernameText.text.toString()
                val password=userPassword.text.toString()
                //i.putExtra("Username", "$username")
                //i.putExtra("Password", "$password")
                startActivity(i)
            }
            else{
                Toast.makeText(this@LoginActivity, "Log in failed, please try again", Toast.LENGTH_SHORT).show()
            }
        }

    }
}