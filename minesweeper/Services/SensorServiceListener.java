package com.example.minesweeper.Services;
public interface SensorServiceListener {

    enum ALARM_STATE {
        ON,OFF
    }

    void alarmStateChanged(ALARM_STATE state);
}