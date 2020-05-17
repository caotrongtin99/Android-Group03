package com.example.musicapp.tracks;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapp.R;

import java.util.List;

public class ListTracksAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<Track> tracks;

    public ListTracksAdapter(Context aContext, List<Track> tracks){
        this.context = aContext;
        this.tracks = tracks;
        layoutInflater = LayoutInflater.from(aContext);

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_track_layout, null);
            holder = new ViewHolder();
            holder.imageSongTextView = (ImageView) convertView.findViewById(R.id.imageView_track);
            holder.titleTextView= (TextView) convertView.findViewById(R.id.textView_song);
            holder.artistTextView = (TextView) convertView.findViewById(R.id.textView_artists);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Track track = this.tracks.get(position);
        holder.titleTextView.setText(track.getTitle());
        holder.artistTextView.setText(track.getArtist());
        int imageId = this.getMipmapResIdByName(track.getImageName());

        holder.imageSongTextView.setImageResource(imageId);

        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        System.out.print("RESID......................"+resID);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);

        return resID;
    }

    static class ViewHolder {
        ImageView imageSongTextView;
        TextView titleTextView;
        TextView artistTextView;
    }

}
