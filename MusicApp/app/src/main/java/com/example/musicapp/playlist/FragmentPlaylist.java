package com.example.musicapp.playlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;

import com.example.musicapp.listsong.RecyclerItemClickListener;
import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.play.PlayActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentPlaylist extends Fragment {
    private static final String TAG = "FRAGMENT_PLAY_LIST";
    public static final String SENDER = "FRAGMENT_PLAY_LIST";
    private static Context mContext;
    private MainActivity mMainActivity;
    private static RecyclerView mRecyclerViewPlaylist;
    private static PlaylistAdapter mPlaylistAdapter;
    private FloatingActionButton mButtonCreatePlaylist;
    private SwipeRefreshLayout mSwpPlaylist;

    private final int mThreshold = 10;
    private static boolean mIsLoading;
    private static ArrayList<PlaylistModel> mPlaylist;

    static String searchValue = "";


    public FragmentPlaylist() {

    }

    public static FragmentPlaylist newInstance() {
        FragmentPlaylist fragmentPlaylist = new FragmentPlaylist();
        return fragmentPlaylist;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mContext = getActivity();
            mMainActivity = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_playlist, container, false);
        mRecyclerViewPlaylist = viewGroup.findViewById(R.id.rcvPlaylist);
        mButtonCreatePlaylist = viewGroup.findViewById(R.id.btnCreatePlaylist);
        mSwpPlaylist = viewGroup.findViewById(R.id.swpPlaylist);
        mPlaylist = PlaylistModel.getAllPlaylist(searchValue);
        mContext = getActivity();
        //mPlaylistAdapter = new PlaylistAdapter(mContext, mPlaylist);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mPlaylistAdapter = new PlaylistAdapter(mContext, mPlaylist);
                mRecyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(mContext));
                mRecyclerViewPlaylist.setAdapter(mPlaylistAdapter);
            }
        }).start();
        mRecyclerViewPlaylist.addOnItemTouchListener(new RecyclerItemClickListener(mContext, mRecyclerViewPlaylist, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showPlaylistSongActivity(mPlaylist.get(position).getId());
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        mButtonCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogCreatePlaylist = new FragmentDialogCreatePlaylist();
                dialogCreatePlaylist.show(mMainActivity.getSupportFragmentManager(), "CreatePlaylist");
            }
        });

        mSwpPlaylist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPlaylist.clear();
                mPlaylistAdapter.notifyDataSetChanged();
                ArrayList<PlaylistModel> temp = PlaylistModel.getAllPlaylist(searchValue);
                Log.d(TAG, "onRefresh: "+temp.size());
                if (temp.size()>0){
                    Log.d(TAG, "onRefresh: "+temp.get(temp.size()-1).getNumberOfSongs());
                }
                mPlaylist.addAll(temp);
                mPlaylistAdapter.notifyDataSetChanged();
                mSwpPlaylist.setRefreshing(false);
            }
        });
        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void showPlaylistSongActivity(int playlistId) {
        Intent intent = new Intent(MainActivity.getMainActivity(), PlaylistSongActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putInt("playlistId", playlistId);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public static void refreshPlaylist() {
                //ArrayList<PlaylistModel> a = new ArrayList<>();
                //mPlaylist = a;
                //mPlaylistAdapter = new PlaylistAdapter(mContext, mPlaylist);
                mPlaylist.clear();
                mPlaylistAdapter.notifyDataSetChanged();
                ArrayList<PlaylistModel> playlistModels = PlaylistModel.getAllPlaylist(searchValue);
                if (playlistModels.size() > 0) {
                    Log.d(TAG, "run: REFRESH PLAYLIST SIZE " + playlistModels.get(playlistModels.size() - 1).getNumberOfSongs());
                }
                mPlaylist.addAll(playlistModels);
                mPlaylistAdapter.notifyDataSetChanged();


    }


    public void UpdateSearch(String s){
        if(s == searchValue) return;
        searchValue = s;
        mIsLoading = true;
        ArrayList<PlaylistModel> tempPlayList = PlaylistModel.getAllPlaylist(searchValue);
        mPlaylist.clear();
        mPlaylist.addAll(tempPlayList);
        mPlaylistAdapter.notifyDataSetChanged();
        mIsLoading = false;
    }
}
