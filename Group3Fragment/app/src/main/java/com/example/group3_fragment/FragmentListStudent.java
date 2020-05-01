package com.example.group3_fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class FragmentListStudent extends Fragment implements FragmentCallbacks {
    MainActivity main;
    Context context;
    LinearLayout layoutStudentList;
    ListStudentAdapter listCustomerAdapter;
    Student currentStudent;
    int currentPosition;
    private ArrayList<Student> lstStudent = null;

    private ArrayList<Student> getListStudent() {
        ArrayList<Student> list = new ArrayList<Student>();

        Student tin = new Student("1753014", "Cao Trong Tin", "17CLC","9");
        Student thinh = new Student("1753015", "Thinh Le", "17CLC","3");
        Student tuan = new Student("1753016", "Tuan", "17CLC","9");
        Student thang = new Student("1753014", "Cao Trong Thang", "17CLC","9");
        Student  lu= new Student("183123", "Ha Lu", "17CLC","8");


        list.add(tin);
        list.add(tuan);
        list.add(thinh);
        list.add(thang);
        list.add(lu);
        return list;
    }

    public static FragmentListStudent newInstance() {
        FragmentListStudent fragmentListStudent = new FragmentListStudent();
        Bundle args = new Bundle();
        args.putString("Key1", "OK");
        fragmentListStudent.setArguments(args);
        return fragmentListStudent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lstStudent = getListStudent();
        layoutStudentList = (LinearLayout) inflater.inflate(R.layout.layout_list_student, null);

        ListView listView = (ListView) layoutStudentList.findViewById(R.id.lsvStudent);
        listCustomerAdapter = new ListStudentAdapter(context, lstStudent);
        listView.setAdapter(listCustomerAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setDataOnChangeItem(position);
            }
        });

        return layoutStudentList;

    }

    private void setDataOnChangeItem(int position) {
        currentStudent = lstStudent.get(position);
        currentPosition = position;
        main.onChangeSelectionFromFragmentToMain("LIST_FRAG" ,position,lstStudent.size(), currentStudent);
        //
        TextView txtCurrentChose = layoutStudentList.findViewById(R.id.txtCurrentChose);
        txtCurrentChose.setText("Mã số: " + currentStudent.getCode());
    }

    @Override
    public void onMsgFromMainToFragment(String msg) {

    }

    @Override
    public void onChangeSelectionFromMainToFragment(int position,int length, Student student) {

    }

    @Override
    public void onControlListFromMainToFragment(String controlCode) {
        final ListView lsvStudent = (ListView) layoutStudentList.findViewById(R.id.lsvStudent);

        switch (controlCode) {
            case "CONTROL_FIRST": {
                currentPosition = 0;
                break;
            }
            case "CONTROL_LAST": {
                currentPosition = lstStudent.size() - 1;
                break;
            }
            case "CONTROL_PREV": {
                currentPosition = currentPosition - 1 < 0 ? 0 : currentPosition - 1;
                break;
            }
            case "CONTROL_NEXT": {
                currentPosition = currentPosition + 1 > (lstStudent.size() - 1) ? (lstStudent.size() - 1) : currentPosition + 1;
                break;
            }
            default:
                break;
        }

        lsvStudent.clearFocus();
        lsvStudent.requestFocusFromTouch();

        lsvStudent.post(new Runnable() {
            @Override
            public void run() {

                lsvStudent.setItemChecked(currentPosition, true);
                lsvStudent.setSelection(currentPosition);

                lsvStudent.smoothScrollToPositionFromTop(currentPosition,0,100);

                listCustomerAdapter.notifyDataSetChanged();
//
                setDataOnChangeItem(currentPosition);
            }
        });

    }
}

