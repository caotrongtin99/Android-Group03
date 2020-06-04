package com.example.musicapp.play;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.musicapp.listsong.SongModel;

public class FragmentPlayAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;
    private SongModel playingSong;
    private static  Fragment fragmentPlayingList, fragmentPlaying;

    public FragmentPlayAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragmentPlayingList = FragmentPlayingList.newInstance();
                fragment = fragmentPlayingList;
                break;
            case 1:
                fragmentPlaying = new FragmentPlaying();
        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
