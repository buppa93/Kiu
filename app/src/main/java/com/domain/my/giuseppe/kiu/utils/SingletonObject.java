package com.domain.my.giuseppe.kiu.utils;

/**
 * Created by giuseppe on 02/01/17.
 */

public class SingletonObject
{
    private static SingletonObject instance;
    private Object obj;

    private SingletonObject() {}

    public static SingletonObject getInstance()
    {
        if(instance == null)
            instance = new SingletonObject();

        return instance;
    }

    public void setObj(Object obj) {this.obj = obj;}

    public Object getObj() {return this.obj;}
}
