package com.example.finalproject.Logic;

public class Shift {


    private String kindOfShift;
    private String timeFrame;


    public Shift(String kindOfShift, String timeFrame) {
      setKindOfShift(kindOfShift);
      setTimeFrame(timeFrame);


    }

    public String getKindOfShift() {
        return kindOfShift;
    }

    public void setKindOfShift(String kindOfShift) {
        this.kindOfShift = kindOfShift;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }



    @Override
    public String toString() {
        return kindOfShift+"\n"+ timeFrame;

    }
}
