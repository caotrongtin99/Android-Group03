package com.example.musicapp.artists;

import com.example.musicapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class ArtistAdapter extends BaseAdapter {
    private List<Artist> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public ArtistAdapter(Context context, List<Artist> artists){
        this.context = context;
        this.listData = artists;
        this.layoutInflater = LayoutInflater.from(context);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_artist, null);
            holder = new ViewHolder();
            //holder.flagView = (ImageView) convertView.findViewById(R.id.imageView_flag);
            holder.txtArtistName = (TextView) convertView.findViewById(R.id.txt_ArtistName);
            holder.txtTracks = (TextView) convertView.findViewById(R.id.txt_Tracks);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Artist artists = this.listData.get(position);
        holder.txtArtistName.setText(artists.getName());
        holder.txtTracks.setText(artists.getTracks() + " track(s)");

        //int imageId = this.getMipmapResIdByName(artists.getFlagName());

        return convertView;
    }

    static class ViewHolder{
        TextView txtArtistName;
        TextView txtTracks;
    }
}
