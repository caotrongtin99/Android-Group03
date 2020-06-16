package com.example.musicapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.FragmentListSong;
import com.example.musicapp.listsong.SongModel;
import com.example.musicapp.playlist.FragmentPlaylist;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class EditNameDialogFragment extends DialogFragment {
    private EditText mEditText;
    private Button btnRenameSong;
    private Button btnCloseDialog;
    private MainActivity _mainActivity;
    private SongModel mCurrentSong;
    private FragmentListSong fragmentListSong;

    public EditNameDialogFragment(SongModel songModel) {
        this.mCurrentSong = songModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txt_newtitle);
        btnRenameSong = (Button) view.findViewById(R.id.btnRenameSong);
        btnCloseDialog = (Button) view.findViewById(R.id.btnCloseDialog);
        mEditText.requestFocus();
        String songTitle = mCurrentSong.getTitle();
        mEditText.setText(songTitle);
        mEditText.setSelection(songTitle.length());
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btnRenameSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = mEditText.getText().toString();

                mCurrentSong.setTitle(newTitle);
                SQLiteDatabase db = DatabaseManager.getInstance().getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(SongModel.COLUMN_TITLE, mCurrentSong.getTitle());
                long id = db.update(SongModel.TABLE_NAME, contentValues, SongModel.COLUMN_ID + " =? ", new String[]{String.valueOf(mCurrentSong.getId())});
                FragmentListSong.refresh();
                //FragmentPlaylist.refreshPlaylist();
                getDialog().dismiss();
            }
        });
    }
}

