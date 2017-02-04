package com.domain.my.giuseppe.kiu.helper;

/**
 * Created by UserOLD on 17/06/2016.
 */
public class Contact {

    private String contact;
    private double distance;
    private String date;
    private double rate;
    private double feedback;
    private String location;


    public Contact(String contact, double distance, String date, double rate, double feedback) {
        super();
        this.contact=contact;
        this.distance=distance;
        this.date=date;
        this.rate=rate;
        this.feedback=feedback;
    }

    public Contact(String contact,String location){
        this.contact = contact;
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }


    public double getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public double getFeedback() {
        return feedback;
    }

    public void setFeedback(float feedback) {
        this.feedback = feedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}