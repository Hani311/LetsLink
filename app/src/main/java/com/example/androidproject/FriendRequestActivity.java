package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.Users.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestActivity extends AppCompatActivity {
    private RecyclerView requestList;
    private DatabaseReference allUsersDataBaseRef, userRef;
    private String senderUserId,reciverUserId,currentState,saveCurrentDate ;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        mAuth=FirebaseAuth.getInstance();
        senderUserId=mAuth.getCurrentUser().getUid();

        allUsersDataBaseRef= FirebaseDatabase.getInstance().getReference().child("friendRequest").child(senderUserId);
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        requestList= findViewById(R.id.FriendRequest);
        requestList.setHasFixedSize(true);
        requestList.setLayoutManager(new LinearLayoutManager(this));
        findRequest();
    }
    private void findRequest() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if(allUsersDataBaseRef.child(user.getID()).child("request_type").equalTo("received")





          /*          Query ReceviedQuery= allUsersDataBaseRef.child(user.getID()).child("request_type").equalTo("received");
                    FirebaseRecyclerAdapter<FindFriends, FriendRequestActivity.FindFriendsViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<FindFriends, FriendRequestActivity.FindFriendsViewHolder>(FindFriends.class,
                            R.layout.all_users_display_layout, FriendRequestActivity.FindFriendsViewHolder.class,
                            ReceviedQuery ) {
                        @Override
                        protected void populateViewHolder(FriendRequestActivity.FindFriendsViewHolder viewHolder, FindFriends findFriends, int i) {
                            viewHolder.setUsername(findFriends.getUsername());
                            viewHolder.setImageURL(findFriends.getImageURL());
                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String visit_user_id= getRef(i).getKey();
                                    Intent profileIntent= new Intent(FriendRequestActivity.this, PersonProfileActivity.class);
                                    profileIntent.putExtra("visit_user_id", visit_user_id);
                                    startActivity(profileIntent);


                                }
                            });
                        }
                    };
                    requestList.setAdapter(firebaseRecyclerAdapter);
                }

           */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setImageURL( String imageURL){
            CircleImageView myImage= mView.findViewById(R.id.profile_image);
            Picasso.get().load(imageURL).placeholder(R.drawable.profile).into(myImage);

        }
        public void setUsername(String username) {
            TextView myName = mView.findViewById(R.id.NameText);
            myName.setText(username);
        }

    }
}