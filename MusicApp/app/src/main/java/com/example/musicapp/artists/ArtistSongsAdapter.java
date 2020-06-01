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

import com.example.musicapp.ImageCacheHelper;
import com.example.musicapp.R;
import com.example.musicapp.listsong.SongModel;

import java.util.List;

public class ArtistSongsAdapter extends BaseAdapter {
    private List<SongModel> listData;
    private Context context;
    private int albumId = -1;
    private ImageCacheHelper mImageCacheHelper = new ImageCacheHelper(R.mipmap.music);
    public ArtistSongsAdapter(Context cont, List<SongModel> songs){
        context = cont;
        listData = songs;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TextView titleSong;
//        TextView album;
        TextView duration;
        TextView artist;
        ImageView imageView;
        LayoutInflater layout=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = layout.inflate(R.layout.layout_item_song, parent, false);
            titleSong = convertView.findViewById(R.id.txtTitle);
//            album=convertView.findViewById(R.id.txtAlbum);
            duration=convertView.findViewById(R.id.txtDuration);
            artist=convertView.findViewById(R.id.txtArtist);
            imageView=convertView.findViewById(R.id.imgSong);
            viewHolder = new ViewHolder(titleSong,artist,duration,imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ArtistSongsAdapter.ViewHolder) convertView.getTag();
            titleSong= viewHolder.titleSong ;
//            album= viewHolder.album;
            artist=viewHolder.artist;
            duration=viewHolder.duration;
            imageView=viewHolder.imageView;
        }
        SongModel songModel=listData.get(position);
        titleSong.setText(songModel.getTitle() );//+ "__" + songModel.getSongId()+"__"+songModel.getFolder()
//        album.setText(songModel.getAlbum());
        artist.setText(songModel.getArtist());
        duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
        final Bitmap bitmap = mImageCacheHelper.getBitmapCache(songModel.getAlbumId());//  mBitmapCache.get((long) songModel.getAlbumId());
        if (bitmap != null && albumId == songModel.getAlbumId()) {
            imageView.setImageBitmap(bitmap);
        } else {
            mImageCacheHelper.loadAlbumArt(imageView, songModel);
        }

        return convertView;
    }
    public int getResourceIdFromName(String resourceName) {

        String pkgName = context.getPackageName();
        int resoureId = context.getResources().getIdentifier(resourceName, "mipmap", pkgName);

        return resoureId;
    }
    private class ViewHolder {
        TextView titleSong;
        TextView album;
        TextView artist;
        TextView duration;
        ImageView imageView;
        TextView id;
        public  ViewHolder(TextView titleSong,TextView artist,TextView duration,ImageView imageView){
            this.titleSong=titleSong;
//            this.album=album;
            this.artist=artist;
            this.imageView=imageView;
            this.duration=duration;
        }
    }
}
