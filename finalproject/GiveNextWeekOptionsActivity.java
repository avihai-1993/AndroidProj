package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GiveNextWeekOptionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    public static String DAY = "DAY";
    public static String DAY_NUM = "DAY_NUM";
    private ArrayAdapter<String> arrayAdapter;
    private String [] daysOfweek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_next_week_options);
        listView = (ListView)findViewById(R.id.workDaysForOptions);
        daysOfweek = new String[]{"Sunday","Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daysOfweek);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(this);

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GiveDayShiftsOptionsActivity.class);
        intent.putExtra(DAY,daysOfweek[position]);
        intent.putExtra(DAY_NUM,position);
        startActivity(intent);

    }
}