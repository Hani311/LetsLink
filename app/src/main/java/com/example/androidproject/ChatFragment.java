package com.example.androidproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private FriendsAdapter friendsAdapter;
    private List<User> friends;
    private List<String> friendsList;
    private RecyclerView chatFriendsView;
    FirebaseUser fBU;
    DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        chatFriendsView=view.findViewById(R.id.current_chats_view);
        chatFriendsView.setHasFixedSize(true);
        chatFriendsView.setLayoutManager(new LinearLayoutManager(getContext()));
        fBU= FirebaseAuth.getInstance().getCurrentUser();
        friendsList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dS:snapshot.getChildren()){

                    Chat chat = dS.getValue(Chat.class);
                    if(chat.getSender().equals(fBU.getUid())) {

                        friendsList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fBU.getUid())){
                        friendsList.add(chat.getSender());
                    }

                }
                displayAllChatFriends();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateNotifToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    private void displayAllChatFriends() {

        friends=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                friends.clear();


                for(DataSnapshot dS:snapshot.getChildren()){
                    User user = dS.getValue(User.class);

                    if(!user.getID().equals(fBU.getUid())){

                        for(String ID:friendsList){
                            if(user.getID().equals(ID)){
                                Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                                if(friends.size()!=0){
                                    for(User friend:friends){
                                        if(!user.getID().equals(friend.getID())){
                                            friends.add(user);
                                        }
                                    }
                                }
                                else{
                                    friends.add(user);
                                }
                            }

                        }
                    }
                }

                friendsAdapter=new FriendsAdapter(getContext(), friends);
                chatFriendsView.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
    }

    private void updateNotifToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        NotifToken token1=new NotifToken(token);
        reference.child(fBU.getUid()).setValue(token1);


    }
}