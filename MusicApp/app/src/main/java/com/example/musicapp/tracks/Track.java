package com.example.musicapp.tracks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class Track {
    public static final String TABLE_NAME = "tracks";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SONG_ID = "song_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_FOLDER = "folder";
    public static final boolean COLUMN_IS_FAVORITES = false;
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_ALBUM_ID = "album_id";

    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_SONG_ID).append(" INTEGER , ")
            .append(COLUMN_TITLE).append(" TEXT,")
            .append(COLUMN_ALBUM).append(" TEXT,")
            .append(COLUMN_ARTIST).append(" TEXT,")
            .append(COLUMN_DURATION).append(" INTEGER,")
            .append(COLUMN_FOLDER).append(" TEXT ,")
            .append(COLUMN_IS_FAVORITES).append(" BOOLEAN ,")
            .append(COLUMN_PATH).append(" TEXT ,")
            .append(COLUMN_ALBUM_ID).append(" INTEGER ")
            .append(" )")
            .toString();

    private String path;
    private String title;
    private String album;
    private String artist;
    private String imageName;
    private Long duration;
    private int id;
    private int songId;
    private String folder;
    private boolean isFavorite;
    private int albumId;
    private boolean isChecked;

    public  Track(String imageName,String title, String artist){
        this.imageName=imageName;
        this.title=title;
        this.artist = artist;
    }

    public Track() {

    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageName() {
        return imageName;
    }

    public void setBitmap(String imageName) {
        this.imageName = imageName;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static ArrayList<Track> getAllAudioFromDevice(final Context context) {
        final ArrayList<Track> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {

                MediaStore.Audio.AudioColumns.DATA,//path
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.ALBUM_ID,

        };
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";
        Cursor c = context.getContentResolver().query(uri, projection, selection, null, null);
        int debugLoop = 40;
        if (c != null) {
            int count = 0;

            while (c.moveToNext()) {// && count++<debugLoop
                count++;
//                Log.d(TAG, "getAllAudioFromDevice: " + count);
                Track songModel = new Track();
                String path = c.getString(0);//c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
                String name = c.getString(1);//c.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
                String album = c.getString(2);//c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)
                String artist = c.getString(3);//c.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
                Long duration = c.getLong(4);//c.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)
                int songId = c.getInt(5);
                int albumId = c.getInt(c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
//                Log.d(TAG, "getAllAudioFromDevice: ALBUM ID" + albumId);
                String parentPath = new File(path).getParent();
                String folder = parentPath.substring(parentPath.lastIndexOf('/') + 1);

//
//
//                if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
//                    inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
//                    mediaMetadataRetriever.release();
//                    bitmap = BitmapFactory.decodeStream(inputStream);
//                } else {
//                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.musical_note_light_64);
//                }
                songModel.setTitle(name);
                songModel.setAlbum(album);
                songModel.setArtist(artist);
                songModel.setPath(path);
                songModel.setBitmap(null);
                songModel.setDuration(duration);
                songModel.setSongId(songId);
                songModel.setFolder(folder);
                songModel.setAlbumId(albumId);
//                Log.e("Name :" + name, " Album :" + album);
//                Log.e("Path :" + path, " artist :" + artist);

                tempAudioList.add(songModel);
            }
            c.close();
        }
        return tempAudioList;
    }

}
