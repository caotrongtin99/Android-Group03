package com.example.group3_fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentStudentDetail  extends Fragment  implements  FragmentCallbacks{
    MainActivity mainActivity;
    Context context;
    LinearLayout layoutDetail;
    int mCurrentPosition;
    int mLengthListStudent;
    Student mCurrentStudent;
    public  static  FragmentStudentDetail newInstance(){
        FragmentStudentDetail fragmentStudentDetail=new FragmentStudentDetail();
        Bundle args= new Bundle();
        args.putString("Key1","OK");
        fragmentStudentDetail.setArguments(args);
        return  fragmentStudentDetail;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            context=getActivity();
            mainActivity =(MainActivity) getActivity();

        }catch (IllegalStateException e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutDetail= (LinearLayout) inflater.inflate(R.layout.layout_student_detail,null);
        TextView txtCode=layoutDetail.findViewById(R.id.txtCode);

        final Button btnFirst =(Button) layoutDetail.findViewById(R.id.btnFirst);
        final Button btnPrev =(Button) layoutDetail.findViewById(R.id.btnPrev);
        final Button btnNext =(Button) layoutDetail.findViewById(R.id.btnNext);
        final Button btnLast =(Button) layoutDetail.findViewById(R.id.btnLast);
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onControlListFromFragmentToMain("DETAIL_FRAG","CONTROL_FIRST");

            }
        });
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onControlListFromFragmentToMain("DETAIL_FRAG","CONTROL_LAST");
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onControlListFromFragmentToMain("DETAIL_FRAG","CONTROL_PREV");
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onControlListFromFragmentToMain("DETAIL_FRAG","CONTROL_NEXT");

            }
        });
        return layoutDetail;

    }

    @Override
    public void onMsgFromMainToFragment(String msg) {

    }

    @Override
    public void onChangeSelectionFromMainToFragment(int position,int length, Student student) {
        mCurrentPosition=position;
        mCurrentStudent=student;
        mLengthListStudent=length;
        TextView txtCode= layoutDetail.findViewById(R.id.txtCode);
        TextView txtFullName =layoutDetail.findViewById(R.id.txtFullName);
        TextView txtClassName = layoutDetail.findViewById(R.id.txtClass);
        TextView txtPointAgv= layoutDetail.findViewById(R.id.txtPointAgv);
        txtCode.setText(mCurrentStudent.getCode());
        txtFullName.setText("Họ tên: " +mCurrentStudent.getFullName());
        txtClassName.setText("Lớp: "+mCurrentStudent.getClassName());
        txtPointAgv.setText("Điểm trung bình: "+mCurrentStudent.getPointAgv());
        resetControl();
    }
    private  void resetControl(){
        final Button btnFirst =(Button) layoutDetail.findViewById(R.id.btnFirst);
        final Button btnPrev =(Button) layoutDetail.findViewById(R.id.btnPrev);
        final Button btnNext =(Button) layoutDetail.findViewById(R.id.btnNext);
        final Button btnLast =(Button) layoutDetail.findViewById(R.id.btnLast);
        if (mCurrentPosition==0){
            if(btnFirst.isEnabled()){
                btnFirst.setEnabled(false);
            }
            if(btnPrev.isEnabled()){
                btnPrev.setEnabled(false);
            }
            if (!btnLast.isEnabled()){
                btnLast.setEnabled(true);
            }
            if(!btnNext.isEnabled()){
                btnNext.setEnabled(true);
            }
        }else if(mCurrentPosition==mLengthListStudent-1){
            if (btnLast.isEnabled()){
                btnLast.setEnabled(false);
            }
            if (btnNext.isEnabled()){
                btnNext.setEnabled(false);
            }
            if(!btnFirst.isEnabled()){
                btnFirst.setEnabled(true);
            }
            if(!btnPrev.isEnabled()){
                btnPrev.setEnabled(true);
            }

        }else{
            if (!btnFirst.isEnabled()){
                btnFirst.setEnabled(true);
            }
            if(!btnPrev.isEnabled()){
                btnPrev.setEnabled(true);
            }
            if (!btnNext.isEnabled()){
                btnNext.setEnabled(true);
            }
            if (!btnLast.isEnabled()){
                btnLast.setEnabled(true);
            }

        }

    }

    @Override
    public void onControlListFromMainToFragment(String controlCode) {

    }
}

