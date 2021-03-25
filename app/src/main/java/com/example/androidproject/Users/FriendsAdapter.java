package com.example.androidproject.Users;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidproject.Chat.Chat;
import com.example.androidproject.Chat.LastSentMessage;
import com.example.androidproject.Chat.MessageActivity;
import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private Context context;
    private List<User> usersList;
    private boolean inChat;

    boolean seenMsg;
    private String receiverName;
    private String senderName;
    private String lastSentMesage;
    private String senderConfirm;


    public FriendsAdapter(Context context, List<User> usersList, boolean inChat) {
        this.context = context;
        this.inChat=inChat;
        this.usersList=usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.friends_layout, parent, false);
        return new FriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = usersList.get(position);
        holder.username.setText(user.getUsername());

        if (inChat) {


            getLastMessage(user.getID(), holder.lastMsg);


        } else {
            holder.lastMsg.setVisibility(View.GONE);
        }

        if (inChat) {
            if (user.getStatus().equals("online")) {
                holder.offline.setVisibility(View.GONE);
                holder.online.setVisibility(View.VISIBLE);
            } else {
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.VISIBLE);
            }
        } else {
            holder.online.setVisibility(View.GONE);
            holder.offline.setVisibility(View.GONE);
        }

        try {
            if (user.getImageURL().equalsIgnoreCase("default")) {

                holder.friendsProfilePic.setImageResource(R.mipmap.ic_launcher);
            } else {

                Glide.with(context).load(user.getImageURL()).into(holder.friendsProfilePic);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getID());
                intent.putExtra("imageUri", user.getImageURL());
                //context.startActivity(intent);
                //ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.friendsProfilePic, ViewCompat.getTransitionName(holder.friendsProfilePic));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private ImageView friendsProfilePic;
        private ImageView online;
        private ImageView offline;
        private TextView lastMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.friends_username);
            friendsProfilePic=itemView.findViewById(R.id.friends_profile_image);
            online=itemView.findViewById(R.id.onStatus);
            offline=itemView.findViewById(R.id.offStatus);
            lastMsg=itemView.findViewById(R.id.lastSentText);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void getLastMessage(String senderID, TextView lastMsg){

            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();


            DatabaseReference userReference= FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user = snapshot.getValue(User.class);

                    receiverName=user.getUsername();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            DatabaseReference senderReference= FirebaseDatabase.getInstance().getReference("Users").child(senderID);
            senderReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user = snapshot.getValue(User.class);

                    senderName=user.getUsername();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
            reference.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    for(DataSnapshot dS:snapshot.getChildren()) {
                        Chat chat = dS.getValue(Chat.class);
                        if (chat.getReceiver().equals(fUser.getUid()) && chat.getSender().equals(senderID) ||
                                chat.getReceiver().equals(senderID) && chat.getSender().equals(fUser.getUid())) {
                            lastSentMesage=chat.getMessage();
                            seenMsg=chat.isSeen();
                            senderConfirm=chat.sender;
                            Log.e("senderID", chat.sender);
                            Log.e("receiverID", fUser.getUid());
                            if(chat.getSender().equals(senderID)){}

                        }
                    }
            try {
                switch (lastSentMesage) {
                    case "":
                        lastMsg.setText("");
                        break;

                    default:
                        if (senderConfirm.equals(fUser.getUid())) {
                            lastMsg.setText("You : " + lastSentMesage);
                        } else {
                            lastMsg.setText(senderName + " : " + lastSentMesage);
                            if (!seenMsg) {
                                lastMsg.setTypeface(lastMsg.getTypeface(), Typeface.BOLD);
                            } else {
                                lastMsg.setTypeface(lastMsg.getTypeface(), Typeface.NORMAL);
                            }
                        }

                        break;

                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

}