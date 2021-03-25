package com.example.androidproject.Chat;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidproject.R;
import com.example.androidproject.Users.User;
import com.example.androidproject.Users.UserSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Group> groups;
    String username;
    String userUri;


    public GroupAdapter(Context context, ArrayList<Group> groups) {
        this.context=context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.group_layout, parent, false);
        return new GroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FirebaseUser fBU= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(fBU.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                username=(user.getUsername());
                userUri=user.getImageURL();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Group group=groups.get(position);
        String groupId=group.getGroupID();
        String groupTitle=group.getTitle();
        String groupDescription=group.getDescription();
        String groupIcon=group.getGroupIcon();

        holder.lastMessage.setText("");
        holder.lastTime.setText("");

        getLastMessage(group, holder);

        holder.groupTitle.setText(groupTitle);

        try{
            if (groupIcon.equalsIgnoreCase("default")) {

                holder.groupIcon.setImageResource(R.drawable.ic_baseline_group_24);
            }
            else {
                Glide.with(context).load(groupIcon).into(holder.groupIcon);
            }
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupID", groupId);
                intent.putExtra("groupIcon", groupIcon);
                intent.putExtra("groupTitle", groupTitle);
                intent.putExtra("currentUri", userUri);
                intent.putExtra("currentName", username);
                context.startActivity(intent);
            }
        });

    }

    private void getLastMessage(Group group, ViewHolder holder) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(group.getGroupID()).child("Messages").limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dS:snapshot.getChildren()){
                            String message=""+dS.child("message").getValue();
                            String timestamp=""+dS.child("timestamp").getValue();
                            String sender=""+dS.child("sender").getValue();

                            Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(Long.parseLong(timestamp));
                            String timesent= DateFormat.format("hh:mm", calendar).toString();

                            holder.lastTime.setText(timesent);


                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child(sender);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String sendername=""+snapshot.child("username").getValue();
                                    String lastText=sendername+": "+message;
                                    holder.lastMessage.setText(lastText);
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

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView groupTitle, lastMessage, lastTime;
        private CircleImageView groupIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupIcon=itemView.findViewById(R.id.group_icon);
            groupTitle=itemView.findViewById(R.id.group_title);
            lastMessage=itemView.findViewById(R.id.group_last_msg);
            lastTime=itemView.findViewById(R.id.group_last_time);
        }

    }
}
