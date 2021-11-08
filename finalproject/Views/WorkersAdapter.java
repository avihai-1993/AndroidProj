package com.example.finalproject.Views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;

import com.example.finalproject.Logic.Worker;

import java.util.List;

public class WorkersAdapter  extends BaseAdapter {

    private List<Worker> workers;
    private Context context;


    public WorkersAdapter(@NonNull Context context, @NonNull List<Worker> objects) {

        this.workers = objects;
        this.context = context;

    }

    @Override
    public int getCount() {
        return workers.size();
    }

    @Override
    public Object getItem(int position) {

        return workers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WorkerView workerView=(WorkerView) convertView;

        if (workerView == null) {

            workerView = new WorkerView(context);
        }
       Worker w = workers.get(position);
       workerView.setText(w.getName() +"\n" + w.getId());



        return workerView;
    }


}
