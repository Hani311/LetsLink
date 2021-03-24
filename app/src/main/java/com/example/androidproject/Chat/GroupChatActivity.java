package com.example.androidproject.Chat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.androidproject.MainActivity;
import com.example.androidproject.Users.User;
import com.example.androidproject.Users.UserSingleton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {

    private ImageButton sendBtn;
    private FirebaseAuth fAuth;
    private User user;
    String groupID;
    String groupIcon;
    String groupTitle;
    String senderUri;
    String senderName;
    TextView sendText;
    TextView groupChatTitle;
    CircleImageView groupPic;
    private DatabaseReference databaseReference;
    private RecyclerView groupChatView;
    private ArrayList<GroupChat> chatList;
    private GroupChatAdapter adapter;
    private UserSingleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        singleton=UserSingleton.getInstance();
        Intent intent = getIntent();

        groupID = intent.getStringExtra("groupID");
        groupIcon = intent.getStringExtra("groupIcon");
        groupTitle = intent.getStringExtra("groupTitle");
        senderUri = intent.getStringExtra("currentUri");
        senderName= intent.getStringExtra("currentName");


        groupChatView=findViewById(R.id.group_chat_view);

        groupChatView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        groupChatView.setLayoutManager(linearLayoutManager);

        sendText = findViewById(R.id.send_text_group);
        sendBtn = findViewById(R.id.sendBtnGroup);
        groupPic=findViewById(R.id.groupchat_pic);
        groupChatTitle=findViewById(R.id.grouchat_name);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        fAuth=FirebaseAuth.getInstance();


        groupChatTitle.setText(groupTitle);

        if(groupIcon.equals("default")) {
            groupPic.setImageResource(R.mipmap.ic_launcher_round);
        }
        else{
            Glide.with(getApplicationContext()).load(groupIcon).into(groupPic);
        }


        readGroupMessages();

        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String message=sendText.getText().toString();

                if(!message.equals("")) {

                    sendMessage(message);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sendBtn.setBackground(getDrawable(R.drawable.ic_baseline_send_24));
                    }
                }
                else{
                    sendMessage(new String(Character.toChars(0x1F44D)));
                }
                sendText.setText("");
            }
        });

        sendText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(s.toString().trim().length()>0){
                        sendBtn.setBackground(getDrawable(R.drawable.ic_baseline_send_ready_24));
                    }else{
                        sendBtn.setBackground(getDrawable(R.mipmap.thumbs_up_foreground));
                    }
                }
            }
        });

    }

    private void readGroupMessages(){

        chatList=new ArrayList<>();
        DatabaseReference readRef=FirebaseDatabase.getInstance().getReference("Groups");

        readRef.child(groupID).child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatList.clear();

                        for(DataSnapshot dS:snapshot.getChildren()){
                            GroupChat chat=dS.getValue(GroupChat.class);
                            chatList.add(chat);
                        }

                        adapter=new GroupChatAdapter(GroupChatActivity.this, chatList);
                        groupChatView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void sendMessage(String message){

        String timestamp=""+System.currentTimeMillis();

        HashMap<String, String> hM= new HashMap<>();

        hM.put("sender", ""+fAuth.getCurrentUser().getUid());
        hM.put("message", ""+ message);
        hM.put("timestamp", ""+ timestamp);
        hM.put("senderUri", senderUri);
        hM.put("senderName", senderName);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Groups");

        reference.child(groupID).child("Messages").child(timestamp)
                .setValue(hM)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                });
    }

    public User getCurrentUser(){


        databaseReference.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user =snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        return user;
    }
}