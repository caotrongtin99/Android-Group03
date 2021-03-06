package com.example.musicapp.artists;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.musicapp.albums.AlbumViewModel;
import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;

public class ArtistProvider {
    public static ArrayList<ArtistViewModel> getArtistModelPaging(Context context, String search, int skip, int take){
        ArrayList<ArtistViewModel> res = new ArrayList<ArtistViewModel>();
        DatabaseManager dbManager = DatabaseManager.newInstance(context);
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String[] tableColumns = new String[] {
                SongModel.COLUMN_ARTIST,
                SongModel.COLUMN_PATH,
                SongModel.COLUMN_ALBUM_ID,
                "COUNT(*)"
        };
        String whereClause =  "? = '' OR " + SongModel.COLUMN_ARTIST +" LIKE ?";
        String[] whereArgs = new String[]{search ,"%" + search + "%"};
        String groupBy = String.format("%s LIMIT %d,%d",SongModel.COLUMN_ARTIST,skip,take);
        String query = "SELECT artist, path, album_id, COUNT(*) from songs where artist  = '" + search + "'" +
                        "or artist LIKE '%" + search + "%'" + "GROUP BY artist LIMIT " + skip + ", " + take;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                res.add(new ArtistViewModel(cursor.getString(0)
                                            ,cursor.getString(1)
                                            ,cursor.getInt(2)
                                            ,cursor.getInt(3)));
            } while (cursor.moveToNext());
        }
        return res;
    }
}
