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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.albums.AlbumProvider;
import com.example.musicapp.albums.AlbumViewModel;
import com.example.musicapp.listsong.RecyclerItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArtistFragment extends Fragment{
    View view;
    ArrayList<ArtistViewModel> artists;
    RecyclerView RVartist;
    Context context;
    ArtistAdapter adapter;
    static boolean mIsLoading;
    SwipeRefreshLayout SRL;
    static String searchValue = "";
    int take = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        view =  inflater.inflate(R.layout.fragment_artists, container, false);
        artists = ArtistProvider.getArtistModelPaging(context, searchValue, 0, 20);
        RVartist = (RecyclerView) view.findViewById(R.id.artistList);
        SRL = view.findViewById(R.id.swpArtist);

        adapter = new ArtistAdapter(context, artists);
        RVartist.setLayoutManager(new LinearLayoutManager(context));
        RVartist.setAdapter(adapter);

        RVartist.addOnItemTouchListener(new RecyclerItemClickListener(context, RVartist, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, ArtistSongsActivity.class);
                String artist = artists.get(position).getName();
                intent.putExtra("artist", artist);
                startActivity(intent);
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

        SRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListArtist();
            }
        });
        return view;
    }

    public void refreshListArtist(){
        artists.clear();
        adapter.notifyDataSetChanged();
        artists.addAll(ArtistProvider.getArtistModelPaging(context, searchValue, 0, 20));
        adapter.notifyDataSetChanged();
        SRL.setRefreshing(false);
    }

    private void loadMore() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<ArtistViewModel> tmp = ArtistProvider.getArtistModelPaging(context, searchValue, artists.size(), take);
                artists.addAll(tmp);
                adapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        });
    }

    public void UpdateSearch(String s) {
        if (s == searchValue) return;
        searchValue = s;
        mIsLoading = true;
        ArrayList<ArtistViewModel> temp = ArtistProvider.getArtistModelPaging(context, searchValue, 0, 20);
        artists.clear();
        artists.addAll(temp);
        adapter.notifyDataSetChanged();
        mIsLoading = false;
    }
}
