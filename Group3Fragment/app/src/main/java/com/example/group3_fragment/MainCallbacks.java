package com.example.group3_fragment;

public interface MainCallbacks {
    void onMsgFromFragmentToMain(String sender,String msg);
    void onChangeSelectionFromFragmentToMain(String sender,int position, int length,Student student);
    void onControlListFromFragmentToMain (String sender,String controlCode);
}
