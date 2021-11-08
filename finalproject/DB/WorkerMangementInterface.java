package com.example.finalproject.DB;

import com.example.finalproject.Logic.Worker;

import java.util.ArrayList;

public interface WorkerMangementInterface {


    void OnGetWorkersList(ArrayList<Worker> workers);
    void OnWorkerAdded(Worker worker);



}
