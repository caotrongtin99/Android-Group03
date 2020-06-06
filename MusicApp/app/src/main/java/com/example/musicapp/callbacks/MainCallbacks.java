package com.example.musicapp.callbacks;

public interface MainCallbacks {
    void playSongsFromFragmentListToMain (String Sender);
    void togglePlayingMinimize(String sender,int action);
    void refreshNotificationPlaying(int action);
}
