package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        TextView text_user = (TextView) findViewById(R.id.username);
        TextView text_password = (TextView) findViewById(R.id.password);
        TextView text_birthday = (TextView) findViewById(R.id.birthday);
        TextView text_gender = (TextView) findViewById(R.id.gender);
        TextView text_hobbies = (TextView) findViewById(R.id.hobbies);
        Button btn_exit = (Button) findViewById(R.id.btn_exit);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        Intent myCallerIntent;
        myCallerIntent = getIntent();
        Bundle myBundle = myCallerIntent.getExtras();

        String username = myBundle.getString("username");
        String password = myBundle.getString("password");
        String birthday = myBundle.getString("birthday");
        String gender = myBundle.getString("gender");
        String hobbies = myBundle.getString("hobbies");


        text_user.setText(username);
        String password_hided = "";
        for (int i = 0; i < password.length();i++){
            password_hided+="*";
        }
        text_password.setText(password_hided);
        text_birthday.setText(birthday);
        text_gender.setText(gender);
        text_hobbies.setText(hobbies);




    }
}
