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

public class BottomSheetOptionSong extends BottomSheetDialogFragment implements View.OnClickListener{
    private MainActivity _mainActivity;
    private SongModel mCurrentSong;
    private TextView mTxtTitleSong;
    private TextView mTxtArtist;
    private TableRow mTbrAddQueue;
    private TableRow mTbrAddPlaylist;
    private TableRow mTbrMakeRingTone;
    private TableRow mTbrDeleteSong;
    private TableRow mTbrShowSongDetail;
    private ImageView mImgSong;
    private ImageHelper mImageHelper;
    private static final String TAG = "BottomSheetOptionSong";
    @SuppressLint("ValidFragment")
    public BottomSheetOptionSong(SongModel songOption) {
        mCurrentSong = songOption;
        _mainActivity = (MainActivity) getActivity();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_option_song, null);
        mImageHelper = new ImageHelper();

        mTxtTitleSong = contentView.findViewById(R.id.txtTitleSong);
        mTxtArtist = contentView.findViewById(R.id.txtArtist);
        mTbrAddQueue = contentView.findViewById(R.id.btnAddSongToQueue);
        mTbrAddPlaylist = contentView.findViewById(R.id.btnAddSongToPlaylist);
        mTbrMakeRingTone = contentView.findViewById(R.id.btnMakeRingTone);
        mImgSong = contentView.findViewById(R.id.imgSong);
        mTbrDeleteSong= contentView.findViewById(R.id.mTbrDeleteSong);
        mTbrShowSongDetail = contentView.findViewById(R.id.mTbrShowSongDetail);



        mTbrAddQueue.setOnClickListener(this);
        mTbrAddPlaylist.setOnClickListener(this);
        mTbrMakeRingTone.setOnClickListener(this);
        mTbrShowSongDetail.setOnClickListener(this);

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
            case R.id.mTbrShowSongDetail:
                _mainActivity = (MainActivity) getActivity();
                BottomSheetShowSongInfo bottomSheetDialogFragment = new BottomSheetShowSongInfo(mCurrentSong);
                bottomSheetDialogFragment.show(_mainActivity.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                break;
            case R.id.btnMakeRingTone:
                //check permission write ringtone
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    boolean canWrite = Settings.System.canWrite(getActivity().getApplicationContext());
                    if (!canWrite){
                        Toast.makeText(getActivity(), "Xin cấp quyền ghi hệ thống để thực hiện thao tác này", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.fromParts("package", getActivity().getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                try {
                    Uri uri = MediaStore.Audio.Media.getContentUriForPath(mCurrentSong.getPath());
                    getContext().getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + mCurrentSong.getPath() + "\"", null);
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DATA, mCurrentSong.getPath());
                    values.put(MediaStore.MediaColumns.TITLE, mCurrentSong.getTitle());
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
                    values.put(MediaStore.Audio.Media.ARTIST, mCurrentSong.getArtist());
                    values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                    values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                    values.put(MediaStore.Audio.Media.IS_ALARM, false);
                    values.put(MediaStore.Audio.Media.IS_MUSIC, false);
                    Uri newUri = getContext().getContentResolver().insert(uri, values);
                    RingtoneManager.setActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_RINGTONE, newUri);
                    Toast.makeText(getContext(),"Đặt làm nhạc chuông thành công!",Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    ex.printStackTrace();
                    Log.e(TAG, "onClick: "+ex.getMessage());
                    Toast.makeText(getContext(),"Đặt làm nhạc chuông thất bại!",Toast.LENGTH_LONG).show();
                }


                break;
        }
    }
}
