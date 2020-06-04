package com.example.musicapp.play;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.musicapp.R;
import com.example.musicapp.listsong.SongModel;
import com.example.musicforlife.MainActivity;
import com.example.musicforlife.YoutubeHelper;
import com.example.musicforlife.utilitys.ImageHelper;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class FragmentPlaying extends Fragment implements FragmentPlayInterface, View.OnClickListener {

    private TableRow mTableLayoutControlPlaying;
    private ViewGroup viewGroupMain;
    private ImageButton imageButtonPlaySong;
    private Context context;
    private PlayActivity playActivity;
    private TextView txtTitleSongPlaying;
    private TextView txtArtistSongPlaying;
    private TextView txtDurationSongPlaying;
    private SeekBar sebDurationSongPlaying;
    private TextView txtCurrentDuration;
    private ImageButton imageButtonPrevSong;
    private ImageButton imageButtonNextSong;
    private ImageButton imageButtonLoopType;
    private ImageView imagePlaying;
    private ImageView imageDvd;
    private ImageView imageBgPlaying;
    private CardView cvImagePlaying;
    private Animation animationPlay;
    private ImageButton btnToggleVideo;

    private boolean isShowVideo;
    private PlayService playService;
    private static SongModel playingSong;

    public static final String SENDER = "FRAGMENT_PLAYING";
    private static final String TAG = "FragmentPlaying";
    private static final ArrayList<Integer> arrLoopTypeValue = new ArrayList<>(Arrays.asList(
            PlayService.NONE_LOOP,
            PlayService.ALL_LOOP,
            PlayService.ONE_LOOP));
    private static final ArrayList<Integer> arrLoopTypeImage = new ArrayList<>(Arrays.asList(
            R.drawable.ic_repeat_none_black_32dp,
            R.drawable.ic_repeat_black_32dp,
            R.drawable.ic_repeat_one_black_32dp));

//    private static final String DEVELOPER_API_KEY = "AIzaSyACtzmdHydUC5STYTFynjzVmVtMvrGAhtI";
//    private static final String VIDEO_ID = "SgaQ0m3Sbqc";
    private static String mCurrentVideoId = "";

    public FragmentPlaying() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (context == null) {
            context = getActivity();
            playActivity = (PlayActivity) getActivity();

        }
//        Bundle bundle = getArguments();
//        mSongPlaying = (SongModel) bundle.getSerializable("PLAY_SONG");
        playingSong = PlayService.getCurrentSongPlaying();
        playService = PlayService.newInstance();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroupMain = (ViewGroup) inflater.inflate(R.layout.fragment_playing, container, false);


        mPlayActivity.updateToolbarTitle();

        return viewGroupMain;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTableLayoutControlPlaying = viewGroupMain.findViewById(R.id.layoutControlPlaying);
        mImageButtonPlaySong = viewGroupMain.findViewById(R.id.btnPlaySong);
        mImageButtonPrevSong = viewGroupMain.findViewById(R.id.btnPrevSong);
        mImageButtonNextSong = viewGroupMain.findViewById(R.id.btnNextSong);
        mImageButtonLoopType = viewGroupMain.findViewById(R.id.btnLoopType);
        mImagePlaying = viewGroupMain.findViewById(R.id.imgPlaying);
//        mImageDvd=viewGroupMain.findViewById(R.id.imgDvd);
        mCvImagePlaying = viewGroupMain.findViewById(R.id.cvImagePlaying);
        mImageBgPlaying = viewGroupMain.findViewById(R.id.imgBgPlaying);
        mBtnToggleVideo = viewGroupMain.findViewById(R.id.btnToggleVideo);
        mYoutubePlayer = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtubePlayer);
//        mCvYoutubePlayer = mCvYoutubePlayer.findViewById(R.id.cvYoutubePlayer);
        mTxtTitleSongPlaying = viewGroupMain.findViewById(R.id.txtTitleSongPlaying);
        mTxtArtistSongPlaying = viewGroupMain.findViewById(R.id.txtArtistSongPlaying);
        mTxtDurationSongPlaying = viewGroupMain.findViewById(R.id.txtDurationSongPlaying);
        mSebDurationSongPlaying = viewGroupMain.findViewById(R.id.sebDurationSongPlaying);
        mTxtCurrentDuration = viewGroupMain.findViewById(R.id.txtCurrentDuration);

        mAnimationPlay = AnimationUtils.loadAnimation(mContext, R.anim.playing_image);
        mImageButtonPlaySong.setOnClickListener(this);
        mImageButtonPrevSong.setOnClickListener(this);
        mImageButtonNextSong.setOnClickListener(this);
        mImageButtonLoopType.setOnClickListener(this);
        mBtnToggleVideo.setOnClickListener(this);
        mImageButtonLoopType.setImageDrawable(
                mPlayActivity.getDrawable(
                        arrLoopTypeImage.get(
                                arrLoopTypeValue.indexOf(PlayService.getLoopType()))));


        mSebDurationSongPlaying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayActivity.updateDuration(SENDER, progress);
                    if (!PlayService.isPlaying()) {
                        mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                        setButtonPause();
                        //mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//        if (mYoutubePlayer!=null){
//            mYoutubePlayer.initialize(DEVELOPER_API_KEY, this);
//        }
        isShowVideo = false;
        setResourceImagePlaying();
        updateControlPlaying(mSongPlaying);
        if (!isShowVideo && mYoutubePlayer.getView() != null) {
            mYoutubePlayer.getView().setVisibility(View.GONE);
        }
    }

    private void loadVideoLyric() {
        YoutubeHelper.getInstance(mPlayActivity).searchVideoLyric(mSongPlaying.getTitle(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + mSongPlaying.getTitle());
                try {
                    JSONArray items = response.getJSONArray("items");
                    Log.d(TAG, "onResponse: " + items.length());
                    JSONObject info = (JSONObject) items.get(0);
                    JSONObject infoId = info.getJSONObject("id");
                    mCurrentVideoId = infoId.get("videoId").toString();
//                    Log.d(TAG, "onResponse: " + );+
//                    mYoutubePlayer = null;
//                    mYoutubePlayer = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtubePlayer);
                    Log.d(TAG, "onCreateView: YOUTUBE " + mYoutubePlayer);
                    if (mYoutubePlayer != null) {
                        mYoutubePlayer.initialize(DEVELOPER_API_KEY, FragmentPlaying.this);
                        mYoutubePlayer.setRetainInstance(true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        });
    }

    private void setResourceImagePlaying() {
        if (mSongPlaying != null) {
            Bitmap bitmapPlaying = ImageHelper.getBitmapFromPath(mSongPlaying.getPath());
//        Bitmap bitmapBgPlaying = ImageHelper.getBitmapFromPath(mSongPlaying.getPath(), R.drawable.background_1);
            if (bitmapPlaying == null) {
                bitmapPlaying = ImageHelper.drawableToBitmap(R.mipmap.dvd_256);
                mImagePlaying.setScaleType(ImageView.ScaleType.CENTER);
            } else {
                mImagePlaying.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            mImagePlaying.setImageBitmap(bitmapPlaying);
            if (PlayService.isPlaying()) {
                mCvImagePlaying.startAnimation(mAnimationPlay);
            } else {
                mCvImagePlaying.clearAnimation();
            }

//            mImageDvd.startAnimation(animation);
        }


//        Bitmap bitmapBlurBgPlaying = ImageHelper.blurBitmap(bitmapBgPlaying, 1.0f, 100);
//        Bitmap bitmapOverlay = ImageHelper.createImage(bitmapBgPlaying.getWidth(), bitmapBgPlaying.getHeight(), Color.argb(100, 0, 0, 0));
//        mImageBgPlaying.setImageBitmap(bitmapBlurBgPlaying);
//        if (ImageHelper.isDarkBitmap(bitmapBgPlaying)) {
//            mTableLayoutControlPlaying.setBackgroundColor(Color.argb(40, 255, 255, 255));
//        } else {
//            mTableLayoutControlPlaying.setBackgroundColor(Color.TRANSPARENT);
//        }
//        Log.d(TAG, "setResourceImagePlaying: ISDARK" + ImageHelper.isDarkBitmap(bitmapPlaying));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
//
//        if (PlayService.isPlaying() && mSongPlaying.getSongId() != PlayService.getCurrentSongPlaying().getSongId()) {
//            Log.d(TAG, "onResume: SERVICE " + PlayService.getCurrentSongPlaying().getTitle() + " PLAY " + mSongPlaying.getTitle());
////            mSongPlaying=PlayService.getCurrentSongPlaying();
//            mPlayActivity.controlSong(SENDER, mSongPlaying, PlayService.ACTION_PLAY);
//            updateControlPlaying(mSongPlaying);
////            mPlayActivity.updateControlPlaying(SENDER, mSongPlaying);
//        }
//
//
//        Log.d(TAG, "onResume: " + PlayService.getCurrentDuration());
////        mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
//        if (mSongPlaying != null && PlayService.getCurrentSongPlaying() != null) {
//            if (mSongPlaying.getSongId() == PlayService.getCurrentSongPlaying().getSongId()) {
//                updateControlPlaying(mSongPlaying);
//            }
//        }
        updateSeekbar(PlayService.getCurrentDuration());
        updateControlPlaying(mSongPlaying);
        updateButtonPlay();
    }

    @Override
    public void updateListPlaying() {

    }

    @Override
    public void refreshListPlaying() {

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {
        Log.d(TAG, "updateControlPlaying: " + songModel.getDuration());
        mSongPlaying = songModel;
        mTxtTitleSongPlaying.setText(mSongPlaying.getTitle());
        mTxtArtistSongPlaying.setText(mSongPlaying.getArtist());
        mTxtDurationSongPlaying.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
        mSebDurationSongPlaying.setMax(mSongPlaying.getDuration().intValue() / 1000);
        setResourceImagePlaying();
        hideVideoLyric(0);

        //
    }

    @Override
    public void updateProgressBar(int currentDuration) {

    }

    @Override
    public void updateSeekbar(int currentDuration) {
        mSebDurationSongPlaying.setProgress(currentDuration / 1000);
        mTxtCurrentDuration.setText(SongModel.formateMilliSeccond(currentDuration));
    }

    @Override
    public void updatePlayingList() {

    }

    @Override
    public void refreshPlayingList() {

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {

    }

    @Override
    public void updateButtonPlay() {
        Log.d(TAG, "updateButtonPlay: " + PlayService.isPlaying());
        if (PlayService.isPlaying()) {
            setButtonPause();
            mCvImagePlaying.startAnimation(mAnimationPlay);

//            mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_black_70dp));
        } else {
            setButtonPlay();
            mCvImagePlaying.clearAnimation();


//            mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_arrow_black_70dp));
        }
    }

    private void setButtonPlay() {
        mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_play_circle_outline_black_64dp));

    }

    private void setButtonPause() {
        mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_circle_outline_black_64dp));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlaySong:
                Toast.makeText(mContext, "PLAY CLICK", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: DURATION PLAY" + PlayService.getCurrentDuration());
                if (PlayService.isPlaying()) {// song is playing then stop
                    mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_PAUSE);
                    mCvImagePlaying.clearAnimation();
                    setButtonPlay();

//                    MainActivity.getMainActivity().refreshNotificationPlaying(PlayService.ACTION_PAUSE);
//                    mPlayService.pause();
                } else if (PlayService.isPause()) { //resume
                    mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                    mCvImagePlaying.startAnimation(mAnimationPlay);
                    setButtonPause();
                    // hideVideoLyric();
//                    MainActivity.getMainActivity().refreshNotificationPlaying(PlayService.ACTION_RESUME);
//                    mPlayService.resurme();
//                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_circle_outline_black_64dp));
                } else {
                    mPlayActivity.controlSong(SENDER, PlayService.getCurrentSongPlaying(), PlayService.ACTION_PLAY);
                    mCvImagePlaying.startAnimation(mAnimationPlay);
                    setButtonPause();
                    //hideVideoLyric();
//                    MainActivity.getMainActivity().refreshNotificationPlaying(PlayService.ACTION_PLAY);
//                    mImageButtonPlaySong.setImageDrawable(mPlayActivity.getDrawable(R.drawable.ic_pause_circle_outline_black_64dp));
                }

                break;
            case R.id.btnPrevSong:
                mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_PREV);
                hideVideoLyric(0);
//                mPlayService.prev(PlayService.ACTION_FROM_USER);
                break;
            case R.id.btnNextSong:
//                mPlayService.next(PlayService.ACTION_FROM_USER);
                mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_NEXT);
                hideVideoLyric(0);
                break;
            case R.id.btnLoopType:
                int currentLoopType = PlayService.getLoopType();
                int indexLoopType = arrLoopTypeValue.indexOf(currentLoopType);
//                Log.d(TAG, "onClick: TYPE LOOP " + currentLoopType + "__" + indexLoopType);
                indexLoopType = indexLoopType >= arrLoopTypeValue.size() - 1 ? 0 : indexLoopType + 1;
                currentLoopType = arrLoopTypeValue.get(indexLoopType);
                PlayService.setLoopType(currentLoopType);
//                Log.d(TAG, "onClick: TYPE LOOP " + currentLoopType + "__" + indexLoopType);
                mImageButtonLoopType.setImageDrawable(
                        mPlayActivity.getDrawable(
                                arrLoopTypeImage.get(indexLoopType)));

                break;
            case R.id.btnToggleVideo:
                if (!isShowVideo) {

                    loadVideoLyric();
                    showVideoLyric();

                } else {

                    hideVideoLyric(R.id.btnToggleVideo);
                }

            default:
                break;
        }
    }

    private void hideVideoLyric(int fromAction) {
        isShowVideo = false;
        if (mYoutubePlayer.getView() != null) {
            Log.d(TAG, "hideVideoLyric: ");
            mYoutubePlayer.getView().setVisibility(View.GONE);
            mCvImagePlaying.setVisibility(View.VISIBLE);
            mBtnToggleVideo.setImageResource(R.drawable.ic_music_note_black_24dp);
            if (PlayService.isPause() && fromAction == R.id.btnToggleVideo) { //resume
                mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                setButtonPause();
            }
            if (PlayService.isPlaying()) {
                mCvImagePlaying.startAnimation(mAnimationPlay);
            }

            if (mCurrentYoutubePlayer != null) {
                mCurrentYoutubePlayer.pause();
            }


        }
    }

    private void showVideoLyric() {
        isShowVideo = true;
        if (mYoutubePlayer.getView() != null) {
            Log.d(TAG, "showVideoLyric: ");
            mYoutubePlayer.getView().setVisibility(View.VISIBLE);
            mCvImagePlaying.setVisibility(View.GONE);
            mCvImagePlaying.clearAnimation();
            mBtnToggleVideo.setImageResource(R.drawable.ic_video_label_black_24dp);

            mPlayActivity.controlSong(SENDER, null, PlayService.ACTION_PAUSE);
            setButtonPlay();

            if (mCurrentYoutubePlayer != null) {
                mCurrentYoutubePlayer.loadVideo(mCurrentVideoId);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (null == youTubePlayer) {
            return;
        }
        Log.d(TAG, "onInitializationSuccess: " + mCurrentVideoId);
        if (!b && !mCurrentVideoId.equals("")) {
            mCurrentYoutubePlayer = youTubePlayer;
            mCurrentYoutubePlayer.setFullscreen(false);
            mCurrentYoutubePlayer.setShowFullscreenButton(false);
            mCurrentYoutubePlayer.cueVideo(mCurrentVideoId);

            showVideoLyric();

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(mContext, "Không thể phát Video.", Toast.LENGTH_LONG).show();
    }
}
