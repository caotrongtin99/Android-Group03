package com.example.musicapp.play;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicapp.R;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentPlaying extends Fragment implements FragmentPlayInterface, View.OnClickListener {
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
    private ImageView imageBgPlaying;
    private ImageView imagePlaying;

    private PlayService playService;
    private static SongModel playingSong;

    public static final String SENDER = "FRAGMENT_PLAYING";
    private static final String TAG = "FragmentPlaying";
    private static final ArrayList<Integer> arrLoopTypeValue = new ArrayList<>(Arrays.asList(
            PlayService.NONE_LOOP,
            PlayService.ALL_LOOP,
            PlayService.ONE_LOOP));

    public FragmentPlaying() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (context == null) {
            context = getActivity();
            playActivity = (PlayActivity) getActivity();
        }
        playingSong = PlayService.getCurrentSongPlaying();
        playService = PlayService.newInstance();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroupMain = (ViewGroup) inflater.inflate(R.layout.fragment_playing, container, false);
        playActivity.updateToolbarTitle();
        return viewGroupMain;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageButtonPlaySong = viewGroupMain.findViewById(R.id.btnPlaySong);
        imageButtonPrevSong = viewGroupMain.findViewById(R.id.btnPrevSong);
        imageButtonNextSong = viewGroupMain.findViewById(R.id.btnNextSong);
        imagePlaying = viewGroupMain.findViewById(R.id.imgThumbnail);
//        imageBgPlaying = viewGroupMain.findViewById(R.id.imgBgThumbnail);
        txtTitleSongPlaying = viewGroupMain.findViewById(R.id.txtTitlePlaying);
        txtArtistSongPlaying = viewGroupMain.findViewById(R.id.txtSongArtistPlaying);
        txtDurationSongPlaying = viewGroupMain.findViewById(R.id.txtDurationTimeSong);
        sebDurationSongPlaying = viewGroupMain.findViewById(R.id.seekBarPlaying);
        txtCurrentDuration = viewGroupMain.findViewById(R.id.txtCurrentTimeSong);

        imageButtonPlaySong.setOnClickListener(this);
        imageButtonPrevSong.setOnClickListener(this);
        imageButtonNextSong.setOnClickListener(this);

        sebDurationSongPlaying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    playActivity.updateDuration(SENDER, progress);
                    if (!PlayService.isPlaying()) {
                        playActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                        setButtonPause();
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
        updateControlPlaying(playingSong);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSeekBar(PlayService.getCurrentDuration());
        updateControlPlaying(playingSong);
        updateButtonPlay();
    }

    @Override
    public void updatePlayingList() {

    }

    @Override
    public void refreshPlayingList() {

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {
        Log.d(TAG, "updateControlPlaying: " + songModel.getDuration());
        playingSong = songModel;
        txtTitleSongPlaying.setText(playingSong.getTitle());
        txtArtistSongPlaying.setText(playingSong.getArtist());
        txtDurationSongPlaying.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
        sebDurationSongPlaying.setMax(playingSong.getDuration().intValue() / 1000);
    }

    @Override
    public void updateSeekBar(int currentDuration) {
        sebDurationSongPlaying.setProgress(currentDuration / 1000);
        txtCurrentDuration.setText(SongModel.formateMilliSeccond(currentDuration));
    }

    @Override
    public void updateButtonPlay() {
        Log.d(TAG, "updateButtonPlay: " + PlayService.isPlaying());
        if (PlayService.isPlaying()) {
            setButtonPause();
        } else {
            setButtonPlay();
        }
    }

    private void setButtonPlay() {
        imageButtonPlaySong.setImageResource(R.drawable.ic_play_black_48dp);

    }

    private void setButtonPause() {
        imageButtonPlaySong.setImageResource(R.drawable.ic_pause_black_48dp);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlaySong:
                Toast.makeText(context, "PLAY CLICK", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: DURATION PLAY" + PlayService.getCurrentDuration());
                if (PlayService.isPlaying()) {// song is playing then stop
                    playActivity.controlSong(SENDER, null, PlayService.ACTION_PAUSE);
                    setButtonPlay();
                } else if (PlayService.isPause()) { //resume
                    playActivity.controlSong(SENDER, null, PlayService.ACTION_RESUME);
                    setButtonPause();
                } else {
                    playActivity.controlSong(SENDER, PlayService.getCurrentSongPlaying(), PlayService.ACTION_PLAY);
                    setButtonPause();
                }

                break;
            case R.id.btnPrevSong:
                playActivity.controlSong(SENDER, null, PlayService.ACTION_PREV);
                break;
            case R.id.btnNextSong:
                playActivity.controlSong(SENDER, null, PlayService.ACTION_NEXT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
