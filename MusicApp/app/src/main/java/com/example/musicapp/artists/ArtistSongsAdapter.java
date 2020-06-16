package com.example.musicapp.artists;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.ImageCacheHelper;
import com.example.musicapp.R;
import com.example.musicapp.listsong.SongModel;

import java.util.List;

public class ArtistSongsAdapter extends RecyclerView.Adapter<ArtistSongsAdapter.ArtistSongViewHolder> {
    private List<SongModel> listData;
    private Context context;
    private int albumId = -1;
    public ArtistSongsAdapter(Context cont, List<SongModel> songs){
        context = cont;
        listData = songs;
    }

    @NonNull
    @Override
    public ArtistSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_artist_song, parent, false);
        ArtistSongViewHolder VH = new ArtistSongViewHolder(view);
        return VH;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistSongViewHolder holder, int position) {
        holder.BindData(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ArtistSongViewHolder extends RecyclerView.ViewHolder {
        TextView titleSong;
        TextView artist;
        TextView duration;
        ImageView imageView;
        private ImageCacheHelper mImageCacheHelper = new ImageCacheHelper(R.mipmap.music);

        public ArtistSongViewHolder(@NonNull  View v){
            super(v);
            titleSong = (TextView) v.findViewById(R.id.txtSongTitle);
            artist = (TextView) v.findViewById(R.id.txtArtist);
            duration = (TextView) v.findViewById(R.id.txtDuration);
            imageView = (ImageView) v.findViewById(R.id.imgSong);
        }
        public void BindData(int pos){
            SongModel song = listData.get(pos);
            String title = song.getTitle().length() > 35
                            ? song.getTitle().substring(0, 35) + "..."
                            : song.getTitle();

            titleSong.setText(title);
            artist.setText(song.getArtist());
            duration.setText(SongModel.formateMilliSeccond(song.getDuration()));
            final Bitmap bitmap = mImageCacheHelper.getBitmapCache(song.getAlbumId());
            if (bitmap != null && albumId == song.getAlbumId()) {
            imageView.setImageBitmap(bitmap);
            } else {
            mImageCacheHelper.loadAlbumArt(imageView, song);
            }
        }
    }
}
