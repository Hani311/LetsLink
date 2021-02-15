package com.example.androidproject;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

public class ChatAdapter extends PagerAdapter {
    public ChatAdapter(FragmentManager childFragmentManager) {
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
