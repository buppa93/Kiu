package com.domain.my.giuseppe.kiu.utils;

/**
 * Created by giuseppe on 21/06/16.
 */
public class PlaceView
{
    private int id;
    private String title;
    private String address;
    private double lat;
    private double lng;
    private int count;

    public PlaceView()
    {
        this.id = 0;
        this.title = "";
        this.address = "";
        this.lat = 0;
        this.lng = 0;
        count = 0;
    }

    public PlaceView (int id, String title, String address, double lat, double lng, int count)
    {
        this.id = id;
        this.title = title;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.count = count;
    }

    public PlaceView (String address)
    {this.address = address;}

    public int getId()
    {return this.id;}

    public String getTitle()
    {return this.title;}

    public String getAddress()
    {return this.address;}

    public double getLat()
    {return this.lat;}

    public double getLng()
    {return this.lng;}

    public int getCount()
    {return this.count;}

    public void setId(int id)
    {this.id = id;}

    public void setTitle(String title)
    {this.title = title;}

    public void setAddress(String address)
    {this.address = address;}

    public void setLat(double lat)
    {this.lat = lat;}

    public void setLng(double lng)
    {this.lng = lng;}

    public void setCount(int count)
    {this.count = count;}

    public boolean isEmpty()
    {
        if((this.getId()==0)&&(this.getTitle().equals(""))&&(this.getAddress().equals(""))&&(this.getLat()==0)&&
                (this.getLng()==0)&&(this.getCount()==0))
            return true;
        else
            return false;
    }
}
