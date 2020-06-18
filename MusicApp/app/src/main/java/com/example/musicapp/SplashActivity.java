package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.example.musicapp.db.DatabaseManager;
import com.example.musicapp.listsong.SongModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {


    private String[] appPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    private static final int PERMISSION_REQUEST_CODE = 1240;
    private static final String TAG = "SplashActivity";
    private DatabaseManager mDatabaseManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));

//        finish();
//        setContentView(R.layout.activity_splash);
//        Utility.setTransparentStatusBar(this);

        if (checkAndRequestPermission()) {
            initApp();
        }
    }



    public void initApp() {
        mDatabaseManager = DatabaseManager.newInstance(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<SongModel> tempAudioList = SongModel.getAllAudioFromDevice(getApplicationContext());
                Integer a = tempAudioList.size();
                Long b = SongModel.getRowsSong(mDatabaseManager);
                if (SongModel.getRowsSong(mDatabaseManager) != tempAudioList.size()) {
                    for (SongModel song : tempAudioList) {
                        long id = SongModel.insertSong(mDatabaseManager, song);
                        Log.d(TAG, "onPostExecute: INSERT SONG FROM MAIN : " + id);
                    }
                }
            }
        }).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 100);

    }

    public boolean checkAndRequestPermission() {
        List<String> listPermissionNeded = new ArrayList<>();
        for (String perm : appPermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeded.add(perm);
            }
        }
        if (!listPermissionNeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeded.toArray(new String[listPermissionNeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                initApp();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String perName = entry.getKey();
                    Integer perResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, perName)) {
                        showDialog("", "Cấp quyền ", "Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkAndRequestPermission();
                            }
                        }, "Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }, false);
                    } else {
                        showDialog("", "Cấp quyền ", "Cài đặt", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                                checkAndRequestPermission();
                                Intent intent = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,   Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }, "Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }, false);
                        break;
                    }

                }
            }

        }
    }

    public AlertDialog showDialog(String title, String message, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
                                  String negativeLabel, DialogInterface.OnClickListener negativeOnclick, boolean isCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialog);
        builder.setTitle(title);
        //set color

        builder.setMessage(message);
        builder.setCancelable(isCancel);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }

}
