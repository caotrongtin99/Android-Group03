package com.example.musicapp.artists;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.txt_ArtistName);
                String artist = tv.getText().toString();
                Intent intent = new Intent(MainActivity.getMainActivity(), ArtistSongsActivity.class);
                intent.putExtra("artist", artist);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
