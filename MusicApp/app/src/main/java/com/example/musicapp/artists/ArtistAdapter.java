package com.example.musicapp.artists;

import com.example.musicapp.ImageCacheHelper;
import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.albums.AlbumListAdapter;
import com.example.musicapp.listsong.SongModel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistViewHolder> {
    private List<ArtistViewModel> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private  ArtistClickListener artistClickListener;

    public ArtistAdapter(Context context, List<ArtistViewModel> artists, ArtistClickListener artistClickListener){
        this.context = context;
        this.listData = artists;
        this.layoutInflater = LayoutInflater.from(context);
        this.artistClickListener = artistClickListener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_artist, parent, false);
        ArtistViewHolder ar = new ArtistViewHolder(view, artistClickListener);
        return ar;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        holder.BindData(listData, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}

class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView TVartist;
    TextView TVcount;
    ImageView img;
    private ImageCacheHelper mImageCacheHelper;
    private ArtistClickListener artistClickListener;

//    public void setItemClickListener(ArtistClickListener itemClickListener)
//    {
//        this.artistClickListener = itemClickListener;
//    }


    public ArtistViewHolder(@NonNull View itemView, ArtistClickListener artistClickListener) {
        super(itemView);
        mImageCacheHelper=new ImageCacheHelper(R.mipmap.album_128);
        TVartist = (TextView)itemView.findViewById(R.id.txt_ArtistName);
        TVcount = (TextView)itemView.findViewById(R.id.txt_Tracks);
        img = (ImageView)itemView.findViewById(R.id.imgArtist);
        this.artistClickListener = artistClickListener;
        itemView.setOnClickListener(this);
    }

    public void BindData(List<ArtistViewModel> list, int pos) {
        ArtistViewModel artist = list.get(pos);
        String artistName = artist.getName().length() > 35
                ? artist.getName().substring(0,35) + "..."
                : artist.getName();

        String count = artist.getTracks() > 1
                ?artist.getTracks() + " tracks"
                :artist.getTracks() + " track";

        TVartist.setText(artistName);
        TVcount.setText(count);

        SongModel song = new SongModel();
        song.setAlbumId(artist.getAlbumID());
        song.setPath(artist.getPath());

        final Bitmap bitmap = mImageCacheHelper.getBitmapCache(song.getAlbumId());
        if(bitmap != null) {
            img.setImageBitmap(bitmap);
        }
        else {
            mImageCacheHelper.loadAlbumArt(img, song);
        }
    }

    @Override
    public void onClick(View v) {
        artistClickListener.onClick(getAdapterPosition());
    }
}
