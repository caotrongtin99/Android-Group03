package com.example.musicapp.tracks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        return null;
    }
}
