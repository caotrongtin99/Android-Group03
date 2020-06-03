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

//    @Override
//    public int getCount() {
//        return listData.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return listData.get(position);
//    }

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

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ArtistSongViewHolder viewHolder;
//        TextView titleSong;
////        TextView album;
//        TextView duration;
//        TextView artist;
//        ImageView imageView;
//        LayoutInflater layout=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null) {
//
//            convertView = layout.inflate(R.layout.layout_item_song, parent, false);
//            titleSong = convertView.findViewById(R.id.txtTitle);
////            album=convertView.findViewById(R.id.txtAlbum);
//            duration=convertView.findViewById(R.id.txtDuration);
//            artist=convertView.findViewById(R.id.txtArtist);
//            imageView=convertView.findViewById(R.id.imgSong);
//            viewHolder = new ArtistSongViewHolder(titleSong,artist,duration,imageView);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ArtistSongsAdapter.ArtistSongViewHolder) convertView.getTag();
//            titleSong= viewHolder.titleSong ;
////            album= viewHolder.album;
//            artist=viewHolder.artist;
//            duration=viewHolder.duration;
//            imageView=viewHolder.imageView;
//        }
//        SongModel songModel=listData.get(position);
//        titleSong.setText(songModel.getTitle() );//+ "__" + songModel.getSongId()+"__"+songModel.getFolder()
////        album.setText(songModel.getAlbum());
//        artist.setText(songModel.getArtist());
//        duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
//        final Bitmap bitmap = mImageCacheHelper.getBitmapCache(songModel.getAlbumId());//  mBitmapCache.get((long) songModel.getAlbumId());
//        if (bitmap != null && albumId == songModel.getAlbumId()) {
//            imageView.setImageBitmap(bitmap);
//        } else {
//            mImageCacheHelper.loadAlbumArt(imageView, songModel);
//        }
//
//        return convertView;
//    }
    public int getResourceIdFromName(String resourceName) {

        String pkgName = context.getPackageName();
        int resoureId = context.getResources().getIdentifier(resourceName, "mipmap", pkgName);

        return resoureId;
    }
    public class ArtistSongViewHolder extends RecyclerView.ViewHolder {
        TextView titleSong;
        TextView album;
        TextView artist;
        TextView duration;
        ImageView imageView;
        private ImageCacheHelper mImageCacheHelper = new ImageCacheHelper(R.mipmap.music);
        TextView id;
        LayoutInflater layout;

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
