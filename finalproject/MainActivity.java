package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.finalproject.DB.DBFireStoreConnector;
import com.example.finalproject.DB.DBMessageInterface;
import com.example.finalproject.DB.EmploeeValidtionListener;
import com.example.finalproject.Logic.ShiftManager;
import com.example.finalproject.Logic.Worker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements EmploeeValidtionListener, DBMessageInterface {

    private EditText userNameEditText;
    private EditText userPasswordEditText;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RadioGroup loginRadiGroup;
    private DBFireStoreConnector connector;
    public static final String WORKER_NAME = "workerName";
    public static final String WORKER_ID = "workerID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connector = new DBFireStoreConnector();
        sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userNameEditText = (EditText)findViewById(R.id.User_Name_EditText);
        userPasswordEditText = (EditText)findViewById(R.id.password_EditText);
        loginRadiGroup = (RadioGroup)findViewById(R.id.loginAsRadioGroup);
        loginRadiGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(R.id.workerLoginRadioButton == checkedId){
                    editor.putString(getString(R.string.Rule),getString(R.string.isWorker));
                }
                else if(R.id.managerLoginRadioButton == checkedId){
                    editor.putString(getString(R.string.Rule),getString(R.string.isManager));
                }
                editor.commit();
            }
        });

        readPref();

        connector.setEmploeeValidtionListener(this);
        connector.setDbMessageInterface(this);



    }

    public void login(View view)
    {
            if(R.id.workerLoginRadioButton == loginRadiGroup.getCheckedRadioButtonId()){
                connector.valideteWorker(new Worker(getUserName(),getPassword()));
            }
            else if(R.id.managerLoginRadioButton == loginRadiGroup.getCheckedRadioButtonId())
            {
                connector.valideteManeger(new ShiftManager(getUserName(),getPassword()));

            }
            else{
                Toast.makeText(this,"no rule picked",Toast.LENGTH_LONG).show();
            }



    }





    private void savePref(String user , String password  ){
        editor.putString(getString(R.string.userName),user);
        editor.putString(getString(R.string.password),password);

        if(R.id.workerLoginRadioButton == loginRadiGroup.getCheckedRadioButtonId()){
            editor.putString(getString(R.string.Rule),getString(R.string.isWorker));
        }
        else if(R.id.managerLoginRadioButton == loginRadiGroup.getCheckedRadioButtonId()){
            editor.putString(getString(R.string.Rule),getString(R.string.isManager));
        }
        editor.commit();

    }

    private void readPref(){
        userNameEditText.setText(sharedPreferences.getString(getString(R.string.userName),""));
        userPasswordEditText.setText(sharedPreferences.getString(getString(R.string.password),""));
        if(sharedPreferences.getString(getString(R.string.Rule),"").compareTo(getString(R.string.isManager)) == 0 ){
            loginRadiGroup.check(R.id.managerLoginRadioButton);
        }
        else if (sharedPreferences.getString(getString(R.string.Rule),"").compareTo(getString(R.string.isWorker)) == 0 ){
            loginRadiGroup.check(R.id.workerLoginRadioButton);
        }

    }

    private String getUserName(){
        return userNameEditText.getText().toString();
    }
    private String getPassword(){
        return userPasswordEditText.getText().toString();
    }

    @Override
    public void onFailedManagerValidtion() {
        Toast.makeText(this,getString(R.string.WorngLogin),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessManagerValidtion() {
            savePref(getUserName(),getPassword());
            Intent intent = new Intent(this,ManagerActivity.class);
            startActivity(intent);
            finish();

    }

    @Override
    public void onSuccessWorkerValidtion() {
        savePref(getUserName(),getPassword());
        Intent intent = new Intent(this,WorkerActivity.class);
        intent.putExtra(WORKER_NAME,getUserName());
        intent.putExtra(WORKER_ID,getPassword());
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailedWorkerValidtion() {
        Toast.makeText(this, getString(R.string.WorngLogin),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetBDMessages(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}


