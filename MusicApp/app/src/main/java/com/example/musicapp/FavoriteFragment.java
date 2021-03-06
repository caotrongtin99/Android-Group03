package com.example.musicapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.callbacks.FragmentCallback;
import com.example.musicapp.listsong.FragmentListSong;
import com.example.musicapp.listsong.ListSongAdapter;
import com.example.musicapp.listsong.ListSongRecyclerAdaper;
import com.example.musicapp.listsong.MultiClickAdapterListener;
import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.play.PlayService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements FragmentCallback, MultiClickAdapterListener {
    private final String[] STATE = {"No track", "1 track", " tracks"};


    private MainActivity _mainActivity;
    private Context _context;
    private ArrayList<SongModel> _listSong;
    private RecyclerView _listViewSong;
    private TextView _txtSizeOfListSong;
    private int _sizeOfListSong;
    private ListSongRecyclerAdaper _listSongAdapter;
    //private SwipeRefreshLayout mSwpListSong;


    //    SkeletonScreen _skeletonScreen;
    private static final String TAG = "FRAGMENT_LIST_SONG";
    public static final String SENDER = "FRAGMENT_LIST_SONG";
    private static final int mThreshHold = 10;
    private static boolean mIsLoading;
    private static PlayService mPlayService;
    private MultiClickAdapterListener myAdapterListener;
    private static Thread mThreadInitListPlaying;
    static String searchValue = "";
    @Subscribe
    public void onEvent(SongModel songModel) {
        final ListView listView = (ListView) getActivity().findViewById(R.id.fav_list);
        String size;

        if (songModel.isFavorite()==0){
            _listSong.add(songModel);
            size = Integer.toString(_listSong.size());

        }
        else {
            int index = 0;
            for (int i =0 ; i < _listSong.size();i++){
                if (_listSong.get(i).getId()==songModel.getId()){
                    index = i;
                }
            }
            _listSong.remove(index);
            size = Integer.toString(_listSong.size());
        }

        _txtSizeOfListSong.setText(size + " favorite songs");
        listView.setAdapter(new ListSongAdapter(this.getContext(), _listSong));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            _context = getActivity();
                _mainActivity = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }

    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        mPlayService = PlayService.newInstance();

        return favoriteFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                _listSong = SongModel.getFavoriteSongs(MainActivity.mDatabaseManager);
                _sizeOfListSong = _listSong.size();
                switch (_sizeOfListSong) {
                    case 0:
                        _txtSizeOfListSong.setText(STATE[0]);
                        break;
                    case 1:
                        _txtSizeOfListSong.setText(STATE[1]);
                        break;
                    default:
                        _txtSizeOfListSong.setText(String.valueOf(_sizeOfListSong) + STATE[2]);
                        break;
                }
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
        Log.i(TAG, "onCreateView: STARTED CREATE VIEW");
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        _txtSizeOfListSong = view.findViewById(R.id.txtSizeOfListSong);

        _listSong = SongModel.getFavoriteSongs(MainActivity.mDatabaseManager);
        _sizeOfListSong = _listSong.size();
        switch (_sizeOfListSong) {
            case 0:
                _txtSizeOfListSong.setText(STATE[0]);
                break;
            case 1:
                _txtSizeOfListSong.setText(STATE[1]);
                break;
            default:
                _txtSizeOfListSong.setText(String.valueOf(_sizeOfListSong) + STATE[2]);
                break;
        }

        final ListView listView = (ListView) view.findViewById(R.id.fav_list);

        listView.setAdapter(new ListSongAdapter(this.getContext(), _listSong));
        return view;

    }

    @Override
    public void updateSizeOfListSong() {
        _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
    }

    @Override
    public void optionMenuClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        showBottomSheetOptionSong(songChose);
    }

    @Override
    public void removeItemClick(View v, int position) {

    }

    @Override
    public void iconOnClick(View v, int position) {

    }

    @Override
    public void layoutItemClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        //playSong(songChose);

    }

    @Override
    public void layoutItemLongClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        showBottomSheetOptionSong(songChose);
    }

    private void showBottomSheetOptionSong(SongModel songChose) {
        BottomSheetOptionSong bottomSheetDialogFragment = new BottomSheetOptionSong(songChose);
        bottomSheetDialogFragment.show(_mainActivity.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    private class loadImageFromStorage extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            _listSong.remove(_listSong.size() - 1);
            final int positionStart = _listSong.size() + 1;
            _listSong.addAll(songModels);
            Log.i(TAG, "onPostExecute: SONGS--> " + _listSong.size());
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
            Log.i(TAG, "onPostExecute: FINISHED");
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
        //_listSong.addAll(tempAudioList);
        //_listSongAdapter.notifyDataSetChanged();
        mIsLoading = false;
    }

}
