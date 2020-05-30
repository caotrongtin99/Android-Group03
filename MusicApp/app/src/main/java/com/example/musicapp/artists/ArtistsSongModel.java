package com.example.musicapp.artists;

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
}
