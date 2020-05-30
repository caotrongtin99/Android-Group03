//package com.example.musicapp.artists;
//
//import android.content.Context;
//import android.widget.BaseAdapter;
//
//import com.example.musicapp.listsong.SongModel;
//
//import java.util.List;
//
//public class ArtistSongsAdapter extends BaseAdapter {
//    private List<SongModel> listData;
//    private Context context;
//
//    public ArtistSongsAdapter(Context cont, List<SongModel> songs){
//        context = cont;
//        listData = songs;
//    }
//
//    @Override
//    public int getCount() {
//        return listData.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return listData.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//}
