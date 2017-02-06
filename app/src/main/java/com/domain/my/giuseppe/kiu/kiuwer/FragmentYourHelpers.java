//package universit.ivasco92.kiu;
package com.domain.my.giuseppe.kiu.kiuwer;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseListHelperAdapter;

//import com.domain.my.giuseppe.kiu.utils.Helpers;

public class FragmentYourHelpers extends Fragment
{
    View rootView;
    ListView listView;
    Cursor cursor;
    YourHelpersDbAdapter adapter;
    Handler handler;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_yourhelpers, container, false);
        listView = (ListView) rootView.findViewById(R.id.listhelpers);
        final DatabaseListHelperAdapter db = new DatabaseListHelperAdapter(getActivity());

        handler=new Handler();
        progressDialog = new ProgressDialog(FragmentYourHelpers.this.getActivity());
        progressDialog.setMessage("loading...");
        progressDialog.show();

        // start the time consuming task in a new thread
        Thread thread = new Thread() {

            public void run() {

                // Now we are on a different thread than UI thread
                // and we would like to update our UI, as this task is completed
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        // Update your UI or do any Post job after the time consuming task
                        //  updateUI();

                        db.open();
                        cursor = db.fetchAllContacts();
                        adapter = new YourHelpersDbAdapter(getActivity(), cursor);
                        listView.setAdapter(adapter);

                        db.close();
                        // remember to dismiss the progress dialog on UI thread
                        progressDialog.dismiss();

                    }
                });
            }
        };
        thread.start();

        return rootView;

    }
}

