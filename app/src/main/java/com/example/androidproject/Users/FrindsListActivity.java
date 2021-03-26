package com.example.androidproject.Users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.PersonProfileActivity;
import com.example.androidproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrindsListActivity extends AppCompatActivity {
private RecyclerView myFriendList;
private DatabaseReference friendsRef, userRef;
private FirebaseAuth mAuth;
private String onlineUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frinds_list);
       mAuth= FirebaseAuth.getInstance();
       onlineUserID=mAuth.getCurrentUser().getUid();
       friendsRef= FirebaseDatabase.getInstance().getReference().child("friends").child(onlineUserID);
       userRef=FirebaseDatabase.getInstance().getReference().child("Users");


        myFriendList=(RecyclerView) findViewById(R.id.friendsList);
        myFriendList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myFriendList.setLayoutManager(linearLayoutManager);

        displayAllFriends();
    }


    private void displayAllFriends() {
        FirebaseRecyclerAdapter<Friends,friendViewHolder> firebaseRecyclerAdapter=  new FirebaseRecyclerAdapter<Friends, friendViewHolder>(
                Friends.class, R.layout.all_users_display_layout, friendViewHolder.class,friendsRef
        ) {
            @Override
            protected void populateViewHolder(friendViewHolder friendViewHolder, Friends friends, int i) {

                friendViewHolder.setDate(friends.getDate());
            final String usersIDs= getRef(i).getKey();
            userRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        final String userName = snapshot.child("username").getValue().toString();
                        final String profileImage = snapshot.child("imageURL").getValue().toString();
                        friendViewHolder.setUsername(userName);
                        friendViewHolder.setImageURL(profileImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
             friendViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     String visit_user_id= getRef(i).getKey();
                     Intent profileIntent= new Intent(FrindsListActivity.this, PersonProfileActivity.class);
                     profileIntent.putExtra("visit_user_id", visit_user_id);
                     startActivity(profileIntent);
                 }
             });
            }
        };
        myFriendList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class friendViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public friendViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setImageURL( String imageURL){
            CircleImageView myImage= mView.findViewById(R.id.profile_image);
            Picasso.get().load(imageURL).placeholder(R.drawable.profile1).into(myImage);

        }
        public void setUsername(String username) {
            TextView myName = mView.findViewById(R.id.NameText);
            myName.setText(username);
        }
        public void setDate(String date){
            TextView friendsDate = mView.findViewById(R.id.status);
            friendsDate.setText("Friends Since: " +date);
        }
    }
}