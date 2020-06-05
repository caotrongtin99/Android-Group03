package com.example.musicapp.play;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.musicapp.listsong.SongModel;

public class FragmentPlayAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;
    private SongModel playingSong;
    private static  Fragment fragmentPlayingList, fragmentPlaying;

    public FragmentPlayAdapter(@NonNull FragmentManager fm) {
        super(fm);
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
                fragment = fragmentPlaying;
                Bundle bundle=new Bundle();
                bundle.putSerializable("PLAY_SONG", playingSong);
                fragment.setArguments(bundle);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    public FragmentPlaying getFragmentPlaying() {
        return (FragmentPlaying) fragmentPlaying;
    }

    public FragmentPlayingList getFragmentListPlaying(){
        return (FragmentPlayingList) fragmentPlayingList;
    }
}
