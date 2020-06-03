package com.example.musicapp.listsong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.musicapp.BottomSheetOptionSong;
import com.example.musicapp.BottomSheetShowSongInfo;
import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.play.PlayService;

import com.example.musicapp.callbacks.FragmentCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class FragmentListSong extends Fragment implements FragmentCallback, MultiClickAdapterListener {
    private MainActivity _mainActivity;
    private Context _context;
    private ArrayList<SongModel> _listSong;
    private RecyclerView _listViewSong;
    private TextView _txtSizeOfListSong;
    private ListSongRecyclerAdaper _listSongAdapter;
    private ListView listView;
    private SwipeRefreshLayout mSwpListSong;



    public static final String SENDER = "FRAGMENT_LIST_SONG";
    private static final int mThreshHold = 10;
    private static boolean mIsLoading;
    private static PlayService mPlayService;
    private MultiClickAdapterListener myAdapterListener;
    private static Thread mThreadInitListPlaying;
    static String searchValue = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            _context = getActivity();
            _mainActivity = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static FragmentListSong newInstance() {
        FragmentListSong fragmentListSong = new FragmentListSong();
        mPlayService = PlayService.newInstance();

        return fragmentListSong;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_song, container, false);
        _txtSizeOfListSong = view.findViewById(R.id.txtSizeOfListSong);
        _listSong = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, 0, 20);
        _listViewSong = view.findViewById(R.id.lsvSongs);
        mSwpListSong = view.findViewById(R.id.swpListSong);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                _listSong = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, 0, 20);
                _listSongAdapter = new ListSongRecyclerAdaper(_context, _listSong, FragmentListSong.this);
                _listViewSong.setLayoutManager(new LinearLayoutManager(_context));
                _listViewSong.setAdapter(_listSongAdapter);
                _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
            }
        });

        return view;



    }

    @Override
    public void updateSizeOfListSong() {
        _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
    }

    /*
    private void loadMore() {
//        _skeletonScreen = Skeleton.bind(_listViewSong).adapter(_listSongAdapter).load(R.layout.layout_item_song).show();
//        _listSong.add(null);
//        _listSongAdapter.notifyItemInserted(_listSong.size());
//        Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, _listSong.size(), mThreshHold);
//                _listSong.remove(_listSong.size() - 1);
//                _listSongAdapter.notifyItemRemoved(_listSong.size());
                _listSong.addAll(tempAudioList);
                _listSongAdapter.notifyDataSetChanged();
//                _listSongAdapter.notifyItemInserted(_listSong.size());
//                _listSongAdapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        }, 1000);

    }


    private void playSong(SongModel songPlay) {
        mPlayService.play(songPlay);

        if (mThreadInitListPlaying != null && mThreadInitListPlaying.isAlive()) {
            mThreadInitListPlaying.interrupt();
        }

        mThreadInitListPlaying = new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayService.initListPlaying(SongModel.getAllSongs(DatabaseManager.getInstance()));
            }
        });
        mThreadInitListPlaying.start();
        _mainActivity.playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
    }
    */
    private void showBottomSheetOptionSong(SongModel song) {

        BottomSheetOptionSong bottomSheetDialogFragment = new BottomSheetOptionSong(song);
        bottomSheetDialogFragment.show(_mainActivity.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }


    @Override
    public void optionMenuClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        showBottomSheetOptionSong(songChose);
    }

    @Override
    public void checkboxClick(View v, int position) {

    }

    @Override
    public void iconOnClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        EventBus.getDefault().post(songChose);
        ImageButton iconLike = v.findViewById(R.id.icon_like);
        if(songChose.isFavorite()==0){

            iconLike.setImageResource(R.drawable.ic_favorite_black_24dp);
            _listSong.get(position).setFavorite(1);

            String strSQL = "UPDATE songs SET is_fav = 1 WHERE song_id = "+ songChose.getSongId();
            SQLiteDatabase db = MainActivity.mDatabaseManager.getReadableDatabase();
            db.execSQL(strSQL);

        }else {
            iconLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            _listSong.get(position).setFavorite(0);
            String strSQL = "UPDATE songs SET is_fav = 0 WHERE song_id = "+ songChose.getSongId();
            SQLiteDatabase db = MainActivity.mDatabaseManager.getReadableDatabase();
            db.execSQL(strSQL);
        }
    }

    @Override
    public void layoutItemClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        //playSong(songChose);

    }

    @Override
    public void layoutItemLongClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        showDetailSong(songChose);
    }

    public void showDetailSong(SongModel songChose) {
        BottomSheetShowSongInfo bottomSheetDialogFragment = new BottomSheetShowSongInfo(songChose);
        bottomSheetDialogFragment.show(_mainActivity.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    private class loadImageFromStorage extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            _listSong.remove(_listSong.size() - 1);
            final int positionStart = _listSong.size() + 1;
            _listSong.addAll(songModels);

            _listViewSong.post(new Runnable() {
                @Override
                public void run() {
//                    _listSongAdapter.notifyDataSetChanged();
                    _listSongAdapter.notifyItemRangeChanged(positionStart, _listSong.size());
                    _listSongAdapter.notifyItemRemoved(positionStart);
                    _listSongAdapter.notifyItemChanged(positionStart);
                    _listSongAdapter.notifyItemInserted(positionStart);
                }
            });


            mIsLoading = false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public ArrayList<SongModel> doInBackground(Void... voids) {
            _listSong.add(null);

            _listSongAdapter.notifyItemInserted(_listSong.size());
            ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, _listSong.size(), mThreshHold);

            return tempAudioList;
        }


    }

    public void UpdateSearch(String s){

        if(s == searchValue) return;
        searchValue = s;
        mIsLoading = true;
        ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, 0, mThreshHold);
        _listSong.clear();
        //_listSongAdapter.notifyDataSetChanged();
        _listSong.addAll(tempAudioList);
        _listSongAdapter.notifyDataSetChanged();
       // _listSong = tempAudioList;
        mIsLoading = false;
    }
}
