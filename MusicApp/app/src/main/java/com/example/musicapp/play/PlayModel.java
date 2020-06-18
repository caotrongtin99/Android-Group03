package com.example.musicapp.play;

import android.annotation.SuppressLint;
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

    private static DatabaseManager databaseManager = DatabaseManager.getInstance();

    private int id;
    private int songId;
    private int isPlaying;
    private int current_duration;
    private static final String TAG = "PlayModel";

    public static ArrayList<PlayModel> getPlayingList() {
        ArrayList<PlayModel> playingList = new ArrayList<>();
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        String[] projection = {
                PlayModel.COLUMN_ID,
                PlayModel.COLUMN_SONG_ID,
                PlayModel.COLUMN_IS_PLAYING,
                PlayModel.COLUMN_CURRENT_DURATION,
                PlayModel.COLUMN_CREATE_DATE
        };
        String orderBy = PlayModel.COLUMN_CREATE_DATE + " DESC ";
        Cursor cursor = db.query(PlayModel.TABLE_NAME, projection, null, null, null, null, orderBy);
        if (cursor.moveToFirst()) {
            do {
                PlayModel play = new PlayModel();
                play.setId(cursor.getInt(cursor.getColumnIndex(PlayModel.COLUMN_ID)));
                play.setSongId(cursor.getInt(cursor.getColumnIndex(PlayModel.COLUMN_SONG_ID)));
                play.setIsPlaying(cursor.getInt(cursor.getColumnIndex(PlayModel.COLUMN_IS_PLAYING)));
                play.setCurrent_duration(cursor.getInt(cursor.getColumnIndex(PlayModel.COLUMN_CURRENT_DURATION)));

                playingList.add(play);
            } while (cursor.moveToNext());

        }
        return playingList;
    }

    public static ArrayList<SongModel> getPlayingSongs() {
        ArrayList<SongModel> songModelList = new ArrayList<>();
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        String query = "SELECT s.* FROM " + PlayModel.TABLE_NAME + " pm INNER JOIN " + SongModel.TABLE_NAME + " s ON pm." + PlayModel.COLUMN_SONG_ID
                + "= s." + SongModel.COLUMN_SONG_ID + " ORDER BY s." + SongModel.COLUMN_TITLE + " ";
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
                songModelList.add(songModel);
            } while (cursor.moveToNext());
        }
//        cursor.close();
        return songModelList;
    }

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

    public static long addSongToPlayingList(SongModel song) {
        Log.d(TAG, "addSongToPlayingList: SONG_ID" + song.getSongId());
        boolean existSong = isSongExist(song);
        Log.d(TAG, "addSongToPlayingList: EXIST" + existSong);
        if (!existSong) {
            SQLiteDatabase database = databaseManager.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlayModel.COLUMN_SONG_ID, song.getSongId());
            contentValues.put(PlayModel.COLUMN_IS_PLAYING, 0);
            contentValues.put(PlayModel.COLUMN_CURRENT_DURATION, 0);
            contentValues.put(PlayModel.COLUMN_CREATE_DATE, getDateTimeNow());
            long id = database.insert(PlayModel.TABLE_NAME, null, contentValues);
            return id;
        }
        return 0;
    }

    public static boolean isSongExist(SongModel song) {
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        boolean result = false;

        String query = "SELECT " + PlayModel.COLUMN_ID + " FROM " + PlayModel.TABLE_NAME + " WHERE " + PlayModel.COLUMN_SONG_ID +  " = " + song.getSongId();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            result = true;
        }
//        cursor.close();
        return result;
    }

    public static long createPlaylistFromSongs(ArrayList<SongModel> songs) {
        SQLiteDatabase database = databaseManager.getWritableDatabase();
        for (SongModel song : songs) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlayModel.COLUMN_SONG_ID, song.getSongId());
            contentValues.put(PlayModel.COLUMN_IS_PLAYING, 0);
            contentValues.put(PlayModel.COLUMN_CURRENT_DURATION, 0);
            contentValues.put(PlayModel.COLUMN_CREATE_DATE, getDateTimeNow());
            database.insert(PlayModel.TABLE_NAME, null, contentValues);
        }
        return 0;
    }

    public static boolean clearPlayingList() {
        SQLiteDatabase db = databaseManager.getWritableDatabase();
        return db.delete(PlayModel.TABLE_NAME, null, null) > 0;

    }

    public static boolean updateStatusPlaying(int oldSongId, int newSongId) {
        Log.d(TAG, "updateStatusPlaying: OLD= " + oldSongId + ", New=" + newSongId);
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        ContentValues oldValue = new ContentValues();
        oldValue.put(COLUMN_IS_PLAYING, 0);
        ContentValues newValue = new ContentValues();
        newValue.put(COLUMN_IS_PLAYING, 1);
        int oldResult = db.update(TABLE_NAME, oldValue, COLUMN_SONG_ID + "=" + oldSongId, null);
        int newResult = db.update(TABLE_NAME, newValue, COLUMN_SONG_ID + "=" + newSongId, null);
        return oldResult > 0 && newResult > 0;
    }

    public static SongModel getSongIsPlaying() {
        SQLiteDatabase db = databaseManager.getReadableDatabase();
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

    public static void deleteSongsPlayingList(String Ids) {
        SQLiteDatabase db = DatabaseManager.getInstance().getWritableDatabase();
        db.execSQL(String.format("DELETE FROM %s WHERE %s IN (%s);",TABLE_NAME,COLUMN_SONG_ID, Ids));
    }
}