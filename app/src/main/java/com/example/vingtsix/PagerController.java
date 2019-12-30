package com.example.vingtsix;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {
    int tabCounts;
    public PagerController(@NonNull FragmentManager fm, int tabCounts) {
        super(fm);
        this.tabCounts = tabCounts;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new AllMusic();

            case 1:
                return new Albums();
            case 2:
                return new Playlists();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
