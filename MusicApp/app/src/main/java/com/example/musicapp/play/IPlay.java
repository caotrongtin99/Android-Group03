package com.example.musicapp.play;

import com.example.musicapp.listsong.SongModel;

public interface IPlay {
    void controlSong(String sender, SongModel songModel, int action);
    void updateControlPlaying(String sender, SongModel songModel);
    void updateDuration(String sender, int progress);
    void updateProgressBar(String sender, int duration);
    void updateButtonPlay(String sender);
    void updatePlayingSongList();
    void updateToolbarTitle();
}
