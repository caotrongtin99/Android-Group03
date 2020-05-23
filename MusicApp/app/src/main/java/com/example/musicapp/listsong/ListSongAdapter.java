package com.example.musicapp.listsong;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapp.R;
import java.util.ArrayList;

public class ListSongAdapter extends BaseAdapter {

    private ArrayList<SongModel> _listSong;
    private Context _context;

    public ListSongAdapter(Context context, ArrayList<SongModel> listSong) {
        this._context = context;
        this._listSong = listSong;
    }

    @Override
    public int getCount() {
        return _listSong.size();
    }

    @Override
    public Object getItem(int position) {
        return _listSong.get(position);
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
        LayoutInflater layout=(LayoutInflater)_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
            viewHolder = (ViewHolder) convertView.getTag();
            titleSong= viewHolder.titleSong ;
//            album= viewHolder.album;
            artist=viewHolder.artist;
            duration=viewHolder.duration;
            imageView=viewHolder.imageView;
        }
        SongModel songModel=_listSong.get(position);
        titleSong.setText(songModel.getTitle() );//+ "__" + songModel.getSongId()+"__"+songModel.getFolder()
//        album.setText(songModel.getAlbum());
        artist.setText(songModel.getArtist());
        duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
//        imageView.setImageBitmap(songModel.getBitmap());
        if(songModel.getBitmap() !=null){
            imageView.setImageBitmap(songModel.getBitmap());
        }else{
            imageView.setImageResource(getResourceIdFromName("musical_note_light_64"));
        }

        return convertView;
    }
    public int getResourceIdFromName(String resourceName) {

        String pkgName = _context.getPackageName();
        int resoureId = _context.getResources().getIdentifier(resourceName, "mipmap", pkgName);

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

