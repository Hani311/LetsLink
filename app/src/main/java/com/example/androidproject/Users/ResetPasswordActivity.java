package com.example.androidproject.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseUser user;
    private  TextInputEditText oldPass,newPass;
    private Button btnReset;
    private View coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        btnReset = findViewById(R.id.btnReset);
        oldPass= findViewById(R.id.textInputOldPassword);
        String oldpass= oldPass.getText().toString();
        newPass= findViewById(R.id.textInputNewPassword);
        String newpass= oldPass.getText().toString();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                        Snackbar snackbar_fail = Snackbar
                                                .make(coordinatorLayout, "Something went wrong. Please try again later", Snackbar.LENGTH_LONG);
                                        snackbar_fail.show();
                                    }else {
                                        Snackbar snackbar_su = Snackbar
                                                .make(coordinatorLayout, "Password Successfully Modified", Snackbar.LENGTH_LONG);
                                        snackbar_su.show();
                                    }
                                }
                            });
                        }else {
                            Snackbar snackbar_su = Snackbar
                                    .make(coordinatorLayout, "Authentication Failed", Snackbar.LENGTH_LONG);
                            snackbar_su.show();
                        }
                    }
                });
            }
        });


    }

}

