package com.domain.my.giuseppe.kiu.utils;

/**
 * Created by giuseppe on 17/05/16.
 */
public class MyDate
{
    private String date;
    private String time;
    private static MyDate istance = new MyDate();

    public MyDate() {}

    public MyDate(String date, String time)
    {
        this.date = date;
        this.time = time;
    }

    public String getDate() {return this.date;}
    public String getTime() {return this.time;}
    public static MyDate getIstance() {return istance;}

    public void setDate(String date) {this.date = date;}
    public void setTime(String time) {this.time = time;}

    public String toStringOnlyDate() {return this.date;}
    public String toStringOnlyTime() {return this.time;}
    public String toString() {return this.date+"::"+this.time;}
}
