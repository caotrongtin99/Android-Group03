package com.example.musicapp.artists;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.db.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private String name;
    //private String pic;
    private int tracks;
    public static final String TABLE_NAME = "songs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SONG_ID = "song_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_FOLDER = "folder";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_ALBUM_ID = "album_id";

    public String getName(){
        return this.name;
    }

//    public  String getPic(){
//        return this.pic;
//    }

    public int getTracks(){
        return tracks;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTracks(int tracks) {this.tracks = tracks;}

//    public void setPic(String pic){
//        this.pic = pic;
//    }

//    public void setTracks(List<String> tracks){
//        this.tracks = tracks;
//    }


    public static ArrayList<Artist> getArtists(DatabaseManager databaseManager){
        ArrayList<Artist> artists = new ArrayList<>();
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        String query = "SELECT artist, count(*) as tracks FROM " + Artist.TABLE_NAME + " GROUP BY " + Artist.COLUMN_ARTIST;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Artist artist = new Artist();
                artist.setName(cursor.getString(cursor.getColumnIndex(Artist.COLUMN_ARTIST)));
                artist.setTracks(Integer.parseInt(cursor.getString(cursor.getColumnIndex("tracks"))));
                artists.add(artist);
            }
            while(cursor.moveToNext());
        }
        return artists;
    }
}
