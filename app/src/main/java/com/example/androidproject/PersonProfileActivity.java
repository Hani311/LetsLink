package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonProfileActivity extends AppCompatActivity {
private TextView userName,status;
private CircleImageView userProfileImage;
private Button btnSendFriendReq,btnDeclineFriendReq;
private String senderUserId,reciverUserId,currentState,saveCurrentDate ;
private DatabaseReference friendReqRef,userRef,friendsRef;
private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);
     mAuth=FirebaseAuth.getInstance();
     senderUserId=mAuth.getCurrentUser().getUid();
     reciverUserId= getIntent().getExtras().get("visit_user_id").toString();
     userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        friendReqRef=FirebaseDatabase.getInstance().getReference().child("friendRequests");
        friendsRef= FirebaseDatabase.getInstance().getReference().child("friends");
     IntializeFields();

     userRef.child(reciverUserId).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.exists())
             {
                 if(snapshot.hasChild("imageURL")){
                     String image = snapshot.child("imageURL").getValue().toString();
                     Picasso.get().load(image).into(userProfileImage);
                 }

                 String myUserName= snapshot.child("username").getValue().toString();
                 userName.setText(myUserName);
                btnMaintanance();
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
     btnDeclineFriendReq.setVisibility(View.INVISIBLE);
     btnDeclineFriendReq.setEnabled(false);
     if(!senderUserId.equals(reciverUserId)){
         btnSendFriendReq.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 btnSendFriendReq.setEnabled(false);
                 if(currentState.equals("not_friends")){
                     sendFriendRequest();
                 }
                 if(currentState.equals("request_sent")) {
                     cancelFriendRequest();
                 }
                 if(currentState.equals("request_received")){
                     acceptFriendsRequest();
                 }
                 if (currentState.equals("friends")){
                unFriend();
                 }

             }
         });
     }
     else{
         btnDeclineFriendReq.setVisibility(View.INVISIBLE);
         btnSendFriendReq.setVisibility(View.INVISIBLE);
     }
    }

    private void unFriend() {
        friendsRef.child(senderUserId).child(reciverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    friendsRef.child(reciverUserId).child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                btnSendFriendReq.setEnabled(true);
                                currentState ="not_friends";
                                btnSendFriendReq.setText("Send friend request");

                                btnDeclineFriendReq.setVisibility(View.INVISIBLE);
                                btnDeclineFriendReq.setEnabled(false);
                            }
                        }


                    });
                }
            }
        });


    }

    private void acceptFriendsRequest() {
        Calendar calForDate =Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());
        friendsRef.child(senderUserId).child(reciverUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    friendsRef.child(reciverUserId).child(senderUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                friendReqRef.child(senderUserId).child(reciverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            friendReqRef.child(reciverUserId).child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        btnSendFriendReq.setEnabled(true);
                                                        currentState ="friends";
                                                        btnSendFriendReq.setText("Unfriend");

                                                        btnDeclineFriendReq.setVisibility(View.INVISIBLE);
                                                        btnDeclineFriendReq.setEnabled(false);
                                                    }
                                                }


                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });

    }

    private void cancelFriendRequest() {
        friendReqRef.child(senderUserId).child(reciverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    friendReqRef.child(reciverUserId).child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                btnSendFriendReq.setEnabled(true);
                                currentState ="not_friends";
                                btnSendFriendReq.setText("Send friend request");

                                btnDeclineFriendReq.setVisibility(View.INVISIBLE);
                                btnDeclineFriendReq.setEnabled(false);
                            }
                        }


                    });
                }
            }
        });

    }

    private void btnMaintanance() {
        friendReqRef.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(reciverUserId)){
                    String requestType= snapshot.child(reciverUserId).child("request_type").getValue().toString();
                    if(requestType.equals("sent")){
                        currentState="request_sent";
                        btnSendFriendReq.setText("Cancel friend request");
                        btnDeclineFriendReq.setVisibility(View.INVISIBLE);
                        btnDeclineFriendReq.setEnabled(false);
                    }else if (requestType.equals("received")){
                        System.out.println("received");
                        currentState="request_received";
                        btnSendFriendReq.setText("Accept friend request");
                        btnDeclineFriendReq.setVisibility(View.VISIBLE);
                        btnDeclineFriendReq.setEnabled(true);
                        btnDeclineFriendReq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelFriendRequest();
                            }
                        });
                    }
                }   else{
                    friendsRef.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(reciverUserId)){
                                currentState="friends";
                                btnSendFriendReq.setText("Unfriend ");

                                btnDeclineFriendReq.setVisibility(View.INVISIBLE);
                                btnDeclineFriendReq.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendFriendRequest() {
  friendReqRef.child(senderUserId).child(reciverUserId).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
          if(task.isSuccessful()){
              friendReqRef.child(reciverUserId).child(senderUserId).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()) {
                   btnSendFriendReq.setEnabled(true);
                   currentState ="request_sent";
                   btnSendFriendReq.setText("Cancel friend request");
                   btnDeclineFriendReq.setVisibility(View.INVISIBLE);
                   btnDeclineFriendReq.setEnabled(false);
                   }
                  }


              });
          }
      }
  });
    }

    private void IntializeFields() {
        userName= findViewById(R.id.person_full_name);
        userProfileImage= findViewById(R.id.person_pic);
        btnSendFriendReq = findViewById(R.id.person_send_friend_request_btn);
        btnDeclineFriendReq = findViewById(R.id.declinefriendreq);
        currentState="not_friends";

    }
}