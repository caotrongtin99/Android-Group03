package com.example.musicapp.listsong;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.ImageCacheHelper;
import com.example.musicapp.R;

public class HolderSongRecyclerView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ImageCacheHelper mImageCacheHelper = new ImageCacheHelper(R.drawable.ic_queue_music_black_24dp);
    private MultiClickAdapterListener mListener;
    private TextView titleSong;
    private TextView artist;
    private TextView duration;
    private ImageView imageView;
    private ImageButton btnOptionSong;
    private CardView layoutItemSong;
    private int albumId = -1;


    public HolderSongRecyclerView(@NonNull View itemView, MultiClickAdapterListener listenerCustom) {
        super(itemView);
        mListener = listenerCustom;
        this.titleSong = itemView.findViewById(R.id.txtTitle);
        this.artist = itemView.findViewById(R.id.txtArtist);
        this.imageView = itemView.findViewById(R.id.imgSong);
        this.duration = itemView.findViewById(R.id.txtDuration);
        this.btnOptionSong = itemView.findViewById(R.id.btnOptionSong);
        layoutItemSong = itemView.findViewById(R.id.layoutItemSong);

        btnOptionSong.setOnClickListener(this);
        layoutItemSong.setOnClickListener(this);
        layoutItemSong.setOnLongClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    public void bindContent(SongModel songModel) {

        this.titleSong.setText(songModel.getTitle());
        this.artist.setText(songModel.getArtist());//+ "_" + songModel.getAlbumId()
        this.duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
        final Bitmap bitmap = mImageCacheHelper.getBitmapCache(songModel.getAlbumId());//  mBitmapCache.get((long) songModel.getAlbumId());
        if (bitmap != null && albumId == songModel.getAlbumId()) {
            this.imageView.setImageBitmap(bitmap);
        } else {
            mImageCacheHelper.loadAlbumArt(imageView, songModel);
        }
        albumId = songModel.getAlbumId();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOptionSong) {
            mListener.optionMenuClick(v, getAdapterPosition());
        } else {
            mListener.layoutItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
//        Log.d(TAG, "onLongClick: " + v.getId());
        mListener.layoutItemLongClick(v, getAdapterPosition());

        return true;
    }
}
