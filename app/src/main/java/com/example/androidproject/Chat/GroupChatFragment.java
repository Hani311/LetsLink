package com.example.androidproject.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidproject.R;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GroupChatFragment extends Fragment {

    private RecyclerView groupsView;
    private FirebaseAuth auth;
    private ArrayList<Group> groups;
    private GroupAdapter groupAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGroups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_group_chat, container, false);
        groupsView=view.findViewById(R.id.groups_recycler_view);
        groupsView.setHasFixedSize(true);
        groupsView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth=FirebaseAuth.getInstance();


        return view;
    }

    private void loadGroups() {

        groups=new ArrayList<>();
        groups.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dS:snapshot.getChildren()){
                    if(dS.child("Participants").child(auth.getCurrentUser().getUid()).exists()){

/*
                        String description =dS.child("description").getValue(String.class);
                        String groupAdmin =dS.child("groupAdmin").getValue(String.class);
                        String groupID =dS.child("groupID").getValue(String.class);
                        String groupIcon =dS.child("groupIcon").getValue(String.class);
                        String timeCreated=dS.child("timeCreated").getValue(String.class);
                        String title =dS.child("title").getValue(String.class);

                        Group group=new Group(groupID, title, description, groupAdmin, groupIcon, timeCreated);
 */

                        String groupID =dS.child("groupID").getValue(String.class);

                        Boolean add = true;
                        Group group=dS.getValue(Group.class);

                        for(Group group1:groups){
                            if(group1.getGroupID().equals(groupID)){
                                add=false;
                            }
                        }
                        if(add) {
                            groups.add(group);
                        }
                    }
                }
                groupAdapter=new GroupAdapter(getContext(), groups);
                groupsView.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findGroupChat(String groupname) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dS : snapshot.getChildren()) {
                    if (!dS.child("Participants").child(auth.getCurrentUser().getUid()).exists()) {

                        if (dS.child("title").toString().toLowerCase().contains(groupname.toLowerCase())) {

                            Group group = dS.getValue(Group.class);
                            groups.add(group);
                        }
                    }
                    groupAdapter = new GroupAdapter(getActivity(), groups);
                    groupsView.setAdapter(groupAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}