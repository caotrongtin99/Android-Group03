package com.example.musicapp.artists;

import com.example.musicapp.tracks.Track;

import java.util.List;

public class Artist {
    private String name;
    //private String pic;
    private List<String> tracks;

    public Artist(String name, List<String> tracks){
        this.name = name;
        //this.pic = pic;
        this.tracks = tracks;
    }

    public String getName(){
        return this.name;
    }

//    public  String getPic(){
//        return this.pic;
//    }

    public int getTracks(){
        return tracks.size();
    }

    public void setName(String name){
        this.name = name;
    }

//    public void setPic(String pic){
//        this.pic = pic;
//    }

    public void setTracks(List<String> tracks){
        this.tracks = tracks;
    }
}
