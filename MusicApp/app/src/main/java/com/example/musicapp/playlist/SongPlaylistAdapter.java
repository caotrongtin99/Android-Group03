package com.example.musicapp.playlist;

import android.content.Context;


import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.listsong.MultiClickAdapterListener;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;

public class SongPlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<SongModel> mListSong;
    private static Context mContext;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private static final String TAG = "ListPlayingAdapter";
    public MultiClickAdapterListener mListener;


    public SongPlaylistAdapter(Context context, ArrayList<SongModel> listSong, MultiClickAdapterListener listener) {
        this.mContext = context;
        this.mListSong = listSong;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_song_playlist, viewGroup, false);
            return new ViewHolderRecycler(view, mListener);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_circle, viewGroup, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolderRecycler) {
            showSongItem((ViewHolderRecycler) viewHolder, i);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoading((LoadingViewHolder) viewHolder, i);
        }
    }

    private void showSongItem(ViewHolderRecycler viewHolder, int position) {
        SongModel songModel = mListSong.get(position);
        viewHolder.bindContent(songModel);
    }

    private void showLoading(LoadingViewHolder viewHolder, int position) {

    }



    @Override
    public int getItemViewType(int position) {
        return mListSong.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mListSong == null ? 0 : mListSong.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolderRecycler extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView titleSong;
        TextView album;
        TextView artist;
        TextView duration;
        TextView txtRowCount;
        ImageButton btnOptionSong;

        CardView layoutItemSong;

        public ViewHolderRecycler(@NonNull View itemView, MultiClickAdapterListener listenerCustom) {
            super(itemView);
            titleSong = (TextView) itemView.findViewById(R.id.txtTitle);
            artist = (TextView) itemView.findViewById(R.id.txtArtist);
            duration = (TextView) itemView.findViewById(R.id.txtDuration);
            txtRowCount = itemView.findViewById(R.id.txtRowCount);
            btnOptionSong = itemView.findViewById(R.id.btnOptionSong);
            layoutItemSong = itemView.findViewById(R.id.layoutItemSong);
            btnOptionSong.setOnClickListener(this);
            layoutItemSong.setOnClickListener(this);
            layoutItemSong.setOnLongClickListener(this);
        }

        public void bindContent(SongModel songModel) {
            this.titleSong.setText(songModel.getTitle());
            this.artist.setText(songModel.getArtist());
            this.duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
            this.txtRowCount.setText(String.valueOf(getAdapterPosition() + 1));
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnOptionSong) {
                Log.d(TAG, "onClick: CLICK OPTION MENU");
                mListener.optionMenuClick(v, getAdapterPosition());
            } else {
                Log.d(TAG, "onClick: ITEM SONG");
                mListener.layoutItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.layoutItemLongClick(v, getAdapterPosition());
            return true;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarCircle);
        }
    }


}
