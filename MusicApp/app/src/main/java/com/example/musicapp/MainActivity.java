package com.example.musicapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.artists.ArtistFragment;
import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.FragmentListSong;
import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.play.PlayService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static DatabaseManager mDatabaseManager;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static MainActivity mMainActivity;

    public static MainActivity getMainActivity() {
        return mMainActivity;
    }

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);



        mMainActivity = MainActivity.this;


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new FavoriteFragment(), "Favorites");
        adapter.addFragment(new FragmentListSong(), "Tracks");
        adapter.addFragment(new AlbumFragment(), "Albums");
        adapter.addFragment(new ArtistFragment(), "Artists");
        adapter.addFragment(new AlbumFragment(), "Playlists");
        viewPager.setAdapter(adapter);

        mDatabaseManager = DatabaseManager.newInstance(getApplicationContext());
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.head_menu, menu);
        return true;
    }



    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }

    @Override
    public void togglePlayingMinimize(String sender, int action) {
        Log.d(TAG, "togglePlayingMinimize: RUNNING SERVICE=" + PlayService.isPlaying());
        if (mMinimizeSongFragment != null) {
            if (PlayService.isPlaying() || action == PlayService.ACTION_PLAY) {
                mMinimizeSongFragment.refreshControls(PlayService.ACTION_PLAY);
            } else {
                mMinimizeSongFragment.refreshControls(-1);
            }

        }

    }

    @Override
    public void refreshNotificationPlaying(int action) {
        SongModel songPlaying = PlayService.getCurrentSongPlaying();
        if (songPlaying != null) {
            Log.d(TAG, "initNotificationPlay: " + songPlaying.getTitle());
        }
        if (songPlaying == null) {
            return;
        }
        createNotificationChanel();
        //create layout notification
        mNotificationlayoutPlaying = new RemoteViews(getPackageName(), R.layout.layout_notificatoin_play);

//        //set content notification

        mNotificationlayoutPlaying.setImageViewBitmap(R.id.imgSongMinimize, ImageHelper.getBitmapFromPath(songPlaying.getPath(), R.mipmap.music_128));
        mNotificationlayoutPlaying.setTextViewText(R.id.txtTitleMinimize, songPlaying.getTitle());
        mNotificationlayoutPlaying.setTextViewText(R.id.txtArtistMinimize, songPlaying.getArtist());
        if (action != PlayService.ACTION_PAUSE) {
            mNotificationlayoutPlaying.setImageViewResource(R.id.btnPlaySong, R.drawable.ic_pause_circle_outline_black_32dp);
        } else {
            mNotificationlayoutPlaying.setImageViewResource(R.id.btnPlaySong, R.drawable.ic_play_circle_outline_black_32dp);
        }
        //\set content notification

        //playback activity
        Intent mainIntent = new Intent(this, MainActivity.class);//mới change
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PlayActivity.class);
        stackBuilder.addNextIntent(mainIntent);


        //intent back to activity
        PendingIntent pendingIntentPlay = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.notificationLayout, pendingIntentPlay);
        //\intent back to activity

        //intent play song
        Intent playButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        playButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_PLAY_NOTIFY);
        playButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent playButtonPending = PendingIntent.getBroadcast(this, 0, playButtonIntent, 0);

        Intent nextButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        nextButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_NEXT_NOTIFY);
        nextButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent nextButtonPending = PendingIntent.getBroadcast(this, 0, nextButtonIntent, 0);

        Intent prevButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
        prevButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_PREV_NOTIFY);
        prevButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent prevButtonPending = PendingIntent.getBroadcast(this, 0, prevButtonIntent, 0);

        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnPlaySong, playButtonPending);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnNextSong, nextButtonPending);
        mNotificationlayoutPlaying.setOnClickPendingIntent(R.id.btnPrevSong, prevButtonPending);

        //\intent play song
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PLAY_CHANEL_ID.toString())
                .setSmallIcon(R.drawable.ic_album_black_24dp)
                .setDefaults(0)
                .setContentIntent(pendingIntentPlay)//mới change
                .setContentIntent(playButtonPending)
                .setContentIntent(nextButtonPending)
                .setContentIntent(prevButtonPending)
                .setCustomContentView(mNotificationlayoutPlaying);//mới change


        if (action != PlayService.ACTION_PAUSE) {
            builder.setOngoing(true);
        } else {
            builder.setOngoing(false);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(PLAY_NOTIFICATION_ID, builder.build());
        refreshMinimizePlaying(action);
    }

}