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
import com.domain.my.giuseppe.kiu.model.User;
import com.google.firebase.auth.FirebaseAuth;

//import com.domain.my.giuseppe.kiu.utils.Helpers;


public class FragmentAddRate extends Fragment {
    private static final String NAME = "name";
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_addrate, container, false);
        addrate = (ListView) rootView.findViewById(R.id.listaddrate);

        final DatabaseListHelperAdapter db = new DatabaseListHelperAdapter(getContext());

        handler = new Handler();

        //mostro il progress dialog
        progressDialog = new ProgressDialog(FragmentAddRate.this.getActivity());
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
                        adapter = new AddRateDbAdapter(getContext(), cursor);
                        addrate.setAdapter(adapter);
                        addrate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), RateActivity.class);
                                intent.putExtra(NAME, cursor.getString(1));
                                startActivity(intent);
                                db.close();
                            }
                        });

                        //fermo il progress dialog
                        progressDialog.dismiss();

                    }
                });
            }
        };
        thread.start();

        return rootView;
    }
}