package com.example.minesweeper.Logic;

import java.util.Date;

public class Record implements Comparable {

    private  String name ;
    private  int score;
    private  String time;
    private  String level;
    private int timeInSec;

    public Record(String name,int score , String time,String level,int timeInSec){
        this.name = name;
        this.score = score;
        this.time = time;
        this.level = level;
        this.timeInSec = timeInSec;




    }

    public Record (String recordFormatString){
        String [] record = recordFormatString.split(",");
        try {
            this.name = record[0];
            this.score = Integer.parseInt(record[1]);;
            this.time = record[2];
            this.level = record[3];
            this.timeInSec =Integer.parseInt(record[4]);
        }catch (Exception e){
            e.printStackTrace();

        }

    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name+","+score+","+time+","+level+","+timeInSec;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Record){
            if(timeInSec < ((Record) o).timeInSec){
                return 1;
            }else if(timeInSec > ((Record) o).timeInSec){
                return -1;
            }else {
                if(score > ((Record) o).score){
                    return -1;
                }
                else if (score < ((Record) o).score){
                    return 1;
                }
                else {
                    return 0;
                }

            }
        }
        else {
            return 1;
        }

    }
}
