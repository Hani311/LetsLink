package com.example.androidproject.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.Users.FriendsAdapter;
import com.example.androidproject.Users.User;
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
    private ArrayList<String> friendIDs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_friends_messages, container, false);

        View view = inflater.inflate(R.layout.fragment_friends_messages, container, false);

        friendsView = view.findViewById(R.id.friends_recycler_view);
        friendsView.setHasFixedSize(true);
        friendsView.setLayoutManager(new LinearLayoutManager(getContext()));

        friends = new ArrayList<>();
        readAllFriends();


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        readAllFriends();
        super.onCreate(savedInstanceState);
    }

    private void readAllFriends() {
        friendIDs=new ArrayList<>();
        friendIDs.clear();

        FirebaseUser fBU = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("friends")
                .child(fBU.getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (friendIDs.contains(ds.getKey())){

                    }else {
                        friendIDs.add(ds.getKey());
                    }
                }
                displayAllFriends(friendIDs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage()); //Don't ignore potential errors!
            }
        };

        friendsRef.addListenerForSingleValueEvent(eventListener);

        /*
        FirebaseUser fBU = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {

            //gets all friends from database reference
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                friends.clear();
                friends = new ArrayList<>();

                for (DataSnapshot dS : snapshot.getChildren()) {
                    User user = dS.getValue(User.class);

                    if (!user.getID().equals(fBU.getUid())) {

                        if (friends.size() > 0) {
                            if (!user.getID().equals(friends.get(friends.size() - 1).getID())) {

                                friends.add(user);
                            }
                        } else {
                            friends.add(user);
                        }
                    }

                }


                friendsAdapter = new FriendsAdapter(getContext(), friends, true);
                friendsView.setAdapter(friendsAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */
    }

    private void displayAllFriends(ArrayList<String> friendIDs) {

        friends = new ArrayList<>();
        friends.clear();

        for (String id : friendIDs) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
            reference.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Boolean add = true;
                        User user = snapshot.getValue(User.class);
                        for(User users:friends){
                            if(users.getID().equals(user.getID())){
                                add=false;
                            }
                        }
                        if(add) {
                            friends.add(user);
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        friendsAdapter = new FriendsAdapter(getContext(), friends, true);
        friendsView.setAdapter(friendsAdapter);
    }
}