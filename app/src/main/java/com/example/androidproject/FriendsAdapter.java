package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private Context context;
    private List<User> usersList;

    public FriendsAdapter(Context context, List<User> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.friends_layout, parent, false);
        return new FriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = usersList.get(position);
        holder.username.setText(user.getUsername());

        if(user.getImageURL().equals("default")){

            holder.friendsProfilePic.setImageResource(R.mipmap.ic_launcher);
        }
        else{

            Glide.with(context).load(user.getImageURL()).into(holder.friendsProfilePic);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView friendsProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.friends_username);
            friendsProfilePic=itemView.findViewById(R.id.friends_profile_image);
        }
    }
}
