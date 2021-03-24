package com.example.androidproject.Users;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.androidproject.Users.PassNotif.CHANNEL_1_ID;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseUser user;
    private NotificationManagerCompat notificationManager;
    private TextView editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Button btnChange= findViewById(R.id.btnChange);

        notificationManager = NotificationManagerCompat.from(this);
        editTextTitle = findViewById(R.id.edit_text_title);

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


                                        String title = editTextTitle.getText().toString();
                                        Log.d("Hani22", "sendOnChannel1: "+ title);

                                        Notification notification = new NotificationCompat.Builder(ResetPasswordActivity.this, CHANNEL_1_ID)
                                                .setSmallIcon(R.drawable.thumbs_up)
                                                .setContentText("The password was not updated something wrong")
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .setAutoCancel(true)
                                                .build();
                                        notificationManager.notify(1, notification);

                                        Toast.makeText(ResetPasswordActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                                    }else {


                                        String title = editTextTitle.getText().toString();
                                        Log.d("Hani22", "sendOnChannel1: "+ title);

                                        Notification notification = new NotificationCompat.Builder(ResetPasswordActivity.this, CHANNEL_1_ID)
                                                .setSmallIcon(R.drawable.thumbs_up)
                                                .setContentTitle("Updated")
                                                .setContentText("The password has been updated successfully")
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .setAutoCancel(true)
                                                .build();
                                        notificationManager.notify(1, notification);

                                        Toast.makeText(ResetPasswordActivity.this, "Password Successfully Modified", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {


                            String title = editTextTitle.getText().toString();
                            Log.d("Hani22", "sendOnChannel1: "+ title);

                            Notification notification = new NotificationCompat.Builder(ResetPasswordActivity.this, CHANNEL_1_ID)
                                    .setSmallIcon(R.drawable.thumbs_up)
                                    .setContentTitle("Authentication Failed")
                                    .setContentText("Please make sure you have entered the right password")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .setAutoCancel(true)
                                    .build();
                            notificationManager.notify(1, notification);

                            Toast.makeText(ResetPasswordActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}