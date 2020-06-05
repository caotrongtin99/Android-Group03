package com.example.musicapp.play;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.SongModel;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PlayModel {
    public static final String TABLE_NAME = "plays";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SONG_ID = "song_id";
    public static final String COLUMN_IS_PLAYING = "is_playing";
    public static final String COLUMN_CURRENT_DURATION = "current_duration";
    public static final String COLUMN_CREATE_DATE = "create_date";

    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_SONG_ID).append(" INTEGER , ")
            .append(COLUMN_IS_PLAYING).append(" INTEGER, ")
            .append(COLUMN_CURRENT_DURATION).append(" INTEGER,")
            .append(COLUMN_CREATE_DATE).append(" DATETIME ")
            .append(" )")
            .toString();

    private Context context;
    private static DatabaseManager mDatabaseManager = DatabaseManager.getInstance();

    private int id;
    private int songId;
    private int isPlaying;
    private int current_duration;
    private static final String TAG = "PlayModel";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(int isPlaying) {
        this.isPlaying = isPlaying;
    }

    public int getCurrent_duration() {
        return current_duration;
    }

    public void setCurrent_duration(int current_duration) {
        this.current_duration = current_duration;
    }

    private static String getDateTimeNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean updateStatusPlaying(int oldSongId, int newSongId) {
        Log.d(TAG, "updateStatusPlaying: OLD= " + oldSongId + ", New=" + newSongId);
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        ContentValues oldValue = new ContentValues();
        oldValue.put(COLUMN_IS_PLAYING, 0);
        ContentValues newValue = new ContentValues();
        newValue.put(COLUMN_IS_PLAYING, 1);
        int oldResult = db.update(TABLE_NAME, oldValue, COLUMN_SONG_ID + "=" + oldSongId, null);
        int newResult = db.update(TABLE_NAME, newValue, COLUMN_SONG_ID + "=" + newSongId, null);
        return oldResult > 0 && newResult > 0;
    }

    public static SongModel getSongIsPlaying() {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        String query = "SELECT s.* FROM " + PlayModel.TABLE_NAME + " pm INNER JOIN " + SongModel.TABLE_NAME + " s ON pm." + PlayModel.COLUMN_SONG_ID
                + "= s." + SongModel.COLUMN_SONG_ID + " WHERE pm." + COLUMN_IS_PLAYING + "=1" + " ORDER BY s." + SongModel.COLUMN_TITLE + " ";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                SongModel songModel = new SongModel();
                songModel.setId(cursor.getInt(cursor.getColumnIndex(SongModel.COLUMN_ID)));
                songModel.setSongId(cursor.getInt(cursor.getColumnIndex(SongModel.COLUMN_SONG_ID)));
                songModel.setTitle(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_TITLE)));
                songModel.setAlbum(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_ALBUM)));
                songModel.setArtist(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_ARTIST)));
                songModel.setFolder(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_FOLDER)));
                songModel.setDuration(cursor.getLong(cursor.getColumnIndex(SongModel.COLUMN_DURATION)));
                songModel.setPath(cursor.getString(cursor.getColumnIndex(SongModel.COLUMN_PATH)));
                return songModel;
            } while (cursor.moveToNext());
        }
        return null;
    }
}