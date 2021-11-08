package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finalproject.DB.DBFireStoreConnector;
import com.example.finalproject.DB.DBMessageInterface;

public class ShiftPolicyManagementActivity extends AppCompatActivity implements DBMessageInterface {

    private ListView listView;
    public static String DAY = "DAY";
    public static String DAY_NUM = "DAY_NUM";
    private DBFireStoreConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_policy_management);
        listView = (ListView)findViewById(R.id.workDays);
        connector = new DBFireStoreConnector();
        connector.setDbMessageInterface(this);
        final String [] daysOfweek =  {"Sunday","Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daysOfweek);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShiftPolicyManagementActivity.this, DayShiftPolicyActivity.class);
                intent.putExtra(DAY,daysOfweek[position]);
                intent.putExtra(DAY_NUM,position);
                startActivity(intent);
            }
        });

    }

    public void autoAssiment(View view) {
       connector.makeAutoAssimentForNWFromOptions();
    }

    @Override
    public void onGetBDMessages(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

    }
}