package com.example.group3_fragment;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListStudentAdapter extends BaseAdapter {
    private ArrayList<Student> lstStudent;
    private LayoutInflater layoutInflater;
    private Context context;


    public ListStudentAdapter(Context context, ArrayList<Student> lstStudent) {
        this.context = context;
        this.lstStudent = lstStudent;
    }

    @Override
    public int getCount() {
        return lstStudent.size();
    }

    @Override
    public Object getItem(int position) {
        return lstStudent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TextView fullName;
        ImageView imgAvatar;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_student, parent, false);
            viewHolder = new ViewHolder();
            imgAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);

            fullName = (TextView) convertView.findViewById(R.id.txtFullName);
            viewHolder.imgAvatar = imgAvatar;
            viewHolder.fullNameView = fullName;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            fullName = viewHolder.fullNameView;
            imgAvatar = viewHolder.imgAvatar;
        }

        Student cus = lstStudent.get(position);


        int imgAvatarId = getImageIdByName(1);
        imgAvatar.setImageResource(imgAvatarId);
        fullName.setText(cus.getCode());

        return convertView;
    }

    public int getImageIdByName(int type) {
        String imgName = "user";

        String pkgName = context.getPackageName();
        int imgID = context.getResources().getIdentifier(imgName, "mipmap", pkgName);
        return imgID;
    }

    private class ViewHolder {
        ImageView imgAvatar;
        TextView fullNameView;

    }
}
