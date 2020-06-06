package com.example.musicapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import android.widget.SearchView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;


import com.example.musicapp.artists.Artist;
import com.example.musicapp.artists.ArtistAdapter;

import com.example.musicapp.albums.AlbumFragment;

import com.example.musicapp.artists.ArtistFragment;
import com.example.musicapp.artists.ArtistSongsActivity;
import com.example.musicapp.artists.ArtistSongsAdapter;
import com.example.musicapp.callbacks.MainCallbacks;
import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.FragmentListSong;
import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.play.PlayActivity;
import com.example.musicapp.play.PlayService;
import com.google.android.material.tabs.TabLayout;


import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainCallbacks {
    public final static String artist = "com.example.musicapp.artist";
    private static final String TAG = "MainActivity";
    public static DatabaseManager mDatabaseManager;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String mSearchValue = "";
    private static MainActivity mMainActivity;
    private Intent mIntentPlayActivity = null;
    private int mCurrentFragmentActive;

    public static final Integer PLAY_CHANEL_ID = 103;
    public static final Integer PLAY_NOTIFICATION_ID = 103;

    private static RemoteViews notificationLayoutPlaying;

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
        adapter.addFragment(new com.example.musicapp.albums.AlbumFragment(), "Albums");
        adapter.addFragment(new com.example.musicapp.artists.ArtistFragment(), "Artists");
        adapter.addFragment(new com.example.musicapp.albums.AlbumFragment(), "Playlists");
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
                mCurrentFragmentActive = position;
                SearchByFragment(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        ListView listView = (ListView) findViewById(R.id.artistList);
//        ArrayList<Artist> artists = Artist.getArtists(mDatabaseManager);
//        ArrayAdapter<String> cheeseAdapter =
//                new ArrayAdapter<String>(this,
//                        R.layout.fragment_artists,
//                        R.id.artistList,
//                        artists.
//                );
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv = (TextView) view.findViewById(R.id.txt_ArtistName);
//                String artist = tv.getText().toString();
//                Log.e("ABC", artist);
//                Intent intent = new Intent(MainActivity.getMainActivity(), ArtistSongsActivity.class);
//                intent.putExtra("artist", artist);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.head_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_main).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchValue = s;
                SearchByFragment(mCurrentFragmentActive);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mSearchValue = s;
                SearchByFragment(mCurrentFragmentActive);
                return false;
            }

            String a = mSearchValue;
            Integer b = 1;
        });

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


//    public void switchArtist(View view) {
//        Intent intent = new Intent(this, ArtistSongsActivity.class);
//        ListView list = (ListView) findViewById(R.id.artistList);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv = (TextView) view.findViewById(R.id.txt_ArtistName);
//                String artist = tv.getText().toString();
//                Log.e("ABC", artist);
//                Intent intent = new Intent(MainActivity.getMainActivity(), ArtistSongsActivity.class);
//                intent.putExtra("artist", artist);
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    public void playSongsFromFragmentListToMain(String Sender) {
//        Log.d(TAG, "playSongsFromFragmentListToMain: " + "SONG " + songPlay.getTitle() + " LIST " + songList.size());
//        refreshNotificationPlaying(PlayService.ACTION_PLAY);
        handleShowPlayActivityWithSongList();
    }

    @Override
    public void togglePlayingMinimize(String sender, int action) {

    }

    @Override
    public void refreshNotificationPlaying(int action) {
//        SongModel songPlaying = PlayService.getCurrentSongPlaying();
//        if (songPlaying != null) {
//            Log.d(TAG, "initNotificationPlay: " + songPlaying.getTitle());
//        }
//        if (songPlaying == null) {
//            return;
//        }
//        createNotificationChanel();
//        //create layout notification
//        notificationLayoutPlaying = new RemoteViews(getPackageName(), R.layout.layout_notificatoin_play);
//
//        //set content notification
//
//        notificationLayoutPlaying.setImageViewBitmap(R.id.imgSongMinimize, ImageHelper.getBitmapFromPath(songPlaying.getPath(), R.mipmap.music_128));
//        notificationLayoutPlaying.setTextViewText(R.id.txtTitleMinimize, songPlaying.getTitle());
//        notificationLayoutPlaying.setTextViewText(R.id.txtArtistMinimize, songPlaying.getArtist());
//        if (action != PlayService.ACTION_PAUSE) {
//            notificationLayoutPlaying.setImageViewResource(R.id.btnPlaySong, R.drawable.ic_pause_circle_outline_black_32dp);
//        } else {
//            notificationLayoutPlaying.setImageViewResource(R.id.btnPlaySong, R.drawable.ic_play_circle_outline_black_32dp);
//        }
//        //\set content notification
//
//        //playback activity
//        Intent mainIntent = new Intent(this, MainActivity.class);//mới change
//        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(PlayActivity.class);
//        stackBuilder.addNextIntent(mainIntent);
//
//
//        //intent back to activity
//        PendingIntent pendingIntentPlay = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        notificationLayoutPlaying.setOnClickPendingIntent(R.id.notificationLayout, pendingIntentPlay);
//        //\intent back to activity
//
//        //intent play song
//        Intent playButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
//        playButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_PLAY_NOTIFY);
//        playButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent playButtonPending = PendingIntent.getBroadcast(this, 0, playButtonIntent, 0);
//
//        Intent nextButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
//        nextButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_NEXT_NOTIFY);
//        nextButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent nextButtonPending = PendingIntent.getBroadcast(this, 0, nextButtonIntent, 0);
//
//        Intent prevButtonIntent = new Intent(this, NotifyBroadcastReceiver.class);
//        prevButtonIntent.setAction(NotifyBroadcastReceiver.ACTION_PREV_NOTIFY);
//        prevButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent prevButtonPending = PendingIntent.getBroadcast(this, 0, prevButtonIntent, 0);
//
//        notificationLayoutPlaying.setOnClickPendingIntent(R.id.btnPlaySong, playButtonPending);
//        notificationLayoutPlaying.setOnClickPendingIntent(R.id.btnNextSong, nextButtonPending);
//        notificationLayoutPlaying.setOnClickPendingIntent(R.id.btnPrevSong, prevButtonPending);
//
//        //\intent play song
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PLAY_CHANEL_ID.toString())
//                .setSmallIcon(R.drawable.ic_album_black_24dp)
//                .setDefaults(0)
//                .setContentIntent(pendingIntentPlay)//mới change
//                .setContentIntent(playButtonPending)
//                .setContentIntent(nextButtonPending)
//                .setContentIntent(prevButtonPending)
//                .setCustomContentView(notificationLayoutPlaying);//mới change
//
//
//        if (action != PlayService.ACTION_PAUSE) {
//            builder.setOngoing(true);
//        } else {
//            builder.setOngoing(false);
//        }
//
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(PLAY_NOTIFICATION_ID, builder.build());
//        refreshMinimizePlaying(action);
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence chanelName = PLAY_CHANEL_ID.toString();
            String description = "TEST NOTIFICATION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel chanel = new NotificationChannel(PLAY_CHANEL_ID.toString(), chanelName, importance);
            chanel.setDescription(description);
            chanel.setSound(null, null);
            chanel.setVibrationPattern(new long[]{0});
            chanel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(chanel);

//            notificationManager.notify(PLAY_NOTIFICATION_ID);
        }
    }

    private void handleShowPlayActivityWithSongList() {
        if (mIntentPlayActivity == null) {
            mIntentPlayActivity = new Intent(MainActivity.this, PlayActivity.class);
            mIntentPlayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        startActivity(mIntentPlayActivity);
    }

    public void SearchByFragment(int fragmentIndex) {
        switch (fragmentIndex) {
            case 0:
                FavoriteFragment fragmentFavorites = (FavoriteFragment) ((TabAdapter) adapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentFavorites != null) {
                    fragmentFavorites.UpdateSearch(mSearchValue);
                }
                break;
            case 1:
                FragmentListSong fragmentListSong = (FragmentListSong) ((TabAdapter) adapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentListSong != null) {
                    fragmentListSong.UpdateSearch(mSearchValue);
                }
                break;
            case 2:
                AlbumFragment fragmentAlbums = (AlbumFragment) ((TabAdapter) adapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentAlbums != null) {
                    fragmentAlbums.UpdateSearch(mSearchValue);
                }
                break;
            case 3:
                ArtistFragment fragmentArtist = (ArtistFragment) ((TabAdapter) adapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentArtist != null) {
                    fragmentArtist.UpdateSearch(mSearchValue);
                }
                break;
            case 4:
                //fragmentFolder = (FragmentFolder) ((PagerMainAdapter) mPagerAdapter).getFragmentAtIndex(fragmentIndex);
                //if (fragmentFolder != null) {
                //    fragmentFolder.UpdateSearch(mSearchValue);
                //}
                break;
            default:
                break;
        }
    }

}