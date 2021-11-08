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
import com.example.finalproject.DB.ShiftPolicyMangmentInterface;
import com.example.finalproject.Fragments.AddShiftInputFragment;
import com.example.finalproject.Fragments.OnDone;
import com.example.finalproject.Logic.Shift;

import java.util.ArrayList;

public class GiveDayShiftsOptionsActivity extends AppCompatActivity implements ShiftPolicyMangmentInterface, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, DBMessageInterface {

    private ListView listView;
    private String day;
    private int dayNum;
    private DBFireStoreConnector connector;
    private ArrayList<String> shiftSOfday;
    private ArrayList <String> shiftsDocId;
    private ArrayAdapter<String> arrayAdapter;
    public static final String DAY = "day";
    public static final String shiftDocId = "shiftDocyomentId";
    public static final String nameOfShift = "nameOfShift";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_day_shifts_options);
        connector = new DBFireStoreConnector();
        day =  getIntent().getStringExtra(GiveNextWeekOptionsActivity.DAY);
        dayNum =  getIntent().getIntExtra(GiveNextWeekOptionsActivity.DAY_NUM,0);
        listView = (ListView)findViewById(R.id.DayShiftsOptions);
        connector.setShiftPolicyMangmentInterface(this);
        connector.setDbMessageInterface(this);
        connector.getAllShiftPolicyByDay(day);
        Toast.makeText(this,WorkerActivity.workerName,Toast.LENGTH_LONG).show();


    }

    @Override
    public void OnGetAllShiftOfDay(ArrayList<String> stringArrayList,ArrayList<String> stringArrayListId)
    {

        shiftSOfday = stringArrayList;
        shiftsDocId = stringArrayListId;
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,shiftSOfday);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        connector.removeWorkerFromNWOptions(day,shiftsDocId.get(position),WorkerActivity.workerName,WorkerActivity.workerID);
        view.setBackground(getDrawable(R.color.red));
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


       connector.addWorkerToNWOptions(day,shiftsDocId.get(position),WorkerActivity.workerName,WorkerActivity.workerID);
       view.setBackground(getDrawable(R.color.green));

    }

    @Override
    public void onGetBDMessages(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}