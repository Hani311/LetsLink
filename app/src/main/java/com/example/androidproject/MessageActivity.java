package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    CircleImageView recepientCiv;
    TextView username;
    ImageButton sendBtn;
    EditText sendText;

    FirebaseUser fUser;
    DatabaseReference reference;
    Intent intent;

    MessageAdapter messageAdapter;
    RecyclerView chatView;
    List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        relativeLayout=findViewById(R.id.rootView);
        Toolbar toolbar=findViewById(R.id.chatToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatView=findViewById(R.id.chat_view);
        recepientCiv=findViewById(R.id.profile_image);
        username=findViewById(R.id.username_display);
        sendBtn=findViewById(R.id.sendBtn);
        sendText=findViewById(R.id.send_text);





        chatView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatView.setLayoutManager(linearLayoutManager);

        intent=getIntent();
        final String userid=intent.getStringExtra("userid");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=sendText.getText().toString();

                if(!message.equals("")) {

                    sendMessage(fUser.getUid(), userid, message);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sendBtn.setBackground(getDrawable(R.drawable.ic_baseline_send_24));
                    }
                }
                else{
                    Toast.makeText(MessageActivity.this, "Cannot send empty message", Toast.LENGTH_SHORT).show();
                }
                sendText.setText("");
            }
        });


        fUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());



                if(user.getImageURL().equals("default")) {
                    recepientCiv.setImageResource(R.mipmap.ic_launcher_round);
                }
                else{
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(recepientCiv);
                }

                readMessage(fUser.getUid(), userid, user.getImageURL());

            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        sendBtn.setBackground(getDrawable(R.drawable.ic_baseline_send_24));
                    }
                }
            }
        });

        sendText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                chatView.smoothScrollToPosition(chatView.getAdapter().getItemCount());

                return false;
            }
        });


        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        relativeLayout.getWindowVisibleDisplayFrame(r);
                        int screenHeight = relativeLayout.getRootView().getHeight();
                        int keypadHeight = screenHeight - r.bottom;

                        try {
                            if (keypadHeight > screenHeight * 0.15) {
                                chatView.smoothScrollToPosition(chatView.getAdapter().getItemCount());
                            } else {
                                chatView.smoothScrollToPosition(chatView.getAdapter().getItemCount());
                            }
                        }
                        catch(NullPointerException e){}
                    }});

    }

    private void sendMessage(String sender, String receiver, String message){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendBtn.setBackground(getDrawable(R.drawable.ic_baseline_send_24));
        }

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hM=new HashMap<>();

        hM.put("sender", sender);
        hM.put("receiver", receiver);
        hM.put("message", message);

        reference.child("Chats").push().setValue(hM);
    }

    private void readMessage(String receiverID, String senderID, String imageurl){
        chatList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();

                for(DataSnapshot dS:snapshot.getChildren()){
                    Chat chat=dS.getValue(Chat.class);
                    if(chat.getReceiver().equals(receiverID)&&chat.getSender().equals(senderID)||chat.getReceiver().equals(senderID)
                            && chat.getSender().equals(receiverID)){
                        chatList.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, chatList, imageurl);
                    chatView.setAdapter(messageAdapter);
                    chatView.smoothScrollToPosition(chatView.getAdapter().getItemCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}