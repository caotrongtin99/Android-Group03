package com.example.musicapp;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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

import androidx.fragment.app.FragmentManager;

import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.FragmentListSong;
import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.playlist.FragmentDialogPlaylist;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

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
    private TableRow mTbrRenameSong;
    //private TableRow mTbrAddSongToPlaylist;
    private ImageView mImgSong;
    private ImageHelper mImageHelper;
    private FragmentListSong fragmentListSong;
    private static final String TAG = "BottomSheetOptionSong";
    @SuppressLint("ValidFragment")
    public BottomSheetOptionSong(SongModel songOption) {
        //fragmentListSong = new FragmentListSong();
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
        mTbrDeleteSong = contentView.findViewById(R.id.mTbrDeleteSong);
        mTbrRenameSong = contentView.findViewById(R.id.mTbrRenameSong);
        mTbrShowSongDetail = contentView.findViewById(R.id.mTbrShowSongDetail);



        mTbrAddQueue.setOnClickListener(this);
        mTbrAddPlaylist.setOnClickListener(this);
        mTbrMakeRingTone.setOnClickListener(this);
        mTbrShowSongDetail.setOnClickListener(this);
        mTbrDeleteSong.setOnClickListener(this);
        mTbrRenameSong.setOnClickListener(this);

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

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = new EditNameDialogFragment(mCurrentSong);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTbrRenameSong:
                showEditDialog();
                getDialog().dismiss();
                break;
            case R.id.btnAddSongToPlaylist:
                FragmentDialogPlaylist fragmentDialogPlaylist = new FragmentDialogPlaylist(mCurrentSong);
                fragmentDialogPlaylist.show(getActivity().getSupportFragmentManager(), "ADD_SONG_TO_LIST_QUEUE");
                BottomSheetOptionSong.this.dismiss();
                break;
            case R.id.mTbrDeleteSong:
                String query2 = "SELECT * FROM songs WHERE title = '" + mCurrentSong.getTitle() +"'";

                DatabaseManager dbManager = DatabaseManager.newInstance(getContext());
                SQLiteDatabase db = dbManager.getReadableDatabase();
                db.delete("songs", "title = '" + mCurrentSong.getTitle() + "'", null);
                Cursor c2 = db.rawQuery(query2, null);

                if(!c2.moveToFirst()){
                    Log.e("ABC: ", mCurrentSong.getTitle() + " DELETED");
                }
                FragmentListSong.refresh();
                getDialog().dismiss();

                break;
            case R.id.mTbrShowSongDetail:
                _mainActivity = (MainActivity) getActivity();
                BottomSheetShowSongInfo bottomSheetDialogFragment = new BottomSheetShowSongInfo(mCurrentSong);
                bottomSheetDialogFragment.show(_mainActivity.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                break;
            case R.id.btnMakeRingTone:

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
