package com.example.androidproject.Chat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.androidproject.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class MessagesFragment extends Fragment {


    private ArrayList<String> titles=new ArrayList<>();
    private ChatAdapter chatAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        chatAdapter= new ChatAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        titles.add("Friends");
        titles.add("Groups");
        TabLayout tablLayout=rootView.findViewById(R.id.messagesTabLayout);
        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.messageViewPager);

        //mViewPager.setAdapter(new ChatAdapter(getChildFragmentManager()));
        mViewPager.setAdapter(chatAdapter);
        tablLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    public class ChatAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments= new ArrayList<>();

        public ChatAdapter(@NonNull FragmentManager fm) {
            super(fm);

            fragments.add(new FriendsMessagesFragment());
            fragments.add(new GroupChatFragment());

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            if(position==0){
                return "Chats";
            }
            else if (position==1){
                return "Groups";
            }
            else{
                return null;
            }
        }
    }
}