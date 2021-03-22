package com.example.androidproject.Chat;

import android.os.Build;
import android.os.Bundle;

import com.example.androidproject.Users.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GroupChatActivity extends AppCompatActivity {

    private Button sendBtn;
    private boolean notify = true;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

    }

    private void sendMessage(String sender, String groupID, String message){
        notify=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendBtn.setBackground(getDrawable(R.drawable.ic_baseline_send_24));
        }

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hM=new HashMap<>();

        hM.put("sender", sender);
        hM.put("message", message);

        reference.child("Groups").child(groupID).push().setValue(hM);
        final String msg=message;

        final DatabaseReference lastMsgRef=FirebaseDatabase.getInstance().getReference("lastMessage")
                .child(groupID);

        lastMsgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lastMsgRef.child("lastMsg").setValue(message);
                lastMsgRef.child("from").setValue(fUser.getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(notify) {

                    //sendNotification(receiver, user.getUsername(), msg);
                    notify=false;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}