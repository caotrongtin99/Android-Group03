package com.example.musicapp.artists;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;

public class ArtistsSongModel {
    private int songID;
    private String songName;
    private String artist;
    private String duration;

    public ArtistsSongModel(int id, String name, String art, String dr){
        songID = id;
        songName = name;
        artist = art;
        duration = dr;
    }

    public void setSongID(int id){
        songID = id;
    }

    public int getSongID(){
        return songID;
    }

    public void setSongName(String name){
        songName = name;
    }

    public String getSongName(){
        return songName;
    }

    public void setArtist(String art){
        artist = art;
    }

    public String getArtist(){
        return artist;
    }

    public void setDuration(String dr){
        duration = dr;
    }

    public String getDuration(){
        return duration;
    }

    public static ArrayList<SongModel> getSongsByArtist(DatabaseManager databaseManager, String name){
        ArrayList<SongModel> songs = new ArrayList<>();
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        String art = "'" + name + "'";
        String query = "SELECT * FROM " + Artist.TABLE_NAME + " WHERE " + Artist.COLUMN_ARTIST + " = " + art;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                SongModel songModel = new SongModel();
                songModel.setId(cursor.getInt(cursor.getColumnIndex(SongModel.COLUMN_ID)));
                songModel.setSongId(cursor.getInt(cursor.getColumnIndex(SongModel.COLUMN_SONG_ID)));
                songModel.setTitle(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_TITLE)));
                songModel.setAlbum(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_ALBUM)));
                songModel.setArtist(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_ARTIST)));
                songModel.setFolder(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_FOLDER)));
                songModel.setDuration(cursor.getLong(cursor.getColumnIndex(SongModel.COLUMN_DURATION)));
                songModel.setPath(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_PATH)));
                songModel.setAlbumId(cursor.getInt(cursor.getColumnIndex(SongModel.COLUMN_ALBUM_ID)));
                songs.add(songModel);
            }
            while(cursor.moveToNext());
        }
        return songs;
    }
}
