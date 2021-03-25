package com.example.androidproject.Chat;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    private Context context;
    private List<GroupChat> chatList;

    private static final int CHAT_TYPE__LEFT=0;
    private static final int CHAT_TYPE_RIGHT=1;
    FirebaseUser fUser;


    public GroupChatAdapter(Context context, List<GroupChat> chatList) {
        this.context = context;
        this.chatList = chatList;
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public GroupChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==CHAT_TYPE_RIGHT) {

            View view = LayoutInflater.from(context).inflate(R.layout.groupchat_item_right, parent, false);
            return new GroupChatAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.groupchat_item_left, parent, false);
            return new GroupChatAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull GroupChatAdapter.ViewHolder holder, int position) {

        GroupChat chat =chatList.get(position);

        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(chat.getTimestamp()));
        String timesent= DateFormat.format("hh:mm", calendar).toString();


        holder.timeText.setText(timesent);
        holder.message.setText(chat.getMessage());
        holder.fromUser.setText(chat.getSenderName());

        if(chat.getSenderUri().equals("default")){
            holder.chatProfilePic.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(chat.getSenderUri()).into(holder.chatProfilePic);
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
        public TextView fromUser;
        public TextView timeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message=itemView.findViewById(R.id.group_chat_message);
            chatProfilePic=itemView.findViewById(R.id.group_sender_pic);
            fromUser=itemView.findViewById(R.id.from);
            timeText=itemView.findViewById(R.id.group_time_text);
        }
    }
}
