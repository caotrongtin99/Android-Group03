package com.example.musicapp.listsong;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.ImageCacheHelper;
import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
public class ListSongAdapter extends BaseAdapter {
    private ImageCacheHelper mImageCacheHelper = new ImageCacheHelper(R.mipmap.music);
    private ArrayList<SongModel> _listSong;
    private LayoutInflater layoutInflater;
    private MainActivity _mainActivity;
    private int albumId = -1;
    private Context _context;

    public ListSongAdapter(Context context, ArrayList<SongModel> listSong) {
        this._context = context;
        this._listSong = listSong;
        this.layoutInflater = LayoutInflater.from(context);
        _mainActivity = MainActivity.getMainActivity();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TextView titleSong;
//        TextView album;
        TextView duration;
        TextView artist;
        ImageView imageView;
        final ImageButton iconLike;
        LayoutInflater layout=(LayoutInflater)_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = layout.inflate(R.layout.layout_item_song, parent, false);
            titleSong = convertView.findViewById(R.id.txtTitle);
//            album=convertView.findViewById(R.id.txtAlbum);
            duration=convertView.findViewById(R.id.txtDuration);
            artist=convertView.findViewById(R.id.txtArtist);
            imageView=convertView.findViewById(R.id.imgSong);
            iconLike=convertView.findViewById(R.id.icon_like);
            viewHolder = new ViewHolder(titleSong,artist,duration,imageView, iconLike);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            titleSong= viewHolder.titleSong ;
//            album= viewHolder.album;
            artist=viewHolder.artist;
            duration=viewHolder.duration;
            imageView=viewHolder.imageView;
            iconLike=viewHolder.iconLike;
        }
        final SongModel songModel=_listSong.get(position);
        titleSong.setText(songModel.getTitle() );
        artist.setText(songModel.getArtist());
        duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
        if(songModel.getBitmap() !=null){
            imageView.setImageBitmap(songModel.getBitmap());
        }else{
            imageView.setImageResource(getResourceIdFromName("musical_note_light_64"));
        }

        final Bitmap bitmap = mImageCacheHelper.getBitmapCache(songModel.getAlbumId());//  mBitmapCache.get((long) songModel.getAlbumId());
        if (bitmap != null && albumId == songModel.getAlbumId()) {
            imageView.setImageBitmap(bitmap);
        } else {
            mImageCacheHelper.loadAlbumArt(imageView, songModel);
        }

        iconLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(songModel);
                if(songModel.isFavorite()==0){
                    iconLike.setImageResource(R.drawable.ic_favorite_black_24dp);
                    _listSong.get(position).setFavorite(1);
                    Toast.makeText(_context, "You liked " + songModel.getTitle(), Toast.LENGTH_SHORT).show();
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
        ImageButton iconLike;
        TextView id;
        public  ViewHolder(TextView titleSong,TextView artist,TextView duration,ImageView imageView, ImageButton iconLike){
            this.titleSong=titleSong;
//            this.album=album;
            this.artist=artist;
            this.imageView=imageView;
            this.duration=duration;
            this.iconLike=iconLike;
        }
    }
}

