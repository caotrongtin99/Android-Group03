package com.example.musicapp.tracks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class TrackFragment extends Fragment {
    private ListTracksAdapter listTracksAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_track, container, false);
        List<Track> tracks = getListData();
        final ListView listView = (ListView)rootView.findViewById(R.id.listview_track);
        listTracksAdapter = new ListTracksAdapter(getActivity(),tracks);
        listView.setAdapter(listTracksAdapter);
        return rootView;
    }

    private  List<Track> getListData() {
        List<Track> list = new ArrayList<Track>();
        Track hello = new Track("hello", "Hello", "Adele");
        Track lalala = new Track("hello", "Hello", "Adele");
        Track layMeDown = new Track("hello", "Hello", "Adele");
        Track maps = new Track("hello", "Hello", "Adele");
        Track oneCallWay= new Track("hello", "Hello", "Adele");


        list.add(hello);
        list.add(lalala);
        list.add(layMeDown);
        list.add(maps);
        list.add(oneCallWay);

        return list;
    }
}
