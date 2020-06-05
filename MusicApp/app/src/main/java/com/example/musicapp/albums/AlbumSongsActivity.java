package com.example.musicapp.albums;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicapp.ImageHelper;
import com.example.musicapp.MainActivity;
import com.example.musicapp.Utility;

import com.example.musicapp.listsong.RecyclerItemClickListener;
import com.example.musicapp.listsong.SongModel;

import com.example.musicapp.play.PlayActivity;
import com.example.musicapp.play.PlayService;
import com.example.musicapp.R;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumSongsActivity extends AppCompatActivity {
    ImageButton ImgBtnBack;
    ImageView ImgProfile;
    TextView TVNameArtist;
    TextView TVNameAlbum;
    TextView TVSongcount;
    AlbumModel albumModel;
//    CoordinatorLayout layoutContentAlbumtSong;
    Toolbar mToolbarAlbumSong;
    RecyclerView RVAlbumsong;
    PlayService mPlayService;
//    AppBarLayout mAppbarLayoutalbum;

//    private MinimizeSongFragment mMinimizeSongFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);
        Intent intent = getIntent();
        albumModel = (AlbumModel) intent.getSerializableExtra("infoAlbum");
        InitControl();
        BindData();
        setupLayoutTransparent();
        initMimimizeSong();
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
    }

    private void InitControl() {
        ImgProfile = (ImageView) findViewById(R.id.albumSongImgProfile);
        TVNameArtist = (TextView) findViewById(R.id.albumSongArtistName);
        TVNameAlbum = (TextView) findViewById(R.id.albumSongName);
        TVSongcount = (TextView) findViewById(R.id.albumSongcount);
//        layoutContentAlbumtSong = (CoordinatorLayout) findViewById(R.id.layoutContentAlbumSong);
        mToolbarAlbumSong = findViewById(R.id.albumhtab_toolbar);
        RVAlbumsong = (RecyclerView) findViewById(R.id.rcvalbumSong);
//        mAppbarLayoutalbum = (AppBarLayout) findViewById(R.id.albumhtab_appbar);
        mPlayService = PlayService.newInstance();
    }

    private void setupLayoutTransparent() {
        Utility.setTransparentStatusBar(AlbumSongsActivity.this);
        Bitmap bitmapBg = ImageHelper.getBitmapFromPath(albumModel.getPath(), R.drawable.gradient_bg);
        if (bitmapBg != null) {
            Bitmap bitmapBgBlur = ImageHelper.blurBitmap(bitmapBg, 1.0f, 30);
            Bitmap bitmapOverlay = ImageHelper.createImage(bitmapBgBlur.getWidth(), bitmapBgBlur.getHeight(), getResources().getColor(R.color.colorBlackGlassDark));
            Bitmap bitmapBgOverlay = ImageHelper.overlayBitmapToCenter(bitmapBgBlur, bitmapOverlay);

//            layoutContentAlbumtSong.setBackground(ImageHelper.getMainBackgroundDrawableFromBitmap(bitmapBgOverlay));
        }
//        layoutContentAlbumtSong.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);
/*
        setSupportActionBar(mToolbarAlbumSong);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/

    }

    private void initMimimizeSong() {
//        mMinimizeSongFragment = MinimizeSongFragment.newInstance();
//        getSupportFragmentManager().beginTransaction().add(R.id.frgMinimizeSong, mMinimizeSongFragment).commit();

    }

    private void BindData() {
        if (albumModel != null) {
            TVNameAlbum.setText(albumModel.getTitle());
            TVNameArtist.setText(albumModel.getArtist());
            TVSongcount.setText(albumModel.getNumberOfSongs() + " Bài hát");
            ImgProfile.setImageBitmap(ImageHelper.getBitmapFromPath(albumModel.getPath(), R.mipmap.microphone_128));
            final ArrayList<SongModel> albumSongsList = AlbumProvider.getALbumSongs(this, albumModel.getTitle());
            AlbumSongAdapter albumSongAdapter = new AlbumSongAdapter(this, albumSongsList);
            RVAlbumsong.setLayoutManager(new LinearLayoutManager(this));
            RVAlbumsong.setHasFixedSize(true);
            RVAlbumsong.setAdapter(albumSongAdapter);
            RVAlbumsong.addOnItemTouchListener(new RecyclerItemClickListener(this, RVAlbumsong, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    SongModel songModel = albumSongsList.get(position);
                    MainActivity _mainActivity = MainActivity.getMainActivity();
//                    mPlayService.play(songModel);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            mPlayService.initListPlaying(albumSongsList);
                        }
                    }).start();
//                    _mainActivity.playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
//            mAppbarLayoutalbum.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
//                @Override
//                public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                    if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
//                        getSupportActionBar().setTitle(albumModel.getTitle());
//
//                    } else {
//                        getSupportActionBar().setTitle(" ");
//                    }
//                }
//            });
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mMinimizeSongFragment.refreshControls(-1);
//    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void onFragmentRefreshNotification(int action) {
//        if (MainActivity.getMainActivity() != null) {
//            MainActivity.getMainActivity().refreshNotificationPlaying(action);
//        }
//    }

//    @Override
//    public void onFragmentShowPlayActivity() {
//        Intent mIntentPlayActivity = new Intent(AlbumSongsActivity.this, PlayActivity.class);
//        mIntentPlayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        startActivity(mIntentPlayActivity);
//    }
//
//    @Override
//    public void onFragmentLoaded(int heightLayout) {
//
//    }
}

