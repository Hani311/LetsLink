package com.example.androidproject

import android.app.Person
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.androidproject.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.example.androidproject.databinding.FragmentTitleBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class LoginFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val binding: FragmentLoginBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val usernameText=binding.usernameInput
        val userPassword=binding.passwordInput
        val signUpButton=binding.signUpButton
        val loginButton=binding.logInButton


        signUpButton.setOnClickListener(){
            signUp()
        }

        loginButton.setOnClickListener(){
            logIn(usernameText, userPassword)
        }

        return binding.root
    }

    private fun signUp() {

        
    }

    private fun logIn(usernameText: TextInputLayout, userPassword: TextInputLayout) {


    }
}
