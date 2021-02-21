package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private Context context;
    private List<Chat> chatList;
    private String imageurl;
    private static final int CHAT_TYPE__LEFT=0;
    private static final int CHAT_TYPE_RIGHT=1;
    FirebaseUser fUser;


    public MessageAdapter(Context context, List<Chat> chatList, String imageurl) {
        this.context = context;
        this.chatList = chatList;
        this.imageurl= imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==CHAT_TYPE_RIGHT) {

            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else{


            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat =chatList.get(position);
        holder.message.setText(chat.getMessage());

        if(imageurl.equals("default")){
            holder.chatProfilePic.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageurl);
        }
    }

    @Override
    public int getItemViewType(int position) {

        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fUser.getUid())){
            return CHAT_TYPE_RIGHT;
        }
        else{
            return CHAT_TYPE__LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView message;
        public ImageView chatProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message=itemView.findViewById(R.id.chat_message);
            chatProfilePic=itemView.findViewById(R.id.civ_in_chat);
        }
    }
}
