package com.example.musicapp.artists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArtistFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_artists, container, false);
        ArrayList<Artist> artists = Artist.getArtists(MainActivity.mDatabaseManager);
        final ListView listView = (ListView)rootView.findViewById(R.id.artistList);

        listView.setAdapter(new ArtistAdapter(this.getContext(), artists));
        return rootView;
    }
}
