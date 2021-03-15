package com.example.androidproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidproject.Notifications.NotifToken;
import com.example.androidproject.Users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ChatFragment extends Fragment {

    private FriendsAdapter friendsAdapter;
    private List<User> friends;
    private List<String> friendsList;
    private RecyclerView chatFriendsView;
    FirebaseUser fBU;
    DatabaseReference reference;
    private List<Chatlist> usersList;

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
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatFriendsView = view.findViewById(R.id.current_chats_view);
        chatFriendsView.setHasFixedSize(true);
        chatFriendsView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsList = new ArrayList<>();
        fBU = FirebaseAuth.getInstance().getCurrentUser();

        /*
        reference=FirebaseDatabase.getInstance().getReference("Chatlist").child(fBU.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friends=new ArrayList<>();
                friends.clear();
                usersList=new ArrayList<Chatlist>();
                try {
                    usersList.clear();
                }
                catch (NullPointerException e){}
                for(DataSnapshot dS:snapshot.getChildren()){
                    Chatlist chatList =dS.getValue(Chatlist.class);
                    //Log.e("chatlist", chatList.getId().trim());
                    usersList.add(chatList);
                }
                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users");
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        friends=new ArrayList<>();
                        friends.clear();
                        for(DataSnapshot dS:snapshot.getChildren()){
                            User user = dS.getValue(User.class);
                            DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getID());
                            reference2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //ArrayList<String> limited=null;
                                    for(DataSnapshot dS:snapshot.getChildren()){
                                        Log.e("friendsSize", String.valueOf(friends.size()));
                                        Chatlist chatList =dS.getValue(Chatlist.class);
                                        if() {
                                            //limited.add(dS.getKey());
                                            friends.add(user);
                                            /*
                                            try {
                                                for(User temp:friends){
                                                    Log.e("temp", temp.getID());
                                                    Log.e("new", user.getID());
                                                    if (temp.getID().equals(user.getID())){
                                                        friends.remove(temp);
                                                } else{
                                                    friends.add(user); }
                                                }
                                            }catch(ConcurrentModificationException e){
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                chatList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        */






        /*
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
    */



        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Chatlist");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList=new ArrayList<>();
                try {
                    usersList.clear();


                for (DataSnapshot dS : snapshot.getChildren()) {
                    Chatlist chatList = dS.getValue(Chatlist.class);

//                   Log.e("chatlistID", chatList.getFrom());
                    if(chatList.getId().equals(fBU.getUid())){
                        usersList.add(chatList);
                    }
                    else if(chatList.getFrom().equals(fBU.getUid())){
                        usersList.add(chatList);
                    }
                    Log.e("newID", String.valueOf(usersList.size()));
                    //limited.add(dS.getKey());
                }
                }catch (NullPointerException e){}


                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        /*
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dS : snapshot.getChildren()) {
                    usersList=new ArrayList<Chatlist>();
                    usersList.clear();
                    User user = dS.getValue(User.class);
                    Log.e("userId",user.getID());
                }
            }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
         */

        updateNotifToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    private void chatList() {




        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                friends=new ArrayList<>();
                friends.clear();

                for (DataSnapshot dS : snapshot.getChildren()) {
                    User user = dS.getValue(User.class);

                    ArrayList<String> friendNames = new ArrayList<>();

                    for (Chatlist chatList : usersList) {

                        //Log.e("info",chatList.getId());

                        Log.e("info1", fBU.getUid());
                        Log.e("info3",user.getID());
                        Log.e("usersList", String.valueOf(usersList.size()));



                        if ((fBU.getUid().equals(chatList.getId()))&&user.getID().equals(chatList.getFrom())){
                                    /*&& chatList.getFrom().equals(user.getID()))
                                    || (fBU.getUid().equals(chatList.getFrom()) && chatList.getId().equals(user.getID())))
                                    {
                                     */
                            try {

                                if(!friends.get(friends.size()-1).getID().equals(user.getID())) {
                                    friends.add(user);
                                }
                            }catch (IndexOutOfBoundsException|NullPointerException e){
                                friends.add(user);
                                friendNames.add(chatList.getFrom());
                            }

                            Log.e("friendsSize", String.valueOf(friends.size()));


                        }
                        else if((fBU.getUid().equals(chatList.getFrom()))&&user.getID().equals(chatList.getId())){

                            try {

                                if(!friends.get(friends.size()-1).getID().equals(user.getID())) {
                                    friends.add(user);
                                }

                            }catch (IndexOutOfBoundsException|NullPointerException e){
                                friends.add(user);
                                friendNames.add(chatList.getId());
                            }
                            Log.e("friendsSize", String.valueOf(friends.size()));
                        }
                        // }
                    }
                }

                //friends.remove(friends.size()-1);

                friendsAdapter=new FriendsAdapter(getContext(), friends, true);
                chatFriendsView.setAdapter(friendsAdapter);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void displayAllChatFriends() {

        friends = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                friends.clear();


                for (DataSnapshot dS : snapshot.getChildren()) {
                    User user = dS.getValue(User.class);

                    if (!user.getID().equals(fBU.getUid())) {

                        for (String ID : friendsList) {
                            if ((user.getID().equals(ID))) {
                                //Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                                if (friends.size() != 0) {
                                    try {
                                        for (User friend : friends) {
                                            if (!user.getID().equals(friend.getID())) {
                                                friends.add(user);
                                            }
                                        }
                                    } catch (ConcurrentModificationException e) {
                                    }
                                } else {
                                    friends.add(user);
                                }
                            }
                        }
                    }
                }
                Log.e("friendsSize", String.valueOf(friends.size()));
                friendsAdapter=new FriendsAdapter(getContext(), friends, true);
                chatFriendsView.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateNotifToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        NotifToken token1 = new NotifToken(token);
        reference.child(fBU.getUid()).setValue(token1);

    }
}