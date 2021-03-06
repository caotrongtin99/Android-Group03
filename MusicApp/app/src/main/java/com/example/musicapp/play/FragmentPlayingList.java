package com.example.musicapp.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.listsong.MultiClickAdapterListener;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;

public class FragmentPlayingList extends Fragment implements FragmentPlayInterface, MultiClickAdapterListener {
    private PlayActivity playActivity;
    private Context context;
    private static ArrayList<SongModel> listSong;
    private RecyclerView listViewSong;
    private PlayingListAdapter playingListAdapter;
    private LoadListPlaying loadListPlaying;
    private static SongModel playingSong = null;

    public static final String SENDER = "FRAGMENT_PLAYING_LIST";
    private static final String TAG = "FragmentPlayingList";
    private MultiClickAdapterListener multiClickAdapterListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            playActivity = (PlayActivity) getActivity();
            loadListPlaying = new LoadListPlaying();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewSong = (RecyclerView) view.findViewById(R.id.lsvPlaying);
        listSong = new ArrayList<>();
        playingListAdapter = new PlayingListAdapter(context, listSong, FragmentPlayingList.this);
        listViewSong.setLayoutManager(new LinearLayoutManager(context));
        listViewSong.setAdapter(playingListAdapter);
        updatePlayingList();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("playingList", listSong);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadListPlaying.cancel(true);
    }

    @Override
    public void optionMenuClick(View v, int position) {

    }

    @Override
    public void removeItemClick(View v, int position) {
        String id = String.valueOf(listSong.get(position).getSongId());
        PlayModel.deleteSongsPlayingList(id);
        listSong.clear();
        listSong.addAll(PlayModel.getPlayingSongs());
        listViewSong.post(new Runnable() {
            @Override
            public void run() {
                playingListAdapter.notifyDataSetChanged();
            }
        });
        updateSongAfterDelete(id);
    }

    private void updateSongAfterDelete(String Ids){
        PlayService.updatePlayingSongs();
        if(listSong.size() == 0)
        {
            PlayService.newInstance().pause();
            playActivity.finish();
//            MainActivity.getMainActivity().isHideMinimize = true;
            return;
        }
        if(playingSong != null && Ids.contains(String.valueOf(playingSong.getSongId())))
        {
            playingSong = listSong.get(0);
            PlayService.newInstance().play(playingSong);
        }
    }

    @Override
    public void iconOnClick(View v, int position) {

    }

    @Override
    public void layoutItemClick(View v, int position) {
        playingSong = listSong.get(position);
        playActivity.controlSong(FragmentPlayingList.SENDER, playingSong, PlayService.ACTION_PLAY);
        playActivity.updateControlPlaying(SENDER, playingSong);
        playingListAdapter.notifyDataSetChanged();
    }

    @Override
    public void layoutItemLongClick(View v, int position) {

    }

    @Override
    public void updatePlayingList() {
        new LoadListPlaying().execute();
    }

    @Override
    public void refreshPlayingList() {

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {

    }

    @Override
    public void updateSeekBar(int currentDuration) {

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
            listSong.clear();
            listSong.addAll(songModels);

            Log.i(TAG, "onPostExecute: SONGS--> " + listSong.size());
            listViewSong.post(new Runnable() {
                @Override
                public void run() {
                    playingListAdapter.notifyDataSetChanged();
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
            return PlayService.getPlayingList();
        }
    }

    public static ArrayList<SongModel> getPlayingList() { return listSong; }

}
