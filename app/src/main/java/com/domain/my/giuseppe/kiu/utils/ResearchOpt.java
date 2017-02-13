package com.domain.my.giuseppe.kiu.utils;

/**
 * Created by giuseppe on 20/06/16.
 */
public class ResearchOpt
{
    private static ResearchOpt istance;
    private String date;
    private String time;
    private String rate;
    private String note;
    private String city;


    private ResearchOpt () {}

    public static ResearchOpt getIstance ()
    {
        if(istance == null) {istance = new ResearchOpt();}
        return istance;
    }

    public String getDate () {return this.date;}

    public String getTime () {return this.time;}

    public String getRate () {return this.rate;}

    public String getNote () {return this.note;}

    public String getCity () {return this.city;}

    public void setDate (String date) {this.date = date;}

    public void setTime(String time) {this.time = time;}

    public void setRate(String rate) {this.rate = rate;}

    public void setNote(String note) {this.note = note;}

    public void setCity(String city) {this.city = city;}
}
