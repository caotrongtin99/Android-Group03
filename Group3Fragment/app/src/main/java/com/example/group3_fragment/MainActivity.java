package com.example.group3_fragment;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity  implements  MainCallbacks{

    FragmentListStudent fragmentListStudent;
    FragmentStudentDetail fragmentStudentDetail;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment.getClass()==FragmentListStudent.class){
            fragmentListStudent=(FragmentListStudent) fragment;
        }
        if (fragment.getClass()==FragmentStudentDetail.class){
            fragmentStudentDetail=(FragmentStudentDetail) fragment;
        }
    }

    @Override
    public void onMsgFromFragmentToMain(String sender, String msg) {
        if(sender.equals("LIST_FRAG")){

        }
        if(sender.equals("DETAIL_FRAG")){

        }
    }

    @Override
    public void onChangeSelectionFromFragmentToMain(String sender,int position, int length,Student student) {
        if(sender.equals("LIST_FRAG")){
            fragmentStudentDetail.onChangeSelectionFromMainToFragment(position, length,student);
        }
    }

    @Override
    public void onControlListFromFragmentToMain(String sender,String controlCode) {
        if(sender.equals("DETAIL_FRAG")){
            fragmentListStudent.onControlListFromMainToFragment(controlCode);
        }

    }
}
