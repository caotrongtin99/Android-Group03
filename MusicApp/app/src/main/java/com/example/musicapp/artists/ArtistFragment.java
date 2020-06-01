package com.example.musicapp.artists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.listsong.RecyclerItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArtistFragment extends Fragment implements ArtistClickListener {
    View view;
    ArrayList<ArtistViewModel> artists;
    RecyclerView RVartist;
    Context context;
    ArtistAdapter adapter;
    static boolean mIsLoading;
    SwipeRefreshLayout SRL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        view =  inflater.inflate(R.layout.fragment_artists, container, false);
        artists = ArtistProvider.getArtists(MainActivity.mDatabaseManager);
        RVartist = (RecyclerView) view.findViewById(R.id.artistList);
        SRL = view.findViewById(R.id.swpArtist);

        adapter = new ArtistAdapter(context, artists, this);
        RVartist.setLayoutManager(new LinearLayoutManager(context));
        RVartist.setAdapter(adapter);

        RVartist.addOnItemTouchListener(new RecyclerItemClickListener(context, RVartist, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        RVartist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!mIsLoading && linearLayoutManager != null && linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                    loadMore();
                    mIsLoading = true;
                }
            }
        });

//        listView.setAdapter(new ArtistAdapter(this.getContext(), artists));
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv = (TextView) view.findViewById(R.id.txt_ArtistName);
//                String artist = tv.getText().toString();
//                Intent intent = new Intent(MainActivity.getMainActivity(), ArtistSongsActivity.class);
//                intent.putExtra("artist", artist);
//                startActivity(intent);
//            }
//        });
        return view;
    }

    private void refreshListArtist(){
        artists.clear();
        adapter.notifyDataSetChanged();
        artists.addAll(ArtistProvider.getArtists(MainActivity.mDatabaseManager));
        adapter.notifyDataSetChanged();
        SRL.setRefreshing(false);
    }

    private void loadMore() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<ArtistViewModel> tmp = ArtistProvider.getArtists(MainActivity.mDatabaseManager);
                artists.addAll(tmp);
                adapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        });
    }

    @Override
    public void onClick(int position) {
        String artist = artists.get(position).getName();
        Intent intent = new Intent(MainActivity.getMainActivity(), ArtistSongsActivity.class);
        intent.putExtra("artist", artist);
        startActivity(intent);
    }
}
