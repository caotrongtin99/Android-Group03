package com.example.musicapp.artists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;
import java.util.List;

public class ArtistSongsActivity extends AppCompatActivity {
    private String artistName;
    private ArrayList<SongModel> songs;
    private String songsSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_songs);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        artistName = intent.getStringExtra("artist");
        TextView txt = findViewById(R.id.artistSongNameArtist);
        txt.setText(artistName);

        songs = SongModel.getSongsByArtist(MainActivity.mDatabaseManager, artistName);

        TextView txt_size = findViewById(R.id.sizeOfSongs);
        if(songs.size() > 1){
            txt_size.setText(songs.size() + "tracks");
        }
        else{
            txt_size.setText(songs.size() + "track");
        }

        final ListView listSong = (ListView) findViewById(R.id.trackList);
        listSong.setAdapter(new ArtistSongsAdapter(this, songs));
    }
}
