package com.example.musicapp.playlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.musicapp.R;

public class FragmentDialogCreatePlaylist extends DialogFragment implements View.OnClickListener {

    EditText txtTitlePlaylist;
    Button btnCancel;
    Button btnSubmit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_create_playlist, null);
        txtTitlePlaylist = view.findViewById(R.id.txtTilePlaylistCreate);
        btnCancel = view.findViewById(R.id.btnCancelCreatePlaylist);
        btnSubmit = view.findViewById(R.id.btnSubmitCreatePlaylist);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        txtTitlePlaylist.requestFocus();
//        txtTitlePlaylist.setSelection(txtTitlePlaylist.length());
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        // Add action buttons
//                .setPositiveButton("Tạo", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                        Toast.makeText(getActivity().getApplicationContext(),txtTitlePlaylist.getText(),Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        FragmentDialogCreatePlaylist.this.getDialog().cancel();
//                    }
//                });


        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelCreatePlaylist:
                FragmentDialogCreatePlaylist.this.getDialog().cancel();
                break;
            case R.id.btnSubmitCreatePlaylist:
                String title = txtTitlePlaylist.getText().toString().trim();
                if (!title.isEmpty()) {
                    long result = PlaylistModel.createPlaylist(title);
                    if (result > 0) {
                        FragmentPlaylist.refreshPlaylist();
                        Toast.makeText(getActivity().getApplicationContext(), "New playlist is created", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

                    }
                    FragmentDialogCreatePlaylist.this.getDialog().cancel();
                }
                break;
            default:
                break;
        }
    }
}
