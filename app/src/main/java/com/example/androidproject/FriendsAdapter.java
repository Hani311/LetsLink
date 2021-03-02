package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private Context context;
    private List<User> usersList;
    private boolean inChat;

    public FriendsAdapter(Context context, List<User> usersList, boolean inChat) {
        this.context = context;
        this.usersList = usersList;
        this.inChat=inChat;
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

        if(inChat){
            if(user.getStatus().equals("online")){
                holder.offline.setVisibility(View.GONE);
                holder.online.setVisibility(View.VISIBLE);
            }
            else{
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.online.setVisibility(View.GONE);
            holder.offline.setVisibility(View.GONE);
        }

        try {
            if (user.getImageURL().equalsIgnoreCase("default")) {

                holder.friendsProfilePic.setImageResource(R.mipmap.ic_launcher);
            } else {

                Glide.with(context).load(user.getImageURL()).into(holder.friendsProfilePic);
            }
        }
        catch (NullPointerException e){

        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView friendsProfilePic;
        public ImageView online;
        public ImageView offline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.friends_username);
            friendsProfilePic=itemView.findViewById(R.id.friends_profile_image);
            online=itemView.findViewById(R.id.onStatus);
            offline=itemView.findViewById(R.id.offStatus);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
