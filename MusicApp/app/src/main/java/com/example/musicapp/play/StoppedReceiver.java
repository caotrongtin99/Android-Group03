package com.example.musicapp.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.musicapp.listsong.SongModel;

public class StoppedReceiver extends BroadcastReceiver {
    private static final String TAG = "StoppedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            SongModel songPlay = (SongModel) bundle.getSerializable(SongModel.class.toString());
            Log.d(TAG, "onReceive: SONG PLAY RECEIVER " + songPlay);
            Intent playIntent = new Intent(context, PlayService.class);
            try {
                if (PlayService.isPlaying()) {
                    playIntent.setAction(String.valueOf(PlayService.ACTION_RESUME));
                } else {
                    playIntent.setAction(String.valueOf(PlayService.ACTION_PAUSE));
                }
                playIntent.putExtras(bundle);
                context.startService(playIntent);
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

        }

    }
}

