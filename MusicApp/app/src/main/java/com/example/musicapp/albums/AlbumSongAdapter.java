package com.example.musicapp.albums;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.listsong.SongModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumSongAdapter extends RecyclerView.Adapter<AlbumSongAdapter.AlbumSongViewHolder> {

    private List<SongModel> mylist;
    private Context myContext;

    public AlbumSongAdapter(Context context,List<SongModel> list){
        myContext = context;
        mylist = list;
    }

    @NonNull
    @Override
    public AlbumSongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_artist_song, viewGroup, false);
        AlbumSongViewHolder albumSongViewHolder = new AlbumSongViewHolder(view);
        return albumSongViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumSongViewHolder albumSongViewHolder, int i) {
        albumSongViewHolder.BindData(i);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class AlbumSongViewHolder extends RecyclerView.ViewHolder{
        TextView TVNumber;
        TextView TVNameSong;
        TextView TVNameArtist;
        TextView TVDuration;
        public AlbumSongViewHolder(@NonNull View itemView) {
            super(itemView);

            TVNameSong = (TextView) itemView.findViewById(R.id.txtSongTitle);
            TVNameArtist = (TextView) itemView.findViewById(R.id.txtArtist);
            TVDuration = (TextView) itemView.findViewById(R.id.txtDuration);
        }
        public void BindData(int position) {
            SongModel albumSongsModel = mylist.get(position);
            String title = albumSongsModel.getTitle().length() > 35
                    ? albumSongsModel.getTitle().substring(0,35) + "..."
                    : albumSongsModel.getTitle();
            TVNameSong.setText(title);
            TVNameArtist.setText(albumSongsModel.getArtist());
            TVDuration.setText(SongModel.formateMilliSeccond(albumSongsModel.getDuration()));
        }
    }
}
