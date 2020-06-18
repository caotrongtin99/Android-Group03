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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
    private CoordinatorLayout layoutPlay;
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
        layoutPlay = findViewById(R.id.layoutPlayActivity);
        viewPager = (ViewPager) findViewById(R.id.viewPagerPlay);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setElevation(0);
        layoutPlay.setPadding(0, 0, 0, 0);

        databaseManager = DatabaseManager.newInstance(getApplicationContext());
        playActivity = this;
        mainActivity = MainActivity.getMainActivity();
        playService = PlayService.newInstance();

        playingSong = PlayService.getCurrentSongPlaying();

        pagerAdapter = new FragmentPlayAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                updateToolbarTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabIndex", 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void controlSong(String sender, SongModel songModel, int action) {
        switch (action) {
            case PlayService.ACTION_PLAY:
                viewPager.setCurrentItem(1);
                playService.play(songModel);
                break;
            case PlayService.ACTION_PAUSE:
                playService.pause();
//                mainActivity.refreshNotificationPlaying(PlayService.ACTION_PAUSE);
                break;
            case PlayService.ACTION_RESUME:
                playService.resume();
//                mainActivity.refreshNotificationPlaying(PlayService.ACTION_RESUME);
                break;
            case PlayService.ACTION_PREV:
                playService.prev(PlayService.ACTION_FROM_USER);
//                mainActivity.refreshNotificationPlaying(PlayService.ACTION_PREV);
//                refreshListPlaying();
                break;
            case PlayService.ACTION_NEXT:
                playService.next(PlayService.ACTION_FROM_USER);
//                mainActivity.refreshNotificationPlaying(PlayService.ACTION_NEXT);
//                refreshListPlaying();
                break;
            default:
                break;
        }
    }

    public void refreshListPlaying() {
        FragmentPlayingList fragmentPlayingList = ((FragmentPlayAdapter) pagerAdapter).getFragmentListPlaying();
        if (fragmentPlayingList != null) {
            fragmentPlayingList.refreshPlayingList();
        }
    }

    @Override
    public void updateControlPlaying(String sender, SongModel songModel) {
        FragmentPlaying fragmentPlaying = ((FragmentPlayAdapter) pagerAdapter).getFragmentPlaying();
        if (fragmentPlaying != null) {
            fragmentPlaying.updateControlPlaying(songModel);
        }
    }

    @Override
    public void updateDuration(String sender, int progress) {
        playService.updateDuration(sender, progress);
    }

    @Override
    public void updateSeekBar(String sender, int duration) {
        FragmentPlaying fragmentPlaying = ((FragmentPlayAdapter) pagerAdapter).getFragmentPlaying();
        if (fragmentPlaying != null) {
            fragmentPlaying.updateSeekBar(duration);
        }
    }

    @Override
    public void updateButtonPlay(String sender) {
        FragmentPlaying fragmentPlaying = ((FragmentPlayAdapter) pagerAdapter).getFragmentPlaying();
        if (fragmentPlaying != null) {
            fragmentPlaying.updateButtonPlay();
        }
    }

    @Override
    public void updatePlayingList() {
        FragmentPlayingList fragmentPlayingList = ((FragmentPlayAdapter) pagerAdapter).getFragmentListPlaying();
        if (fragmentPlayingList != null) {
            fragmentPlayingList.updatePlayingList();
        }
    }

    @Override
    public void updateToolbarTitle() {
        int index = viewPager.getCurrentItem();
        if (index == 0) {
            getSupportActionBar().setTitle("Playing Queue");
            if (menu != null) {
//                menu.findItem(R.id.actionSetTimerSong).setVisible(false);
            }
        } else if (index == 1) {
            getSupportActionBar().setTitle("Now Playing");
            if (menu != null) {
//                mMenuPlay.findItem(R.id.actionSetTimerSong).setVisible(true);
            }
        }
    }

    public Context getApplicationContext() {
        return getBaseContext();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
