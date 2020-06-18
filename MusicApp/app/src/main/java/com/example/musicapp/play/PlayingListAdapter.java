package com.example.musicapp.play;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.listsong.MultiClickAdapterListener;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;

public class PlayingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static ArrayList<SongModel> listSong;
    private static Context context;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private static final String TAG = "PlayingListAdapter";

    private MultiClickAdapterListener multiClickAdapterListener;

    public PlayingListAdapter(Context context, ArrayList<SongModel> listSong, MultiClickAdapterListener multiClickAdapterListener) {
        this.context = context;
        this.listSong = listSong;
        this.multiClickAdapterListener = multiClickAdapterListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_song_playing, parent, false);
            return new ViewHolderRecycle(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_circle, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderRecycle) {
            showSongItem((ViewHolderRecycle) holder, position);
        }
    }



    private void showSongItem(ViewHolderRecycle viewHolder, int position) {
        SongModel songModel = listSong.get(position);
        viewHolder.bindContent(songModel);
    }

    @Override
    public int getItemCount() {
        return listSong == null ? 0 : listSong.size();
    }

    public int getItemViewType(int position) {
        return listSong.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) { return position; }

    private class ViewHolderRecycle extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleSong;
        TextView artist;
        TextView duration;
        ImageView thumbnail;
        ImageButton remove;
        ImageView imgStatusPlaying;
        CardView layoutItemSong;

        public ViewHolderRecycle(@NonNull View itemView) {
            super(itemView);
            this.titleSong = (TextView) itemView.findViewById(R.id.txtTitleItemPlayingList);
            this.artist = (TextView) itemView.findViewById(R.id.txtArtistItemPlayingList);
            this.duration = (TextView) itemView.findViewById(R.id.txtDurationItemPlayingList);
            this.imgStatusPlaying = (ImageView) itemView.findViewById(R.id.imgStatusItemPlayingList);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.imgItemPlayingList);
            this.remove = (ImageButton) itemView.findViewById(R.id.btnRemoveItemPlayingList);
            this.remove.setOnClickListener(this);
            layoutItemSong = itemView.findViewById(R.id.layoutItemPlayingList);
            layoutItemSong.setOnClickListener(this);
        }

        public void bindContent(SongModel songModel) {
            this.titleSong.setText(songModel.getTitle());
            this.artist.setText(songModel.getArtist());
            this.duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
            if (songModel.getBitmap() != null) this.thumbnail.setImageBitmap(songModel.getBitmap());
            if (songModel != null && PlayService.getCurrentSongPlaying() != null) {
                if (songModel.getSongId() == PlayService.getCurrentSongPlaying().getSongId()) {
                    this.imgStatusPlaying.setVisibility(View.VISIBLE);
                    this.duration.setVisibility(View.GONE);
                    this.titleSong.setTextColor(context.getResources().getColor(R.color.colorMain));
                } else {
                    this.titleSong.setTextColor(context.getResources().getColor(R.color.black));
                    this.imgStatusPlaying.setVisibility(View.GONE);
                    this.duration.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnRemoveItemPlayingList) {
                multiClickAdapterListener.removeItemClick(v, getAdapterPosition());
            } else {
                multiClickAdapterListener.layoutItemClick(v, getAdapterPosition());
            }
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
