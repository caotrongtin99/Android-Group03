package com.example.musicapp.albums;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.MainActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.musicapp.R;
import com.example.musicapp.listsong.RecyclerItemClickListener;

public class AlbumFragment extends Fragment {
    private static final String TAG = "FRAGMENT_ALBUM";
    public static final String SENDER = "FRAGMENT_ALBUM";

    View view;
    ArrayList<AlbumViewModel> arrAlbum;
    RecyclerView RCalbum;
    Context context;
    AlbumListAdapter albumListAdapter;
    SwipeRefreshLayout mSwpListAlbum;
    static boolean mIsLoading;
    static int take = 10;
    static String searchValue = "";
    TextView albumSizeTxtView;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //get context activity
        context = (MainActivity) getActivity();

        //get view from infalter
        view = inflater.inflate(R.layout.fragment_album, container, false);

        //get list artist from db
        arrAlbum = AlbumProvider.getAlbumModelPaging(context, searchValue, 0, 20);
        albumSizeTxtView = (TextView) view.findViewById(R.id.albumSize);
        int size = arrAlbum.size();
        albumSizeTxtView.setText(new String(String.valueOf(size)) + " Albums");
//        System.out.println(arrAlbum.size());
        //get RecyclerView Album by id
        RCalbum = (RecyclerView) view.findViewById(R.id.rvAlbumList);

        mSwpListAlbum = view.findViewById(R.id.swpListAlbum);

        //map layout with adapter
        albumListAdapter = new AlbumListAdapter(context, arrAlbum);
        RCalbum.setLayoutManager(new LinearLayoutManager(context));
        RCalbum.setAdapter(albumListAdapter);
        RCalbum.addOnItemTouchListener(new RecyclerItemClickListener(context, RCalbum, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, AlbumSongsActivity.class);
                AlbumModel albumModel = (AlbumModel) arrAlbum.get(position).getAlbumModel();
                intent.putExtra("infoAlbum", albumModel);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        RCalbum.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        mSwpListAlbum.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListAlbum();
            }
        });
        return view;
    }

    private void refreshListAlbum() {
        arrAlbum.clear();
        albumListAdapter.notifyDataSetChanged();
        ArrayList<AlbumViewModel> temp = AlbumProvider.getAlbumModelPaging(context, searchValue, 0, 20);
        arrAlbum.addAll(temp);
        albumListAdapter.notifyDataSetChanged();
        mSwpListAlbum.setRefreshing(false);
    }

    private void loadMore() {
//        arrAlbum.add(null);
//        albumListAdapter.notifyItemInserted(arrAlbum.size());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<AlbumViewModel> tempAudioList = AlbumProvider.getAlbumModelPaging(context, searchValue, arrAlbum.size(), take);
//                arrAlbum.remove(arrAlbum.size() - 1);
//                albumListAdapter.notifyItemRemoved(arrAlbum.size());
                arrAlbum.addAll(tempAudioList);
                albumListAdapter.notifyDataSetChanged();
//                albumListAdapter.notifyItemInserted(arrAlbum.size());
                mIsLoading = false;
            }
        });
    }

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    public void UpdateSearch(String s) {
        if (s.equals(searchValue)) return;
        searchValue = s;
        mIsLoading = true;
        ArrayList<AlbumViewModel> tempAudioList = AlbumProvider.getAlbumModelPaging(context, searchValue, 0, 20);
        arrAlbum.clear();
        arrAlbum.addAll(tempAudioList);
        albumListAdapter.notifyDataSetChanged();
        mIsLoading = false;
    }

}
