package com.example.musicapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
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
import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.FragmentListSong;
import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.playlist.FragmentPlaylist;
import com.google.android.material.tabs.TabLayout;


import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String artist = "com.example.musicapp.artist";
    public static DatabaseManager mDatabaseManager;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String mSearchValue = "";
    private static MainActivity mMainActivity;
    private int mCurrentFragmentActive;
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