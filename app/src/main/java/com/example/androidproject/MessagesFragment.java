package com.example.androidproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class MessagesFragment extends Fragment {


    private ArrayList<String> titles=new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        titles.add("Chats");
        titles.add("Friends");
        ViewPager2 mViewPager = (ViewPager2) rootView.findViewById(R.id.messageViewPager);
        //mViewPager.setAdapter(new ChatAdapter(getChildFragmentManager()));
        mViewPager.setAdapter(new ChatAdapter(this));
        new TabLayoutMediator(rootView.findViewById(R.id.messagesTabLayout), mViewPager,
                (tab, position) -> tab.setText(titles.get(position))).attach();

        return rootView;
    }

    public class ChatAdapter extends FragmentStateAdapter {

        private ArrayList<Fragment> messageFragments;

        ChatAdapter(MessagesFragment fragmentActivity) {
            super(fragmentActivity);
            this.messageFragments=new ArrayList<>();
        }

        /*
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return messageFragments.get(position);
        }


        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return messageFragments.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return false;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
         */

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ChatFragment();
                case 1:
                    return new FriendsMessagesFragment();
            }
            return new ChatFragment();
        }

        @Override
        public int getItemCount() {

            return titles.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public void addFragment(Fragment fragment, String title){
            messageFragments.add(fragment);
            titles.add(title);
        }

        /*
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }



        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

         */
    }
}