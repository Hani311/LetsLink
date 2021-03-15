package com.example.androidproject.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidproject.Notifications.APISpecifier;
import com.example.androidproject.DB.Data;
import com.example.androidproject.Notifications.NotifReceiver;
import com.example.androidproject.Notifications.NotifSender;
import com.example.androidproject.Notifications.NotifToken;
import com.example.androidproject.R;
import com.example.androidproject.Users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MessageActivity extends AppCompatActivity {

    boolean notify=false;
    RelativeLayout relativeLayout;
    CircleImageView recepientCiv;
    TextView username;
    ImageButton sendBtn;
    EditText sendText;
    String userid;
    String recepientUri;
    FirebaseUser fUser;
    DatabaseReference reference;
    List<Chat> chatList;
    Intent intent;
    MessageAdapter messageAdapter;
    RecyclerView chatView;
    ValueEventListener seenListener;
    APISpecifier apiSpecifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        relativeLayout=findViewById(R.id.rootView);
        Toolbar toolbar=findViewById(R.id.chatToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiSpecifier= NotifReceiver.getNotifReceiver("https://fcm.googleapis.com/").create(APISpecifier.class);
        fUser= FirebaseAuth.getInstance().getCurrentUser();

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(MessageActivity.this, MessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        userid=intent.getStringExtra("userid");
        recepientUri=intent.getStringExtra("imageUri");
        if(recepientUri.equals("default")) {
            recepientCiv.setImageResource(R.mipmap.ic_launcher_round);
        }
        else{
            Glide.with(getApplicationContext()).load(recepientUri).into(recepientCiv);
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                readMessage(fUser.getUid(), userid, recepientUri);
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                notify=true;
                String message=sendText.getText().toString();

                if(!message.equals("")) {

                    sendMessage(fUser.getUid(), userid, message);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sendBtn.setBackground(getDrawable(R.drawable.ic_baseline_send_24));
                    }
                }
                else{
                    sendMessage(fUser.getUid(), userid, new String(Character.toChars(0x1F44D)));
                }
                sendText.setText("");
            }
        });

        recepientCiv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Context context=getApplicationContext();
                Intent intent=new Intent(context, PopInChatUserActivity.class);
                intent.putExtra("userid", userid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                //ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(MessageActivity.this, recepientCiv, ViewCompat.getTransitionName(recepientCiv));
                ActivityOptions options=ActivityOptions.makeCustomAnimation(context, R.animator.fad_in_popup, R.animator.fad_out_popup);
                context.startActivity(intent, options.toBundle());
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());



                if(user.getImageURL().equals("default")) {
                    recepientCiv.setImageResource(R.mipmap.ic_launcher_round);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(recepientCiv);
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
                        sendBtn.setBackground(getDrawable(R.mipmap.thumbs_up_foreground));
                    }
                }
            }
        });

        sendText.setOnTouchListener(new View.OnTouchListener() {



            @Override
            public boolean onTouch(View v, MotionEvent event) {



                try {
                    chatView.smoothScrollToPosition(chatView.getAdapter().getItemCount());
                }
                catch(NullPointerException e){}
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
        notify=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendBtn.setBackground(getDrawable(R.drawable.ic_baseline_send_24));
        }

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hM=new HashMap<>();

        hM.put("sender", sender);
        hM.put("receiver", receiver);
        hM.put("message", message);
        hM.put("seen", false);

        reference.child("Chats").push().setValue(hM);
        final String msg=message;

        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatRef.child("id").setValue(userid);
                chatRef.child("from").setValue(fUser.getUid());
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

                    sendNotification(receiver, user.getUsername(), msg);
                    notify=false;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        reference=FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dS:snapshot.getChildren()){
                    try {
                        {
                            User user = dS.getValue(User.class);
                            if (notify) {
                            }
                            notify = false;
                        }
                    }catch (DatabaseException e){}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
         */


        /*
        DatabaseReference chatReference=FirebaseDatabase.getInstance().getReference("Chatlist").child(userid);
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatReference.child("id").setValue(userid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
         */
    }

    private void sendNotification(String receiver, String username, String msg) {




        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    for (DataSnapshot dS : snapshot.getChildren()) {
                        NotifToken token = dS.getValue(NotifToken.class);
                        Data data = new Data(fUser.getUid(), R.mipmap.ic_launcher, userid,username + ": " + msg ,"New Message");
                        NotifSender sender = new NotifSender(data, token.getToken());
                        apiSpecifier.sendNotification(sender).enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                if (response.code() == 200) {
                                    if (response.body().succe == 1) {
                                        Toast.makeText(MessageActivity.this, "Sending notification failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {

                            }
                        });
                    }
                }catch (IllegalArgumentException e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readMessage(String receiverID, String senderID, String imageurl){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();


        chatList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();

                for(DataSnapshot dS:snapshot.getChildren()) {
                    Chat chat = dS.getValue(Chat.class);
                    if (chat.getReceiver().equals(receiverID) && chat.getSender().equals(senderID) || chat.getReceiver().equals(senderID)
                            && chat.getSender().equals(receiverID)) {
                        chatList.add(chat);
                    }
                }

                messageAdapter = new MessageAdapter(MessageActivity.this, chatList, imageurl);
                chatView.setAdapter(messageAdapter);

                try {
                    chatView.smoothScrollToPosition(chatView.getAdapter().getItemCount());
                }
                catch(NullPointerException e){ }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seen(userid);
    }

    private void seen(String userid){
        reference=FirebaseDatabase.getInstance().getReference().child("Chats");
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dS:snapshot.getChildren()){
                    Chat chat = dS.getValue(Chat.class);

                    if(chat.getReceiver().equals(fUser.getUid())&&chat.getSender().equals(userid))
                    {
                        HashMap<String, Object> hM=new HashMap<>();

                        hM.put("seen", true);
                        dS.getRef().updateChildren(hM);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.inchat_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.block_options:
                blockUser();
                return true;

            case R.id.mute_notifs:
                muteUserNotifs();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void muteUserNotifs() {
    }

    private void blockUser() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }
}