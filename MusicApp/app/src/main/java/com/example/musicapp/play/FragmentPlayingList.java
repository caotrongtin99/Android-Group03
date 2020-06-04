package com.example.musicapp.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.listsong.MultiClickAdapterListener;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;

public class FragmentPlayingList extends Fragment implements FragmentPlayInterface, MultiClickAdapterListener {

    private PlayActivity playActivity;
    private Context context;
    private LayoutInflater inflater;
    private static ArrayList<SongModel> listSong;
    private LinearLayout linearLayout;
    private RecyclerView listViewSong;
    private PlayingListAdapter playingListAdapter;
    private LoadListPlaying loadListPlaying;
    private TextView sizePlayingList;
    private LinearLayout layoutDeleteSong;
    private Button btnDeleteAllSongPlaying;
    private AppCompatCheckBox selectedAllPlayingSong;
    private static SongModel playingSong = null;

    public static final String SENDER = "FRAGMENT_PLAYING_LIST";
    private static final String TAG = "FragmentPlayingList";
    private MultiClickAdapterListener multiClickAdapterListener;
    private ArrayList<Integer> listSelectedSong;
    private ArrayList<Integer> listIdSelectedSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            playActivity = (PlayActivity) getActivity();
            loadListPlaying = new LoadListPlaying();
            listSelectedSong = new ArrayList<>();
            listIdSelectedSong = new ArrayList<>();
        } catch (IllegalStateException e) {}
    }

    public static FragmentPlayingList newInstance() {
        FragmentPlayingList fragmentPlayingList = new FragmentPlayingList();
        playingSong = PlayService.getCurrentSongPlaying();
        return fragmentPlayingList;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: STARTED");
        updatePlayingList();
    }

    @Nullable
    @Override()
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playing_list, container, false);
        playActivity.updateToolbarTitle();
        return view;
    }

    @Override
    public void optionMenuClick(View v, int position) {

    }

    @Override
    public void checkboxClick(View v, int position) {

    }

    @Override
    public void layoutItemClick(View v, int position) {

    }

    @Override
    public void layoutItemLongClick(View v, int position) {

    }

    @Override
    public void updatePlayingList() {

    }

    @Override
    public void refreshPlayingList() {

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {

    }

    @Override
    public void updateProgressBar(int currentDuration) {

    }

    @Override
    public void updateButtonPlay() {

    }

    private class LoadListPlaying extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            if (songModels == null) {
                return;
            }
            mListSong.clear();
            mListSong.addAll(songModels);

            mListIdSelectedSong.clear();
            mListSelectedSong.clear();
            for (int i = 0 ; i < songModels.size() ; i++){
                if(songModels.get(i).isChecked()){
                    mListIdSelectedSong.add(songModels.get(i).getSongId());
                    mListSelectedSong.add(i);
                }
            }
            updateViewSelectAll();

            Log.i(TAG, "onPostExecute: SONGS--> " + mListSong.size());
            mListViewSong.post(new Runnable() {
                @Override
                public void run() {
                    mListSongAdapter.notifyDataSetChanged();
                }
            });
            Log.i(TAG, "onPostExecute: FINISHED");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public ArrayList<SongModel> doInBackground(Void... voids) {

            return PlayService.getListPlaying();
        }
    }
}
