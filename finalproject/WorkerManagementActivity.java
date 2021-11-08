package com.example.finalproject;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.DB.DBFireStoreConnector;
import com.example.finalproject.DB.DBMessageInterface;
import com.example.finalproject.DB.WorkerMangementInterface;
import com.example.finalproject.Fragments.AddWorkerInputFragment;
import com.example.finalproject.Fragments.OnDone;
import com.example.finalproject.Logic.Worker;
import com.example.finalproject.Views.WorkersAdapter;

import java.util.ArrayList;

public class WorkerManagementActivity extends AppCompatActivity implements WorkerMangementInterface, View.OnClickListener, AdapterView.OnItemLongClickListener, DBMessageInterface {


   private ListView listView;
   private Button addbtn;
   private DBFireStoreConnector dbFireStoreConnector;
   private ArrayList<Worker> workerArrayList;
   private WorkersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_management);
        dbFireStoreConnector = new DBFireStoreConnector();
        dbFireStoreConnector.setWorkerMangementInterface(this);
        dbFireStoreConnector.setDbMessageInterface(this);
        dbFireStoreConnector.getAllWorkers();

    }





    @Override
    public void OnGetWorkersList(ArrayList<Worker> workers) {
        workerArrayList = workers;
        listView = (ListView) findViewById(R.id.workersList);
        adapter = new WorkersAdapter(this, workerArrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //delete
        listView.setOnItemLongClickListener(WorkerManagementActivity.this);
        addbtn = (Button) findViewById(R.id.addWorkerBtn);
        addbtn.setOnClickListener(this);

    }

    @Override
    public void OnWorkerAdded(Worker worker) {
        workerArrayList.add(worker);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        final AddWorkerInputFragment addWorkerInputFragment = new AddWorkerInputFragment();
        addWorkerInputFragment.setOnDone(new OnDone() {
            @Override
            public void onDone(String input) {
                if(input.trim().compareTo(",") == 0){
                    Toast.makeText(WorkerManagementActivity.this,"EMPTY INPUT",Toast.LENGTH_LONG).show();
                }else{
                    Worker worker = new Worker(input.split(",")[0],input.split(",")[1]);
                    dbFireStoreConnector.addWorker(worker);
                }
                addWorkerInputFragment.dismiss();
            }
        });
        addWorkerInputFragment.show(getSupportFragmentManager(),"");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        dbFireStoreConnector.deleteWorker(workerArrayList.get(position));
        workerArrayList.remove(position);
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onGetBDMessages(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}