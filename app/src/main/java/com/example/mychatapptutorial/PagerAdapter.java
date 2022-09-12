package com.example.mychatapptutorial;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    int tabcount;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return new ChatFragment();

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
