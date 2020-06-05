package com.example.musicapp.artists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;
import java.util.List;

public class ArtistSongsActivity extends AppCompatActivity {
    private String artistName;
    private RecyclerView RVartist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_songs);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        artistName = intent.getStringExtra("artist");
        TextView txt = findViewById(R.id.artistSongNameArtist);
        txt.setText(artistName);

        final ArrayList<SongModel> songs = SongModel.getSongsByArtist(MainActivity.mDatabaseManager, artistName);
        ArtistSongsAdapter ASA = new ArtistSongsAdapter(this, songs);
        RVartist = findViewById(R.id.trackList);
        RVartist.setLayoutManager(new LinearLayoutManager(this));
        RVartist.setHasFixedSize(true);
        RVartist.setAdapter(ASA);

        TextView txt_size = findViewById(R.id.sizeOfSongs);
        if(songs.size() > 1){
            txt_size.setText(songs.size() + " tracks");
        }
        else{
            txt_size.setText(songs.size() + " track");
        }
    }
}
