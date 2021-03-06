package com.example.musicapp.artists;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.db.DatabaseManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Artist implements Serializable {
    private String name;
    private String path;
    private int tracks;
    private int albumID;
    public static final String TABLE_NAME = "songs";
    public static final String COLUMN_ARTIST = "artist";

    public String getName(){
        return this.name;
    }

    public String getPath( ){
        return path;
    }

    public Artist(String aName, String aPath, int id, int i) {
        path = aPath;
        name = aName;
        tracks = i;
        albumID = id;
    }

    public int getAlbumID() { return albumID; }

    public int getTracks(){
        return tracks;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTracks(int tracks) {this.tracks = tracks;}

}
