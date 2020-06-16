package com.example.musicapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;

import com.example.musicapp.artists.ArtistFragment;
import com.example.musicapp.play.PlayService;
import com.example.musicapp.playlist.FragmentPlaylist;
import com.example.musicapp.playlist.PlaylistSongModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.playlist.FragmentDialogPlaylist;
import com.example.musicapp.playlist.PlaylistModel;
import com.example.musicapp.playlist.PlaylistSongActivity;


@SuppressLint("ValidFragment")
public class BottomSheetOptionSongPlaylist extends BottomSheetDialogFragment implements View.OnClickListener {

    private SongModel mCurrentSong;
    private PlaylistModel mCurrentPlaylist;
    private TextView mTxtTitleSong;
    private TextView mTxtArtist;
    private TableRow mTbrDeleteSongInPlaylist;
    private ImageView mImgSong;
    private ImageHelper mImageHelper;

    private PlaylistSongActivity mSongPlaylistActivity;

    @SuppressLint("ValidFragment")
    public BottomSheetOptionSongPlaylist(SongModel songOption, PlaylistModel playlistOption, PlaylistSongActivity playlistSongActivity) {
        mCurrentSong = songOption;
        mCurrentPlaylist = playlistOption;
        mSongPlaylistActivity = playlistSongActivity;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_option_song_playlist, null);
        mImageHelper = new ImageHelper();

        mTxtTitleSong = contentView.findViewById(R.id.txtTitleSong);
        mTxtArtist = contentView.findViewById(R.id.txtArtist);
        mTbrDeleteSongInPlaylist = contentView.findViewById(R.id.btnDeleteSongInPlaylist);
        mImgSong = contentView.findViewById(R.id.imgSong);

        mTbrDeleteSongInPlaylist.setOnClickListener(this);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mTxtTitleSong.setText(mCurrentSong.getTitle());
                mTxtArtist.setText(mCurrentSong.getArtist());
                mImgSong.setImageBitmap(ImageHelper.getBitmapFromPath(mCurrentSong.getPath(), R.mipmap.music_128));
            }
        });


        dialog.setContentView(contentView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDeleteSongInPlaylist:
                long result = PlaylistSongModel.deleteSongInPlaylist(mCurrentSong.getSongId(), mCurrentPlaylist.getId());
                if (result > 0) {
                    Toast.makeText(getActivity(), "Đã xóa khỏi playlist", Toast.LENGTH_LONG).show();
                    mSongPlaylistActivity.refreshSongPlaylist();
                    //PlaylistSongActivity.refreshSongPlaylist();
                    FragmentPlaylist.refreshPlaylist();
                } else {
                    Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_LONG).show();
                }
                BottomSheetOptionSongPlaylist.this.dismiss();
                break;

        }
    }
}
