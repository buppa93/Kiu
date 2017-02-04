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
    // DatabaseListHelperAdapter db;
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
       /* db= new DatabaseListHelperAdapter(getActivity());

        db.open();
        cursor=db.fetchAllContacts();
        adapter= new YourHelpersDbAdapter(getActivity(), cursor);
        listView.setAdapter(adapter);
        db.close();*/
        // Construct the data source
        /*final ArrayList<Helpers> helperses=new ArrayList<Helpers😠);
        helperses.add(new Helpers("ivan scorrano", "via milano", "26/02/2016", "20:30", 3.00, (float)3.0));
        helperses.add(new Helpers("stefano carrino", "via pici", "28/02/2016", "15:30", 1.00, (float)5.0));
        helperses.add(new Helpers("donato tanieli", "via piscieddi", "02/12/2016", "10:00", 3.50,(float) 4.3));
        helperses.add(new Helpers("giuseppe sansone", "via picciotti", "14/08/2016", "20:30", 3.00, (float)1.0));*/

        // Create the adapter to convert the array to views
        // YourHelpersAdapter adapter = new YourHelpersAdapter(this.getContext(), helperses);
        // Attach the adapter to a ListView
        // listView.setAdapter(adapter);
      /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        });*/

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

