//package universit.ivasco92.kiu;
package com.domain.my.giuseppe.kiu.kiuwer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseListHelperAdapter;

//import com.domain.my.giuseppe.kiu.utils.Helpers;


public class FragmentAddRate extends Fragment {
    private static final String NAME="name";
    private static final String PROFILE_IMG = "profileImg";
    private static final String THUMBNAIL = "thumbnail";
    View rootView;
    ListView addrate;
    Cursor cursor;
    AddRateDbAdapter adapter;
    ProgressDialog progressDialog;
    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_addrate, container, false);
        addrate= (ListView) rootView.findViewById(R.id.listaddrate);
        final DatabaseListHelperAdapter db= new DatabaseListHelperAdapter(getContext());

        // display the progressbar on the screen
        handler=new Handler();
        progressDialog = new ProgressDialog(FragmentAddRate.this.getActivity());
        progressDialog.setMessage("loading...");
        progressDialog.show();


        Thread thread = new Thread() {

            public void run() {

                // Now we are on a different thread than UI thread
                // and we would like to update our UI, as this task is completed
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        //do any Post job after the time consuming task
                        db.open();
                        cursor=db.fetchAllContacts();
                        adapter= new AddRateDbAdapter(getContext(), cursor);
                        addrate.setAdapter(adapter);
                        addrate.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Intent intent=new Intent(getActivity(),RateActivity.class);
                                intent.putExtra(NAME,cursor.getString(1));
                                startActivity(intent);
                                db.close();
                            }
                        });

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