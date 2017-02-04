package com.domain.my.giuseppe.kiu.service;

import android.app.Service;
import android.util.Log;

public abstract class MyCustomService extends Service
{
    private static final String TAG = "TAG";
    private int numTasks = 0;

    public void taskStarted() {changeNumberOfTasks(1);}

    public void taskCompleted() {changeNumberOfTasks(-1);}

    private synchronized void changeNumberOfTasks(int delta)
    {
        Log.d(TAG, "changeNumberOfTasks: " + numTasks + " : " + delta);
        numTasks += delta;

        if(numTasks <= 0)
        {
            Log.d(TAG, "stopping");
            stopSelf();
        }
    }


}
