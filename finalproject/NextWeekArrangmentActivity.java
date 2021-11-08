package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.DB.DBFireStoreConnector;
import com.example.finalproject.DB.DBMessageInterface;
import com.example.finalproject.DB.NextWeekArrangmentInterface;

public class NextWeekArrangmentActivity extends AppCompatActivity implements NextWeekArrangmentInterface, DBMessageInterface {

    private TextView sundaylogTextView;
    private TextView mondaylogTextView;
    private TextView tuesdaylogTextView;
    private TextView wednesdaylogTextView;
    private TextView thursdaylogTextView;
    private TextView fridaylogTextView;
    private TextView saturdaylogTextView;
    private DBFireStoreConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_week_arrangment);
        connector = new DBFireStoreConnector();
        connector.setNextWeekArrangmentInterface(this);
        connector.setDbMessageInterface(this);

        sundaylogTextView = (TextView) findViewById(R.id.Sunday_next_week_Log);
        mondaylogTextView = (TextView) findViewById(R.id.Monday_next_week_Log);
        tuesdaylogTextView = (TextView) findViewById(R.id.Tuesday_next_week_Log);
        wednesdaylogTextView = (TextView) findViewById(R.id.Wednesday_next_week_Log);
        thursdaylogTextView = (TextView) findViewById(R.id.Thursday_next_week_Log);
        fridaylogTextView = (TextView) findViewById(R.id.Friday_next_week_Log);
        saturdaylogTextView = (TextView) findViewById(R.id.Saturday_next_week_Log);

        connector.getNexWeekArregment();


    }


    @Override
    public void OnGetSunday(String log) {
        String str = sundaylogTextView.getText().toString()+"\n";
        sundaylogTextView.setText(str+log);
    }

    @Override
    public void OnGetMonday(String log) {

        String str = mondaylogTextView.getText().toString()+"\n";
        mondaylogTextView.setText(str+log);
    }

    @Override
    public void OnGetTuesday(String log) {
        String str = tuesdaylogTextView.getText().toString()+"\n";
        tuesdaylogTextView.setText(str+log);
    }

    @Override
    public void OnGetWednesday(String log)
    {
        String str = wednesdaylogTextView.getText().toString()+"\n";
        wednesdaylogTextView.setText(str+log);
    }

    @Override
    public void OnGetThursday(String log) {
        String str = thursdaylogTextView.getText().toString()+"\n";
        thursdaylogTextView.setText(str+log);


    }

    @Override
    public void OnGetFriday(String log) {
        String str = fridaylogTextView.getText().toString()+"\n";
        fridaylogTextView.setText(str+log);

    }

    @Override
    public void OnGetsaturday(String log) {
        String str = saturdaylogTextView.getText().toString()+"\n";
        saturdaylogTextView.setText(str+log);
    }

    @Override
    public void onGetBDMessages(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}