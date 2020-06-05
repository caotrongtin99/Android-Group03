package com.example.musicapp.play;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.musicapp.MainActivity;
import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.SongModel;

import java.io.IOException;
import java.util.ArrayList;

public class PlayService implements IPlay, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static ArrayList<PlayModel> playingList;
    private static ArrayList<SongModel> playingSongList;
    private static SongModel currentSongPlaying;
    private static SongModel oldSongPlaying;
    private static int currentIndexSong;
    private static boolean isPause;

    private static MediaPlayer mediaPlayer = null;
    private static PlayService playService = null;
    private static DatabaseManager databaseManager = null;
    private CountDownTimer countDownTimerUpdateSeekBar = null;


    public static final int ACTION_PLAY = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_RESUME = 3;
    public static final int ACTION_NEXT = 4;
    public static final int ACTION_PREV = 5;

    public static final int NONE_LOOP = 1;
    public static final int ALL_LOOP = 2;
    public static final int ONE_LOOP = 3;

    public static final int ACTION_FROM_USER = 1;
    public static final int ACTION_FROM_SYSTEM = 2;


    private static int loopType = ALL_LOOP;
    public static boolean isShuffle;
    private static final String TAG = "PlayService";
    public static final String SENDER = "PLAY_CENTER";

    public static PlayService newInstance() {
        if (playService == null || mediaPlayer == null || databaseManager == null) {
            playService = new PlayService();
            mediaPlayer = new MediaPlayer();
            //using weak lock
//            mediaPlayer.setWakeMode(MainActivity.getMainActivity(), PowerManager.PARTIAL_WAKE_LOCK);
            databaseManager = DatabaseManager.newInstance(MainActivity.getMainActivity().getApplicationContext());
        }
        return playService;
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    public static int getLoopType() {
        return loopType;
    }

    public static void setLoopType(int loopType) {
        PlayService.loopType = loopType;
    }

    public void play(final SongModel songModel) {
        isPause = false;
        try {
            if (oldSongPlaying == null) {
                oldSongPlaying = songModel;
            }
            currentSongPlaying = songModel;

            setIndexSongInPlayingList();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songModel.getPath());
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);

            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (oldSongPlaying.getSongId() != currentSongPlaying.getSongId()) {
                    boolean resultUpdateStatus = PlayModel.updateStatusPlaying(oldSongPlaying.getSongId(), currentSongPlaying.getSongId());
                    Log.d(TAG, "initListPlaying: UPDATE STATUS" + resultUpdateStatus);
                }
                if (playingList == null || playingSongList == null) {
                    updatePlayingSongs();
                }
            }
        }).start();
    }

    public void pause() {
        mediaPlayer.pause();
        isPause = true;
    }

    public void resume() {
        isPause = false;
        if (currentSongPlaying != null && mediaPlayer != null) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
            mediaPlayer.start();
            Log.d(TAG, "resume: RESUME SONG " + mediaPlayer.getCurrentPosition());
        } else {
            Log.d(TAG, "resume: NOT RESUME SONG ");
        }

    }

    public void next(int actionFrom) {
        isPause = false;
        resetMediaPlayer();
        if (playingList == null) {
            return;
        }
        oldSongPlaying = currentSongPlaying;

        if (actionFrom == ACTION_FROM_USER) {
            setNextIndexSong();
        } else {
            if (loopType == ALL_LOOP) {
                setNextIndexSong();
            } else if (loopType == ONE_LOOP) {
                currentIndexSong = currentIndexSong;
            } else {
                mediaPlayer.seekTo(0);
                if (PlayActivity.getActivity() != null) {
                    PlayActivity.getActivity().updateControlPlaying(SENDER, currentSongPlaying);
                    PlayActivity.getActivity().updateButtonPlay(SENDER);
                    PlayActivity.getActivity().updateProgressBar(SENDER, mediaPlayer.getCurrentPosition());
                }
                pause();
                return;
            }
        }

        currentSongPlaying = playingSongList.get(currentIndexSong); //SongModel.getSongFromSongId(mDatabaseManager, mPlayingList.get(mCurrentIndexSong).getSongId());
        play(currentSongPlaying);
        if (MainActivity.getMainActivity() != null) {
            MainActivity.getMainActivity().togglePlayingMinimize(SENDER, ACTION_PLAY);
            MainActivity.getMainActivity().refreshNotificationPlaying(ACTION_PLAY);
        }

        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateControlPlaying(SENDER, currentSongPlaying);
        }
    }

    private void resetMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            mediaPlayer.stop();
            countDownTimerUpdateSeekBar.cancel();
        }
    }

    private void setNextIndexSong() {
        if (currentIndexSong == playingList.size() - 1) {
            currentIndexSong = 0;
        } else {
            currentIndexSong++;
        }
    }

    public void prev(int actionFrom) {
        isPause = false;
        resetMediaPlayer();
        if (playingList == null || PlayActivity.getActivity() == null) {
            return;
        }
        oldSongPlaying = currentSongPlaying;
        if (currentIndexSong == 0) {
            currentIndexSong = playingList.size() - 1;
        } else {
            currentIndexSong--;
        }
        currentSongPlaying = playingSongList.get(currentIndexSong);//SongModel.getSongFromSongId(mDatabaseManager, mPlayingList.get(mCurrentIndexSong).getSongId());
        play(currentSongPlaying);
        if (MainActivity.getMainActivity() != null) {
            MainActivity.getMainActivity().togglePlayingMinimize(SENDER, ACTION_PLAY);
            MainActivity.getMainActivity().refreshNotificationPlaying(ACTION_PLAY);

        }
        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateControlPlaying(SENDER, currentSongPlaying);
        }
    }

    public static long addSongToPlayingList(SongModel song) {

        if (song == null) {
            return -1;
        }
        boolean isExist = PlayModel.isSongExsist(song);
        if (isExist) {
            return 0;
        }
        long result = PlayModel.addSongToPlayingList(song);
        if (result > 0) {
            updatePlayingSongs();
        } else {
            return -1;
        }

        return result;
    }

    public static int createPlayingList(ArrayList<SongModel> songs) {
        Log.d(TAG, "createPlayingList: " + songs.size());
        PlayModel.clearPlayingList();
        PlayModel.createPlaylistFromSongs(songs);
        updatePlayingSongs();
        return 1;
    }

    public static int updatePlayingSongs() {
        playingList = PlayModel.getListPlaying();
        playingSongList = PlayModel.getSongPlayingList();

        setIndexSongInPlayingList();
        Log.d(TAG, "updatePlayingSongs: SIZE PLAYING LIST" + playingList.size());
        return playingList.size();
    }

    public static ArrayList<SongModel> getPlayingList() {
        return playingSongList;
    }

    public ArrayList<PlayModel> getPlayModelsList() {
        return playingList;
    }

    public static boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public static boolean isPause() {
        return isPause;
    }

    public static int getCurrentDuration() {
        return mediaPlayer.getCurrentPosition();
    }

    public static SongModel getCurrentSongPlaying() {
        return currentSongPlaying;
    }


    public static void revertListSongPlaying() {
        currentSongPlaying = PlayModel.getSongIsPlaying();
        updatePlayingSongs();
    }

    public void updateDuration(int progress) {

        mediaPlayer.seekTo(progress * 1000);
//        if (!mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//            mPlayActivity.updateControlPlaying(SENDER, mCurrentSongPlaying);
//        }
    }

    @Override
    public void controlSong(String sender, SongModel songModel, int action) {

    }

    @Override
    public void updateControlPlaying(String sender, SongModel songModel) {

    }

    @Override
    public void updateDuration(String sender, int progress) {

    }

    @Override
    public void updateProgressBar(String sender, int duration) {
        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateProgressBar(sender, duration);
        }

    }

    @Override
    public void updateButtonPlay(String sender) {

    }

    @Override
    public void updatePlayingList() {

    }

    @Override
    public void updateToolbarTitle() {

    }

    private static void setIndexSongInPlayingList() {
        currentIndexSong = -1;
        if (playingSongList != null) {
            for (int i = 0; i < playingSongList.size(); i++) {
                if (playingSongList.get(i).getSongId() == currentSongPlaying.getSongId()) {
                    currentIndexSong = i;
                }
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (!mediaPlayer.isPlaying()) {
            countDownTimerUpdateSeekBar = new CountDownTimer(currentSongPlaying.getDuration(), 1000) {
                public void onTick(long millisUntilFinished) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            Log.d(TAG, "onTick: " + millisUntilFinished + " " + currentSongPlaying.getTitle());
                            updateProgressBar(SENDER, mediaPlayer.getCurrentPosition());
                        }

                    } catch (IllegalStateException ex) {
                        ex.printStackTrace();
                    }


                }

                public void onFinish() {
                    Log.d(TAG, "onFinish: " + mediaPlayer.getCurrentPosition());
//                                        mediaPlayer.stop();

                }
            }.start();

        }
        mp.start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                long retSong = RecentModel.addToRecent(String.valueOf(currentSongPlaying.getSongId()), RecentModel.TYPE_SONG);
//                long retArtist = RecentModel.addToRecent(String.valueOf(currentSongPlaying.getArtist()), RecentModel.TYPE_ARTIST);
//                Log.d(TAG, "run: INSERT RECENT SONG " + retSong + "_ARTIST " + retArtist);
//            }
//        }).start();
        if (MainActivity.getMainActivity() != null) {
            MainActivity.getMainActivity().togglePlayingMinimize(SENDER, ACTION_PLAY);
            MainActivity.getMainActivity().refreshNotificationPlaying(ACTION_PLAY);
        }
        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateButtonPlay(SENDER);
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}