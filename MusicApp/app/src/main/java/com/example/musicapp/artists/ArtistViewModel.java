package com.example.musicapp.artists;

import android.graphics.Bitmap;

public class ArtistViewModel extends Artist {
    public ArtistViewModel(String artist, String  path, int albumid, int numberOfSongs) {
        super(artist, path, albumid, numberOfSongs);
    }
    private Bitmap bitmap = null;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Artist getAlbumModel(){
        return new Artist(super.getName(), super.getPath(), super.getAlbumID(),super.getTracks());
    }
}
