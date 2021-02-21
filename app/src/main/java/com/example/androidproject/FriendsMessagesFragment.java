package com.example.androidproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsMessagesFragment extends Fragment {

    private RecyclerView friendsView;
    private FriendsAdapter friendsAdapter;
    private List<User> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_friends_messages, container, false);

        View view =inflater.inflate(R.layout.fragment_friends_messages, container, false);

        friendsView=view.findViewById(R.id.friends_recycler_view);
        friendsView.setHasFixedSize(true);
        friendsView.setLayoutManager(new LinearLayoutManager(getContext()));

        friends=new ArrayList<>();
        readAllFriends();


        return view;
    }

    private void readAllFriends() {

        FirebaseUser fBU= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {

            //gets all friends from database reference
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                friends.clear();
                friends=new ArrayList<>();

                for(DataSnapshot dS:snapshot.getChildren()){
                    User user = dS.getValue(User.class);

                    if(!user.getID().equals(fBU.getUid())) {

                        if(friends.size()>0) {
                            if (!user.getID().equals(friends.get(friends.size() - 1).getID())) {

                                friends.add(user);
                            }
                        }
                        else{
                            friends.add(user);
                        }
                    }

                }

                friendsAdapter=new FriendsAdapter(getContext(), friends);
                friendsView.setAdapter(friendsAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}