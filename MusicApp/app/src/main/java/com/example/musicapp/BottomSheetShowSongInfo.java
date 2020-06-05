package com.example.musicapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.listsong.SongModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetShowSongInfo extends BottomSheetDialogFragment implements View.OnClickListener{

    private SongModel mCurrentSong;
    private TextView txt_singer;
    private TextView txt_folder;
    private TextView txt_albums;
    private TextView txtTitleSongDetail;
    private ImageView mImgSongDetail;
    private ImageHelper mImageHelper;
    private static final String TAG = "BottomSheetShowSongInfo";
    @SuppressLint("ValidFragment")
    public BottomSheetShowSongInfo(SongModel songOption) {
        mCurrentSong = songOption;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_option_detail_song, null);
        mImageHelper = new ImageHelper();

        txtTitleSongDetail = contentView.findViewById(R.id.txtTitleSongDetail);
        txt_singer = contentView.findViewById(R.id.txt_singer);
        txt_albums = contentView.findViewById(R.id.txt_albums);
        txt_folder = contentView.findViewById(R.id.txt_folder);
        mImgSongDetail = contentView.findViewById(R.id.imgSongDetail);



        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                txtTitleSongDetail.setText(mCurrentSong.getTitle());
                txt_singer.setText(mCurrentSong.getArtist());
                txt_folder.setText(mCurrentSong.getFolder());
                txt_albums.setText(mCurrentSong.getAlbum());
                mImgSongDetail.setImageBitmap(ImageHelper.getBitmapFromPath(mCurrentSong.getPath(), R.mipmap.music_128_foreground));
            }
        });

        dialog.setContentView(contentView);
    }
    @Override
    public void onClick(View v) {

    }
}
