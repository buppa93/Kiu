package com.domain.my.giuseppe.kiu.helper;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseListKiuer;

public class FragmentHomeHelper extends Fragment {

    private View root;
    private DatabaseListKiuer db;
    private ListView listView;
    private Cursor cursor;
    private CurrentQueueDbAdapter currentQueueDbAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        root =inflater.inflate(R.layout.fragment_home_helper, container, false);
        db = new DatabaseListKiuer(getActivity());
        listKiuer();
        return root;
    }

    private void listKiuer(){
        listView = (ListView) root.findViewById(R.id.listView);
        //prova
        db.open();
        cursor = db.fetchAcceptedKiuer();
        currentQueueDbAdapter = new CurrentQueueDbAdapter(getActivity(),cursor);
        listView.setAdapter(currentQueueDbAdapter);
        db.close();
    }

}
