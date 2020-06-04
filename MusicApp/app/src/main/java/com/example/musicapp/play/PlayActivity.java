package com.example.musicapp.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.Utility;
import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements IPlay {

    private ViewPager viewPager;

    private DatabaseManager databaseManager;
    private LinearLayout linearLayout;
    private PagerAdapter pagerAdapter;
    private PlayService playService;
    private MainActivity mainActivity;
    private ArrayList<SongModel> playList;

    private static final String TAG = "PlayActivity";
    private static final String SENDER = "PLAY_ACTIVITY";
    private long times;
    private long currentTimes;

    private SongModel playingSong = null;
    private static PlayActivity playActivity;
    private Toolbar toolBar;
    private Menu menu;

    public static PlayActivity getActivity() { return playActivity; }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        toolBar = findViewById(R.id.tool_bar_play);
        viewPager = (ViewPager) findViewById(R.id.viewPagerPlay);

        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Utility.setTransparentStatusBar(PlayActivity.this);

        linearLayout.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseManager = DatabaseManager.newInstance(getApplicationContext());
        playActivity = this;
        mainActivity = MainActivity.getMainActivity();
        playService = PlayService.newInstance();

        playingSong = PlayService.getCurrentSongPlaying();

        pagerAdapter = new FragmentP
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

    }

    @Override
    public void updateButtonPlay(String sender) {

    }

    @Override
    public void updatePlayingSongList() {

    }

    @Override
    public void updateToolbarTitle() {

    }
}
