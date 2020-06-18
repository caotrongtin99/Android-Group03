package com.example.musicapp.playlist;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.BottomSheetOptionSongPlaylist;
import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.listsong.MultiClickAdapterListener;
import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.play.PlayService;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Objects;

public class PlaylistSongActivity extends AppCompatActivity implements MultiClickAdapterListener, SongPlaylistInterface {

    private RecyclerView mRecylerViewListSong;
    private SongPlaylistAdapter mSongPlaylistAdapter;
    private ArrayList<SongModel> mListSong;
    private CoordinatorLayout mLayoutSongPlaylist;
    private Toolbar mToolbar;
    private int mCurrentPlaylistId;
    private PlaylistModel mCurrentPlaylist;
    private TextView mTxtTitlePlaylist;
    private AppBarLayout mAppbarLayoutPlaylist;
    private ImageView mImageCoverPlaylist;
    private static PlayService mPlayService;
    private static final String TAG = "PlaylistSongActivity";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_song);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCurrentPlaylistId = bundle.getInt("playlistId");
        }

        initFindViewId();
        initRecyclerViewListSong();
        initToolBarParalax();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initFindViewId() {
        mRecylerViewListSong = findViewById(R.id.rcvListSongPlaylist);
        mLayoutSongPlaylist = findViewById(R.id.layoutSongPlaylist);
        mTxtTitlePlaylist = findViewById(R.id.txtTitlePlaylist);
        mAppbarLayoutPlaylist = findViewById(R.id.htab_appbar);

        mPlayService = PlayService.newInstance();

        mTxtTitlePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtTitlePlaylist.setCursorVisible(true);
                mTxtTitlePlaylist.setFocusableInTouchMode(true);
                mTxtTitlePlaylist.setInputType(InputType.TYPE_CLASS_TEXT);
                mTxtTitlePlaylist.requestFocus(); //to trigger the soft input
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getSupportActionBar().setTitle(mCurrentPlaylist.getTitle());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initToolBarParalax() {
        mCurrentPlaylist = PlaylistModel.getPlaylistById(mCurrentPlaylistId);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mTxtTitlePlaylist.setText(mCurrentPlaylist.getTitle());
    }

    private void setSupportActionBar(Toolbar mToolbar) {
    }

    private void initRecyclerViewListSong() {

        mListSong = PlaylistSongModel.getAllSongFromPlaylistId(mCurrentPlaylistId);
        mSongPlaylistAdapter = new SongPlaylistAdapter(this, mListSong, this);
        mRecylerViewListSong.setLayoutManager(new LinearLayoutManager(this));
        mRecylerViewListSong.setHasFixedSize(true);
        mRecylerViewListSong.setAdapter(mSongPlaylistAdapter);

    }

    @Override
    public void refreshSongPlaylist() {
                ArrayList<SongModel> songsPlaylist = PlaylistSongModel.getAllSongFromPlaylistId(mCurrentPlaylistId);
                mListSong.clear();
                mSongPlaylistAdapter.notifyDataSetChanged();
                mListSong.addAll(songsPlaylist);
                mSongPlaylistAdapter.notifyDataSetChanged();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                          mSongPlaylistAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void refreshTitlePlaylist(String titlePlaylist) {
        mCurrentPlaylist.setTitle(titlePlaylist);
        mTxtTitlePlaylist.setText(mCurrentPlaylist.getTitle());
    }

    private void showBottomSheetOptionSong(SongModel song) {
        BottomSheetOptionSongPlaylist bottomSheetDialogFragment = new BottomSheetOptionSongPlaylist(song, mCurrentPlaylist, this);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    private void playSong(SongModel songPlay) {
        mPlayService.play(songPlay);

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        MainActivity.getMainActivity().playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
    }


    @Override
    public void optionMenuClick(View v, int position) {
        final SongModel songChose = mListSong.get(position);
        showBottomSheetOptionSong(songChose);
    }
//
    @Override
    public void checkboxClick(View v, int position) {

    }

    @Override
    public void iconOnClick(View v, int position) {

    }

    //
    @Override
    public void layoutItemClick(View v, int position) {
        final SongModel songChose = mListSong.get(position);
        playSong(songChose);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                RecentModel.addToRecent(String.valueOf(mCurrentPlaylistId), RecentModel.TYPE_PLAYLIST);
            }
        }).start();
    }
//
    @Override
    public void layoutItemLongClick(View v, int position) {
        final SongModel songChose = mListSong.get(position);
        showBottomSheetOptionSong(songChose);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_playlist_song, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_playlist:
                new AlertDialog.Builder(this, R.style.DialogPrimary)
                        .setMessage("Bạn có chắc muốn xóa playlist " + mCurrentPlaylist.getTitle() + " ?")
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long result = PlaylistModel.deletePlaylist(mCurrentPlaylistId);
                                if (result > 0) {
                                    Toast.makeText(PlaylistSongActivity.this, "Xóa thành công", Toast.LENGTH_LONG).show();
                                    FragmentPlaylist.refreshPlaylist();
                                } else {
                                    Toast.makeText(PlaylistSongActivity.this, "Thất bại", Toast.LENGTH_LONG).show();
                                }
                                finish();
                            }

                        })
                        .setNegativeButton("Đóng", null)
                        .show();
                break;

            case R.id.action_edit_title_playlist:
                DialogFragment dialogEditPlaylist = new FragmentDialogEditPlaylist(mCurrentPlaylist, this);
//                Dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialogEditPlaylist.show(getSupportFragmentManager(), "EditPlaylist");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
