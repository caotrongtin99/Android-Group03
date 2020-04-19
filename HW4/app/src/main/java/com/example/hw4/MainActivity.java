package com.example.hw4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Customer> customers = getListData();
        final ListView listView = (ListView) findViewById(R.id.listView_customers);
        final TextView textViewSelection = (TextView) findViewById(R.id.textView_selection) ;
        listView.setAdapter(new ListCustomerAdapter(this, customers));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Customer customer = (Customer) o;
                textViewSelection.setText("You choose:"+ customer.getFullName());
            }
        });
    }

    private  List<Customer> getListData() {
        List<Customer> list = new ArrayList<Customer>();
        Customer cao_trong_tin = new Customer("Cao Trong Tin", "0909090909", "user");
        Customer  nguyen_huu_tuan= new Customer("Nguyen Huu Tuan", "01234572821", "user1");
        Customer le_tan_thinh = new Customer("Le Tan Thinh", "09728127212", "user2");
        Customer ngo_viet_thang = new Customer("Ngo Viet Thang", "09728127212", "user3");
        Customer ha_lu = new Customer("Ha Lu", "09728127212", "user4");


        list.add(cao_trong_tin);
        list.add(nguyen_huu_tuan);
        list.add(le_tan_thinh);
        list.add(ngo_viet_thang);
        list.add(ha_lu);

        return list;
    }
}
