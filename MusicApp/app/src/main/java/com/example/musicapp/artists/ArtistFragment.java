package com.example.musicapp.artists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.musicapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArtistFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_artists, container, false);
        List<Artist> artists = getListData();
        final ListView listView = (ListView)rootView.findViewById(R.id.artistList);

        listView.setAdapter(new ArtistAdapter(this.getContext(), artists));
        return rootView;
    }

    private  List<Artist> getListData() {
        List<Artist> list = new ArrayList<Artist>();
        List<String> tracks = new ArrayList<String>();
        List<String> tracks2 = new ArrayList<String>();
        tracks.add("Hello");
        tracks.add("Rolling in the deep");
        tracks2.add("Attention");
        tracks2.add("We don't talk anymore");
        tracks2.add("One call away");
        Artist adele = new Artist("Adele", tracks);
        Artist charlie = new Artist("Charlie Puth", tracks2);


        list.add(adele);
        list.add(charlie);

        return list;
    }
}
