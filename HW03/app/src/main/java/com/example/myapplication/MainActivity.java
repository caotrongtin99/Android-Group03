package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText edit_user, edit_password, edit_retype;
    EditText edit_bd;
    Button btn_reset, btn_signup;
    RadioGroup radioGroup;
    RadioButton radioButton_male, radioButton_female;
    CheckBox checkbox_tenis, checkbox_futbal,checkbox_others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_user = (EditText) findViewById(R.id.edit_username);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_retype = (EditText) findViewById(R.id.edit_retypepassword);
        edit_bd = (EditText) findViewById(R.id.edit_bd);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton_male = (RadioButton) findViewById(R.id.radioButton_male);
        radioButton_female = (RadioButton) findViewById(R.id.radioButton_female);
        checkbox_tenis = (CheckBox) findViewById(R.id.checkbox_tenis);
        checkbox_futbal = (CheckBox) findViewById(R.id.checkbox_futbal);
        checkbox_others = (CheckBox) findViewById(R.id.checkbox_others);

        edit_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_user.setText("");
                edit_password.setText("");
                edit_retype.setText("");
                edit_bd.setText("");
                radioGroup.clearCheck();
                if (checkbox_tenis.isChecked()) {
                    checkbox_tenis.setChecked(false);
                }
                if (checkbox_futbal.isChecked()) {
                    checkbox_futbal.setChecked(false);
                }
                if (checkbox_others.isChecked()) {
                    checkbox_others.setChecked(false);
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password,retypepasssword, birthday,gender,hobbies ="";
                username = edit_user.getText().toString();
                password = edit_password.getText().toString();
                retypepasssword = edit_retype.getText().toString();
                birthday = edit_bd.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                if (radioButton_male.getId() == radioId)
                    gender = "Male";
                else gender = "Female";

                if (checkbox_futbal.isChecked())
                    hobbies +="Futbal";

                if (checkbox_tenis.isChecked())
                    hobbies += ",Tenis ";
                if (checkbox_others.isChecked())
                    hobbies+=",Others";
                if (username.length()==0)
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
                else if (radioGroup.getCheckedRadioButtonId()==-1)
                    Toast.makeText(MainActivity.this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
                else if (birthday.length()==0)
                    Toast.makeText(MainActivity.this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
                else if (password.length()==0)
                    Toast.makeText(MainActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                else if(retypepasssword.length()==0)
                    Toast.makeText(MainActivity.this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                else if (password.length() > 0 && retypepasssword.length() > 0){
                    if (!password.equals(retypepasssword)) {
                        Toast.makeText(MainActivity.this, "Mật khẩu không khớp!!!", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Intent myIntentA1A2;
                        myIntentA1A2 = new Intent(MainActivity.this, Main3Activity.class);
                        Bundle myBundle1 = new Bundle();
                        myBundle1.putString("username", username);
                        myBundle1.putString("password", password);
                        myBundle1.putString("birthday",birthday);
                        myBundle1.putString("gender", gender);
                        myBundle1.putString("hobbies",hobbies);
                        myIntentA1A2.putExtras(myBundle1);
                        startActivityForResult(myIntentA1A2, 1122);
                    }
                }

            }
        });


    }

    private void PickDate(){
        final Calendar calender = Calendar.getInstance();
        int date = calender.get(Calendar.DATE);
        int month = calender.get(Calendar.MONTH);
        int year = calender.get(Calendar.YEAR)  ;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calender.set(year,month,dayOfMonth);
                SimpleDateFormat sdp = new SimpleDateFormat("dd/MM/yyyy");
                edit_bd.setText(sdp.format(calender.getTime()));
            }
        },year,month,date);
        datePickerDialog.show();
    }

    private String dateToString(int day,int month, int year) {;
        String d = Integer.toString(day);
        String m = Integer.toString(month);
        String y = Integer.toString(year);
        return d+'/'+m+'/'+y;
    }
}
