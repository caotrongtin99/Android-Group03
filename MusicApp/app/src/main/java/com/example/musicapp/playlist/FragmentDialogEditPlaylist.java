package com.example.musicapp.playlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.musicapp.R;
@SuppressLint("ValidFragment")
public class FragmentDialogEditPlaylist extends DialogFragment implements View.OnClickListener {

    private EditText txtTitlePlaylist;
    private Button btnCancel;
    private Button btnSubmit;
    private PlaylistModel mCurrentPlaylist;
    private PlaylistSongActivity mPlaylistSongActivity;

    @SuppressLint("ValidFragment")
    public FragmentDialogEditPlaylist(PlaylistModel playlistModel, PlaylistSongActivity playlistSongActivity) {
        mCurrentPlaylist = playlistModel;
        mPlaylistSongActivity = playlistSongActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_edit_playlist, null);
        txtTitlePlaylist = view.findViewById(R.id.txtTilePlaylistEdit);
        btnCancel = view.findViewById(R.id.btnCancelEditPlaylist);
        btnSubmit = view.findViewById(R.id.btnSubmitEditPlaylist);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        txtTitlePlaylist.setText(mCurrentPlaylist.getTitle());
        txtTitlePlaylist.requestFocus();
        txtTitlePlaylist.setSelection(txtTitlePlaylist.length());

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelEditPlaylist:
                FragmentDialogEditPlaylist.this.getDialog().cancel();
                break;
            case R.id.btnSubmitEditPlaylist:
                String titleEdit = txtTitlePlaylist.getText().toString();
                if (titleEdit.isEmpty()) {
                    break;
                }
                mCurrentPlaylist.setTitle(titleEdit);
                long result = PlaylistModel.updateTitlePlaylist(mCurrentPlaylist);
                if (result > 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Sửa tiêu đề thành công", Toast.LENGTH_LONG).show();
                    mPlaylistSongActivity.refreshTitlePlaylist(mCurrentPlaylist.getTitle());
                    FragmentPlaylist.refreshPlaylist();
                    FragmentPlaylist.refreshPlaylist();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Thất bại", Toast.LENGTH_LONG).show();
                }
                FragmentDialogEditPlaylist.this.getDialog().cancel();
                break;
            default:
                break;
        }
    }
}
