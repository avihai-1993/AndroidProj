package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WorkerActivity extends AppCompatActivity {

    private Intent intent;
    public static String workerName;
    public static String workerID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        workerName = getIntent().getStringExtra(MainActivity.WORKER_NAME);
        workerID = getIntent().getStringExtra(MainActivity.WORKER_ID);
    }

    public void GiveNextWeekOptions(View view) {
        intent = new Intent(this,GiveNextWeekOptionsActivity.class);
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


}