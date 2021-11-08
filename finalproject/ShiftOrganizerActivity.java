package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.DB.DBFireStoreConnector;
import com.example.finalproject.DB.DBMessageInterface;
import com.example.finalproject.DB.OrgenazerInteface;
import com.example.finalproject.Logic.Worker;

import java.util.ArrayList;
import java.util.Random;

public class ShiftOrganizerActivity extends AppCompatActivity implements OrgenazerInteface, AdapterView.OnItemClickListener, DBMessageInterface {

    private String nameOfShift;
    private String docIdOfShift;
    private String dayOfShift;
    private ListView currentAssignToNextWeekListView;
    private ListView optiontsOfWorkerToNextWeekListView;
    private TextView currentAssignmetTitle;
    private ArrayAdapter<String> arrayAdapter1;
    private ArrayAdapter<String> arrayAdapter2;
    private DBFireStoreConnector connector;
    private ArrayList<String> workerOptionsData;
    private ArrayList<String> workerNWAssignData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_organizer);
        connector = new DBFireStoreConnector();
        connector.setOrgenazerInteface(this);
        connector.setDbMessageInterface(this);
        dayOfShift = getIntent().getStringExtra(DayShiftPolicyActivity.DAY);
        nameOfShift = getIntent().getStringExtra(DayShiftPolicyActivity.nameOfShift);
        docIdOfShift = getIntent().getStringExtra(DayShiftPolicyActivity.shiftDocId);
        currentAssignToNextWeekListView =(ListView)findViewById(R.id.current_Assign_to_next_Week);
        optiontsOfWorkerToNextWeekListView = (ListView)findViewById(R.id.workers_options_to_shift);
        currentAssignmetTitle = (TextView) findViewById(R.id.current_Assignment);
        currentAssignmetTitle.setText(currentAssignmetTitle.getText().toString()+"\n"+dayOfShift+":"+nameOfShift);
        connector.getWorkerOptions(dayOfShift,docIdOfShift);
        connector.getNextWeekAssignWorkers(dayOfShift,docIdOfShift);

    }



    @Override
    public void OnGetOptions(ArrayList<String> optionWorkers) {

       workerOptionsData = optionWorkers;
       arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,workerOptionsData);
       optiontsOfWorkerToNextWeekListView.setAdapter(arrayAdapter2);
       optiontsOfWorkerToNextWeekListView.setOnItemClickListener(this);
       arrayAdapter2.notifyDataSetChanged();
    }

    @Override
    public void OnGetNWAssignment(ArrayList<String> nextWeekCurrentAssignment) {
        workerNWAssignData = nextWeekCurrentAssignment;
        arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,workerNWAssignData);
        currentAssignToNextWeekListView.setAdapter(arrayAdapter1);
        currentAssignToNextWeekListView.setOnItemClickListener(this);
        arrayAdapter1.notifyDataSetChanged();
    }

    @Override
    public void OnAddedToNWAssigment(String worker) {
        workerNWAssignData.add(worker);
        arrayAdapter1.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent.toString().compareTo(currentAssignToNextWeekListView.toString())==0){
            Worker worker = new Worker(workerNWAssignData.get(position).split("\n")[0],workerNWAssignData.get(position).split("\n")[1]);
            connector.removeWorkerFromNWAssignment(dayOfShift,docIdOfShift,worker);
            workerNWAssignData.remove(position);
        }
        else {
            Worker worker = new Worker(workerOptionsData.get(position).split("\n")[0],workerOptionsData.get(position).split("\n")[1]);
            connector.addWorkerToNWAssignment(dayOfShift,docIdOfShift,worker);

        }

        arrayAdapter1.notifyDataSetChanged();

    }

    @Override
    public void onGetBDMessages(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }
}