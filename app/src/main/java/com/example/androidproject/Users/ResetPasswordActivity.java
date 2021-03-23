package com.example.androidproject.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Button btnChange= findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText oldPass= findViewById(R.id.textInputOldPassword);
                String oldpass= oldPass.getText().toString();
                EditText  newPass= findViewById(R.id.textInputNewPassword);
                String newpass= newPass.getText().toString();
                user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(email,oldpass);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(ResetPasswordActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(ResetPasswordActivity.this, "Password Successfully Modified", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(ResetPasswordActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}