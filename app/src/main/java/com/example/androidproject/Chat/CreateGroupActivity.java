package com.example.androidproject.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.example.androidproject.R;
import com.example.androidproject.Users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateGroupActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private FirebaseAuth auth;
    private boolean notify=true;
    private Button sendBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        actionBar=getSupportActionBar();
        actionBar.setTitle("Creating group");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        auth=FirebaseAuth.getInstance();
        checkForUser();
        //sendBtn=findViewById(R.id.);

    }

    private void checkForUser() {
        FirebaseUser user = auth.getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User");
        
        if(user!=null){
            actionBar.setSubtitle("username");
        }
    }

}