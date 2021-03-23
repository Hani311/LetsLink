package com.example.androidproject.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_group_chat, container, false);
        groupsView=view.findViewById(R.id.groups_recycler_view);

        auth=FirebaseAuth.getInstance();

        loadGroups();

        return view;
    }

    private void loadGroups() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dS:snapshot.getChildren()){
                    if(!dS.child("Participants").child(auth.getCurrentUser().getUid()).exists()){
                        Group group=dS.getValue(Group.class);
                        groups.add(group);
                    }
                }
                groupAdapter=new GroupAdapter(getActivity(), groups);
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