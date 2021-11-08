package com.example.finalproject.DB;

public interface EmploeeValidtionListener {

    void onFailedManagerValidtion();
    void onSuccessManagerValidtion();
    void onSuccessWorkerValidtion();
    void onFailedWorkerValidtion();

}
