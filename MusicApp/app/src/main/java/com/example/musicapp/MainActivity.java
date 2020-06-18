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
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import android.widget.SearchView;
import android.widget.Toast;


import androidx.annotation.Nullable;
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
import com.example.musicapp.playlist.FragmentPlaylist;
import com.google.android.material.tabs.TabLayout;


import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        adapter.addFragment(new com.example.musicapp.playlist.FragmentPlaylist(), "Playlists");
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

    }
    String txtVoice;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.head_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search_main:
                searchItem(item);

                return true;
            case R.id.action_micro_main:
                microItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    String getTxtVoice;
    private void searchItem(MenuItem menuItem){
        SearchView searchView = (SearchView) menuItem.getActionView();
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

        });

    }


    private void microItem(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, 1000);
        }
        catch (Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1000:
                if(resultCode == RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtVoice = result.get(0);
                    mSearchValue = result.get(0);
                    SearchByFragment(mCurrentFragmentActive);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        mSearchValue = "";
        SearchByFragment(mCurrentFragmentActive);
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
    public void playSongsFromFragmentListToMain(String Sender) {
        handleShowPlayActivityWithSongList();
    }

    @Override
    public void togglePlayingMinimize(String sender, int action) {

    }

    @Override
    public void refreshNotificationPlaying(int action) {

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
                FragmentPlaylist fragmentPlaylist = (FragmentPlaylist) ((TabAdapter) adapter).getFragmentAtIndex(fragmentIndex);
                if (fragmentPlaylist != null) {
                    fragmentPlaylist.UpdateSearch(mSearchValue);
                }
                break;
            default:
                break;
        }
    }

}