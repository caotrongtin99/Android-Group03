package com.example.musicapp.play;

import com.example.musicapp.listsong.SongModel;

public interface FragmentPlayInterface {
    //Fragment PlayingList
    void updatePlayingList();
    void refreshPlayingList();

    //Fragment Playing
    void updateControlPlaying(SongModel songModel);
    void updateProgressBar(int currentDuration);
    void updateButtonPlay();
}
