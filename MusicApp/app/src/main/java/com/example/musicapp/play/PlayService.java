package com.example.musicapp.play;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.musicapp.MainActivity;
import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.SongModel;

import java.io.IOException;
import java.util.ArrayList;

public class PlayService {
    private static ArrayList<PlayModel> mPlayingList;
    private static ArrayList<SongModel> mSongPlayingList;
    private static SongModel mCurrentSongPlaying;
    private static SongModel mOldSongPlaying;
    private static int mCurrentIndexSong;
    private static boolean mIsPause;

    private static MediaPlayer mMediaPlayer = null;
    private static PlayService mPlayService = null;
    private static DatabaseManager mDatabaseManager = null;
    private CountDownTimer mCountDownTimerUpdateSeekBar = null;


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
    public static boolean Shuffle;
    private static final String TAG = "PlayService";
    public static final String SENDER = "PLAY_CENTER";

    public static PlayService newInstance() {
        if (mPlayService == null || mMediaPlayer == null || mDatabaseManager == null) {
            mPlayService = new PlayService();
            mMediaPlayer = new MediaPlayer();
            //using weak lock
//            mMediaPlayer.setWakeMode(MainActivity.getMainActivity(), PowerManager.PARTIAL_WAKE_LOCK);
            mDatabaseManager = DatabaseManager.newInstance(MainActivity.getMainActivity().getApplicationContext());
        }
        return mPlayService;
    }
    /*
    public static MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public static int getLoopType() {
        return loopType;
    }

    public static void setLoopType(int loopType) {
        PlayService.loopType = loopType;
    }


    public void play(final SongModel songModel) {
//        Log.d(TAG, "play: "+songModel.getPath());
//        Log.d(TAG, "play: "+ Uri.parse(songModel.getPath()));
//        File path = Environment.getExternalStorageDirectory();
//        Log.d(TAG, "play: "+ path+songModel.getPath());
        mIsPause = false;
        try {
            if (mOldSongPlaying == null) {
                mOldSongPlaying = songModel;
            }
            mCurrentSongPlaying = songModel;

            setIndexSongInPlayingList();
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(songModel.getPath());
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);

            mMediaPlayer.prepareAsync();
//            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mOldSongPlaying.getSongId() != mCurrentSongPlaying.getSongId()) {
                    boolean resultUpdateStatus = PlayModel.updateStatusPlaying(mOldSongPlaying.getSongId(), mCurrentSongPlaying.getSongId());
                    Log.d(TAG, "initListPlaying: UPDATE STATUS" + resultUpdateStatus);
                }
                if (mPlayingList == null || mSongPlayingList == null) {
                    updatePlayingList();
                }
            }
        }).start();


    }

    public void pause() {
        mMediaPlayer.pause();
        mIsPause = true;
    }

    public void resurme() {
        mIsPause = false;
        if (mCurrentSongPlaying != null && mMediaPlayer != null) {
            mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition());
            mMediaPlayer.start();
            Log.d(TAG, "resurme: RESUME SONG " + mMediaPlayer.getCurrentPosition());
        } else {
            Log.d(TAG, "resurme: NOT RESUME SONG ");
        }

    }

    public void next(int actionFrom) {
        mIsPause = false;
        resetMediaPlayer();
        if (mPlayingList == null) {
            return;
        }
        mOldSongPlaying = mCurrentSongPlaying;

        if (actionFrom == ACTION_FROM_USER) {
            setNextIndexSong();
        } else {
            if (loopType == ALL_LOOP) {
                setNextIndexSong();
            } else if (loopType == ONE_LOOP) {
                mCurrentIndexSong = mCurrentIndexSong;
            } else {
                mMediaPlayer.seekTo(0);
                if (PlayActivity.getActivity() != null) {
                    PlayActivity.getActivity().updateControlPlaying(SENDER, mCurrentSongPlaying);
                    PlayActivity.getActivity().updateButtonPlay(SENDER);
                    PlayActivity.getActivity().updateSeekbar(SENDER, mMediaPlayer.getCurrentPosition());
                }
                pause();
                return;
            }
        }

        mCurrentSongPlaying = mSongPlayingList.get(mCurrentIndexSong); //SongModel.getSongFromSongId(mDatabaseManager, mPlayingList.get(mCurrentIndexSong).getSongId());
        play(mCurrentSongPlaying);
        if (MainActivity.getMainActivity() != null) {
            MainActivity.getMainActivity().togglePlayingMinimize(SENDER, ACTION_PLAY);
            MainActivity.getMainActivity().refreshNotificationPlaying(ACTION_PLAY);
        }

        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateControlPlaying(SENDER, mCurrentSongPlaying);
        }
    }

    private void resetMediaPlayer() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
            mMediaPlayer.stop();
            mCountDownTimerUpdateSeekBar.cancel();
        }
    }

    private void setNextIndexSong() {
        if (mCurrentIndexSong == mPlayingList.size() - 1) {
            mCurrentIndexSong = 0;
        } else {
            mCurrentIndexSong++;
        }
    }

    public void prev(int actionFrom) {
        mIsPause = false;
        resetMediaPlayer();
        if (mPlayingList == null || PlayActivity.getActivity() == null) {
            return;
        }
        mOldSongPlaying = mCurrentSongPlaying;
        if (mCurrentIndexSong == 0) {
            mCurrentIndexSong = mPlayingList.size() - 1;
        } else {
            mCurrentIndexSong--;
        }
        mCurrentSongPlaying = mSongPlayingList.get(mCurrentIndexSong);//SongModel.getSongFromSongId(mDatabaseManager, mPlayingList.get(mCurrentIndexSong).getSongId());
        play(mCurrentSongPlaying);
        if (MainActivity.getMainActivity() != null) {
            MainActivity.getMainActivity().togglePlayingMinimize(SENDER, ACTION_PLAY);
            MainActivity.getMainActivity().refreshNotificationPlaying(ACTION_PLAY);

        }
        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateControlPlaying(SENDER, mCurrentSongPlaying);
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
            updatePlayingList();
        } else {
            return -1;
        }

        return result;
    }

    public static int createPlayingList(ArrayList<SongModel> songs) {
        Log.d(TAG, "createPlayingList: " + songs.size());
        PlayModel.clearPlayingList();
        PlayModel.createPlaylistFromSongs(songs);
        updatePlayingList();
        return 1;
    }

    public static int updatePlayingList() {
        mPlayingList = PlayModel.getListPlaying();
        mSongPlayingList = PlayModel.getSongPlayingList();

        setIndexSongInPlayingList();
        Log.d(TAG, "updatePlayingList: SIZE PLAYING LIST" + mPlayingList.size());
        return mPlayingList.size();
    }


    public ArrayList<PlayModel> getPlayModelsList() {
        return mPlayingList;
    }

    public static boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public static boolean isPause() {
        return mIsPause;
    }

    public static int getCurrentDuration() {
        return mMediaPlayer.getCurrentPosition();
    }

    public static SongModel getCurrentSongPlaying() {
        return mCurrentSongPlaying;
    }


    public static void revertListSongPlaying() {
        mCurrentSongPlaying = PlayModel.getSongIsPlaying();
        updatePlayingList();
    }

    public void updateDuration(int progress) {

        mMediaPlayer.seekTo(progress * 1000);
//        if (!mMediaPlayer.isPlaying()) {
//            mMediaPlayer.start();
//            mPlayActivity.updateControlPlaying(SENDER, mCurrentSongPlaying);
//        }
    }

     */
    /*
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
    public void updateSeekbar(String sender, int duration) {
        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateSeekbar(sender, duration);
        }

    }

    @Override
    public void updateButtonPlay(String sender) {

    }

    @Override
    public void updateSongPlayingList() {

    }

    @Override
    public void updateToolbarTitle() {

    }

    private static void setIndexSongInPlayingList() {
        mCurrentIndexSong = -1;
        if (mSongPlayingList != null) {
            for (int i = 0; i < mSongPlayingList.size(); i++) {
                if (mSongPlayingList.get(i).getSongId() == mCurrentSongPlaying.getSongId()) {
                    mCurrentIndexSong = i;
                }
            }
        }


    }

     */
    /*
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (!mMediaPlayer.isPlaying()) {
            mCountDownTimerUpdateSeekBar = new CountDownTimer(mCurrentSongPlaying.getDuration(), 1000) {
                public void onTick(long millisUntilFinished) {
                    try {
                        if (mMediaPlayer.isPlaying()) {
                            Log.d(TAG, "onTick: " + millisUntilFinished + " " + mCurrentSongPlaying.getTitle());
                            updateSeekbar(SENDER, mMediaPlayer.getCurrentPosition());
                        }

                    } catch (IllegalStateException ex) {
                        ex.printStackTrace();
                    }


                }

                public void onFinish() {
                    Log.d(TAG, "onFinish: " + mMediaPlayer.getCurrentPosition());
//                                        mMediaPlayer.stop();

                }
            }.start();

        }
        mp.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long retSong = RecentModel.addToRecent(String.valueOf(mCurrentSongPlaying.getSongId()), RecentModel.TYPE_SONG);
                long retArtist = RecentModel.addToRecent(String.valueOf(mCurrentSongPlaying.getArtist()), RecentModel.TYPE_ARTIST);
                Log.d(TAG, "run: INSERT RECENT SONG " + retSong + "_ARTIST " + retArtist);
            }
        }).start();
        if (MainActivity.getMainActivity()!=null){
            MainActivity.getMainActivity().togglePlayingMinimize(SENDER, ACTION_PLAY);
            MainActivity.getMainActivity().refreshNotificationPlaying(ACTION_PLAY);
        }
        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateButtonPlay(SENDER);
        }

    }
    */
    /*
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion: NEXT -> ");
        mCountDownTimerUpdateSeekBar.cancel();
        next(ACTION_FROM_SYSTEM);

    }

     */

    /*
    public void initListPlaying(final ArrayList<SongModel> listPlaying) {
        PlayModel.clearPlayingList();
        PlayModel.createPlaylistFromSongs(listPlaying);
        updatePlayingList();
        if (PlayActivity.getActivity() != null) {
            PlayActivity.getActivity().updateSongPlayingList();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean resultUpdateStatus = PlayModel.updateStatusPlaying(mOldSongPlaying.getSongId(), mCurrentSongPlaying.getSongId());
                Log.d(TAG, "initListPlaying: UPDATE STATUS" + resultUpdateStatus);
            }
        }).start();
    }
    */

    public static ArrayList<SongModel> getListPlaying() {
        return mSongPlayingList;
    }

    public static SongModel getSongIsPlaying() {
        return PlayModel.getSongIsPlaying();
    }


//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        PlayService.newInstance();
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.getAction() == null) {
//            return START_STICKY;
//        }
//        int action = Integer.parseInt(Objects.requireNonNull(intent.getAction()));
//        Bundle bundle = intent.getExtras();
//        switch (action) {
//            case ACTION_PLAY:
//                Log.d(TAG, "onStartCommand: BUNDLE " + bundle);
//                if (bundle != null) {
//                    SongModel songPlay = (SongModel) bundle.getSerializable(SongModel.class.toString());
//                    play(songPlay);
//                }
//                break;
//            case ACTION_RESUME:
//                resurme();
//                break;
//            case ACTION_PAUSE:
//                pause();
//                break;
//            case ACTION_NEXT:
//                if (bundle != null) {
//                    int actionFrom = bundle.getInt("actionFrom");
//                    next(actionFrom);
//                }
//                break;
//            case ACTION_PREV:
//                if (bundle != null) {
//                    int actionFrom = bundle.getInt("actionFrom");
//                    prev(actionFrom);
//                }
//                break;
//            default:
//                break;
//        }
//
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mMediaPlayer != null) {
//            mMediaPlayer.release();
//        }
//        stopSelf();
//
//    }
}


