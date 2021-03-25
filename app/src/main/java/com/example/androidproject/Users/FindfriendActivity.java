package com.example.androidproject.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.PersonProfileActivity;
import com.example.androidproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindfriendActivity extends AppCompatActivity {

    private ImageButton searchBtn;
    private TextInputEditText searchText;
    private RecyclerView searchResultList;
   private DatabaseReference allUsersDataBaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriend);
        allUsersDataBaseRef= FirebaseDatabase.getInstance().getReference().child("Users");

        searchResultList= findViewById(R.id.FriendView);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));

        searchBtn= findViewById(R.id.SearchButton);
        searchText= findViewById(R.id.SearchText);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTextInput= searchText.getText().toString();

                SearchPeopleAndFriends(searchTextInput.toLowerCase());

            }
        });
    }

    private void SearchPeopleAndFriends(String searchTextInput) {
        Toast.makeText(this,"Searching....",Toast.LENGTH_LONG).show();
        Query searchPeopleAndFriendsQuery= allUsersDataBaseRef.orderByChild("searchname").startAt(searchTextInput.toLowerCase()).endAt(searchTextInput + "\uf8ff" );
        FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>(FindFriends.class,
                R.layout.all_users_display_layout,FindFriendsViewHolder.class,
            searchPeopleAndFriendsQuery ) {
            @Override
            protected void populateViewHolder(FindFriendsViewHolder viewHolder, FindFriends findFriends, int i) {
                viewHolder.setUsername(findFriends.getUsername());
                viewHolder.setImageURL(findFriends.getImageURL());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id= getRef(i).getKey();
                        Intent profileIntent= new Intent(FindfriendActivity.this, PersonProfileActivity.class);
                        profileIntent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileIntent);


                    }
                });
            }
        };
        searchResultList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setImageURL( String imageURL){
            CircleImageView myImage= mView.findViewById(R.id.profile_image);
            Picasso.get().load(imageURL).placeholder(R.drawable.profile1).into(myImage);

        }
        public void setUsername(String username) {
            TextView myName = mView.findViewById(R.id.NameText);
            myName.setText(username);
        }

    }
}