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
import com.domain.my.giuseppe.kiu.model.User;
import com.google.firebase.auth.FirebaseAuth;
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
        rootView = inflater.inflate(R.layout.fragment_yourhelpers, container, false);
        listView = (ListView) rootView.findViewById(R.id.listhelpers);
        final DatabaseListHelperAdapter db = new DatabaseListHelperAdapter(getActivity());

        handler=new Handler();

        //mostro il progress dialog
        progressDialog = new ProgressDialog(FragmentYourHelpers.this.getActivity());
        progressDialog.setMessage("loading...");
        progressDialog.show();

        Thread thread = new Thread() {

            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //recupero i dati degli helper dal database locale
                        db.open();
                        cursor = db.fetchContactsByUsernameProfile(User.getUserName(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                        adapter = new YourHelpersDbAdapter(getActivity(), cursor);
                        listView.setAdapter(adapter);

                        db.close();
                        // fermo il progress dialog
                        progressDialog.dismiss();

                    }
                });
            }
        };
        thread.start();

        return rootView;

    }
}

