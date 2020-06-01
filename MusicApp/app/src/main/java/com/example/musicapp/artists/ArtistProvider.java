package com.example.musicapp.artists;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.db.DatabaseManager;

import java.util.ArrayList;

public class ArtistProvider {
    public static ArrayList<ArtistViewModel> getArtists(DatabaseManager databaseManager){
        ArrayList<ArtistViewModel> artists = new ArrayList<>();
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        String query = "SELECT artist, album_id, path, count(*) as tracks FROM " + Artist.TABLE_NAME + " GROUP BY " + Artist.COLUMN_ARTIST;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
//                ArtistViewModel artist = new ArtistViewModel();
//                artist.setName(cursor.getString(cursor.getColumnIndex(Artist.COLUMN_ARTIST)));
//                artist.setTracks(Integer.parseInt(cursor.getString(cursor.getColumnIndex("tracks"))));
//                artist.setAlbumID(cursor.getInt(8));
//                artist.(Integer.parseInt(cursor.getString(cursor.getColumnIndex("tracks"))));
                artists.add(new ArtistViewModel(cursor.getString(0),
                                                cursor.getString(2),
                                                cursor.getInt(1),
                                                cursor.getInt(3)));
            }
            while(cursor.moveToNext());
        }
        return artists;
    }
}
