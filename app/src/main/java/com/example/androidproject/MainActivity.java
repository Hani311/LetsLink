package com.example.androidproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.androidproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //DataBindingUtil binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflator, R.layout.fragment_title, container, false)
        //ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setContentView(R.layout.activity_main);
    }
}