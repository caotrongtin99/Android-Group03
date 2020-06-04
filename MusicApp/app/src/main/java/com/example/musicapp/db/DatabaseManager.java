package com.example.musicapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicapp.listsong.SongModel;

public class DatabaseManager extends SQLiteOpenHelper {

    private static DatabaseManager mDatabaseInstance = null;

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private int mOpenCounter;

    public static DatabaseManager getInstance() {
        return mDatabaseInstance;
    }

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "music_db.db";
    //singleton
    public static DatabaseManager newInstance(Context context) {
        if (mDatabaseInstance == null) {
            mDatabaseInstance = new DatabaseManager(context.getApplicationContext());
        }
        return mDatabaseInstance;
    }

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SongModel.SCRIPT_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SongModel.TABLE_NAME);
        onCreate(db);
    }

    public void resetDB() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + SongModel.TABLE_NAME);
        onCreate(db);
    }
}