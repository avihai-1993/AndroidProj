package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.DB.DBFireStoreConnector;
import com.example.finalproject.DB.DBMessageInterface;

public class ManagerActivity extends AppCompatActivity implements DBMessageInterface {

    Intent intent;
    DBFireStoreConnector dbFireStoreConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        dbFireStoreConnector = new DBFireStoreConnector();
        dbFireStoreConnector.setDbMessageInterface(this);
    }

    public void goToWorkerManagement(View view) {
        intent = new Intent(this, WorkerManagementActivity.class);
        startActivity(intent);
    }



    public void goToShiftPolicyManagement(View view) {
        intent = new Intent(this,ShiftPolicyManagementActivity.class);
        startActivity(intent);
    }

    public void watchThisWeekArrangement(View view) {
        intent = new Intent(this,ThisWeekArrangmentActivity.class);
        startActivity(intent);
    }

    public void watchNextWeekArrangement(View view) {
        intent = new Intent(this,NextWeekArrangmentActivity.class);
        startActivity(intent);
    }

    public void changeWeeks(View view) {
        dbFireStoreConnector.changeWeeks();
    }

    @Override
    public void onGetBDMessages(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}