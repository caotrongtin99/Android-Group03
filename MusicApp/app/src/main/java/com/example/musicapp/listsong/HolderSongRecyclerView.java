package com.example.musicapp.listsong;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.ImageCacheHelper;
import com.example.musicapp.MainActivity;
import com.example.musicapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class HolderSongRecyclerView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ImageCacheHelper mImageCacheHelper = new ImageCacheHelper(R.mipmap.music);
    private MultiClickAdapterListener mListener;
    private TextView titleSong;
    private TextView artist;
    private TextView duration;
    private ImageView imageView;
    private ImageButton btnOptionSong;
    private CardView layoutItemSong;
    private ImageButton iconLike;
    private int albumId = -1;

    private ArrayList<SongModel> _listSong;


    public HolderSongRecyclerView(@NonNull View itemView, MultiClickAdapterListener listenerCustom) {
        super(itemView);
        mListener = listenerCustom;
        this.titleSong = itemView.findViewById(R.id.txtTitle);
        this.artist = itemView.findViewById(R.id.txtArtist);
        this.imageView = itemView.findViewById(R.id.imgSong);
        this.duration = itemView.findViewById(R.id.txtDuration);
        this.btnOptionSong = itemView.findViewById(R.id.btnOptionSong);
        this.iconLike = itemView.findViewById(R.id.icon_like);
        layoutItemSong = itemView.findViewById(R.id.layoutItemSong);
        //final SongModel songModel=_listSong.get();
        btnOptionSong.setOnClickListener(this);
        layoutItemSong.setOnClickListener(this);
        layoutItemSong.setOnLongClickListener(this);
        iconLike.setOnClickListener(this);
        /*iconLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(songModel);
                if(songModel.isFavorite()==0){
                    iconLike.setImageResource(R.drawable.ic_favorite_black_24dp);
                    _listSong.get(position).setFavorite(1);

                    String strSQL = "UPDATE songs SET is_fav = 1 WHERE song_id = "+ songModel.getSongId();
                    SQLiteDatabase db = MainActivity.mDatabaseManager.getReadableDatabase();
                    db.execSQL(strSQL);

                }else {
                    iconLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    _listSong.get(position).setFavorite(0);
                    String strSQL = "UPDATE songs SET is_fav = 0 WHERE song_id = "+ songModel.getSongId();
                    SQLiteDatabase db = MainActivity.mDatabaseManager.getReadableDatabase();
                    db.execSQL(strSQL);
                }
            }
        });
        */
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
        if(songModel.isFavorite()==1){
            iconLike.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else {
            iconLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOptionSong) {
            mListener.optionMenuClick(v, getAdapterPosition());
        }else if (v.getId()==R.id.icon_like){
            mListener.iconOnClick(v,getAdapterPosition());
        }
        else {
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
