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
import com.example.finalproject.DB.ManagerShiftPolicyMangmentInterface;
import com.example.finalproject.DB.ShiftPolicyMangmentInterface;
import com.example.finalproject.Fragments.AddShiftInputFragment;
import com.example.finalproject.Fragments.AddWorkerInputFragment;
import com.example.finalproject.Fragments.OnDone;
import com.example.finalproject.Logic.Shift;

import java.util.ArrayList;

public class DayShiftPolicyActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, DBMessageInterface, ManagerShiftPolicyMangmentInterface {

    private ListView listView;
    private String day;
    private int dayNum;
    private DBFireStoreConnector connector;
    private ArrayList <String> shiftSOfday;
    private ArrayList <String> shiftsDocId;
    private ArrayAdapter<String> arrayAdapter;
    public static final String DAY = "day";
    public static final String shiftDocId = "shiftDocyomentId";
    public static final String nameOfShift = "nameOfShift";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_shift_policy);
        connector = new DBFireStoreConnector();
        day =  getIntent().getStringExtra(ShiftPolicyManagementActivity.DAY);
        dayNum =  getIntent().getIntExtra(ShiftPolicyManagementActivity.DAY_NUM,0);
        listView = (ListView)findViewById(R.id.DayShifts);
        connector.setManagerShiftPolicyMangmentInterface(this);
        connector.setDbMessageInterface(this);
        connector.getAllShiftPolicyByDay(day);






    }

    public void addShift(View view) {
        final AddShiftInputFragment addShiftInputFragment = new AddShiftInputFragment();
        addShiftInputFragment.setOnDone(new OnDone() {
            @Override
            public void onDone(String input) {
                Toast.makeText(DayShiftPolicyActivity.this,input,Toast.LENGTH_LONG).show();
                connector.addShiftToPolicy(day,new Shift(input.split(",")[0],input.split(",")[1]));
                addShiftInputFragment.dismiss();
            }
        });
        addShiftInputFragment.show(getSupportFragmentManager(),"");
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
    public void onShiftAdd(Shift shift, String shiftDocId) {
        shiftSOfday.add(shift.toString());
        shiftsDocId.add(shiftDocId);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        connector.deleteShiftFromPolicy(day,shiftsDocId.get(position));
        shiftSOfday.remove(position);
        shiftsDocId.remove(position);
        arrayAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ShiftOrganizerActivity.class);
        intent.putExtra(DAY,day);
        intent.putExtra(nameOfShift,shiftSOfday.get(position).split("\n")[0]);
        intent.putExtra(shiftDocId,shiftsDocId.get(position));
        startActivity(intent);
    }

    @Override
    public void onGetBDMessages(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}

