package com.domain.my.giuseppe.kiu.helper;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseListKiuer;
import com.domain.my.giuseppe.kiu.notification.ShowRequestDetailActivity;


public class FragmentYourKiuwer extends Fragment {

    private View root;
    private DatabaseListKiuer db;
    private ListView listView;
    private Cursor cursor;
    private YourKiuerDbAdapter yourKiuerDbAdapter;
    public static final String KEY_ID = "id_kiuer";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_FEEDBACK = "feedback";
    public static final String KEY_LOCAL_PLACE = "local_place";
    public static final String KEY_LOCAL_ADDRESS = "local_address";
    public static final String KEY_LOCAL_DATE = "local_date";
    public static final String KEY_LOCAL_TIME = "local_time";
    public static final String KEY_PRICE = "price";
    public static final String KEY_NOTE = "note";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_VISIBILITY = "visibility";
    public static final String KEY_ACCEPTED = "accepted";

    private static final String LOCALDATA = "localdata";
    private static final String ID = "id";
    private static final String LOCALTIME = "localtime";
    private static final String PLACENAME = "placename";
    private static final String PLACEADDRESS = "placeaddress";
    private static final String NOTE = "note";
    private static final String PRICE = "price";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String KIUERTOKEN = "kiuertoken";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_your_kiuwer, container, false);
        db = new DatabaseListKiuer(getActivity());
        listKiuer();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(pos);
                Intent intent = new Intent(getActivity(),ShowRequestDetailActivity.class);
                intent.putExtra(KEY_ID,cursor.getString(cursor.getColumnIndex(ID)));
                intent.putExtra(ID,2);
                intent.putExtra(KIUERTOKEN,cursor.getString(cursor.getColumnIndex(KEY_TOKEN)));
                intent.putExtra(PLACENAME,cursor.getString(cursor.getColumnIndex(KEY_LOCAL_PLACE)));
                intent.putExtra(PLACEADDRESS,cursor.getString(cursor.getColumnIndex(KEY_LOCAL_ADDRESS)));
                intent.putExtra(LOCALDATA,cursor.getString(cursor.getColumnIndex(KEY_LOCAL_DATE)));
                intent.putExtra(LOCALTIME,cursor.getString(cursor.getColumnIndex(KEY_LOCAL_TIME)));
                intent.putExtra(PRICE,cursor.getString(cursor.getColumnIndex(KEY_PRICE)));
                //intent.putExtra(KEY_FEEDBACK,cursor.getFloat(cursor.getColumnIndex(KEY_FEEDBACK)));
                intent.putExtra(NOTE,cursor.getString(cursor.getColumnIndex(KEY_NOTE)));
                intent.putExtra(LATITUDE,cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                intent.putExtra(LONGITUDE,cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                view.setBackgroundColor(Color.TRANSPARENT);
                startActivity(intent);

            }
        });

        return root;
    }

    private void listKiuer(){
        listView = (ListView) root.findViewById(R.id.listView2);
        //prova
        db.open();
        cursor = db.fetchAllKiuer();
        yourKiuerDbAdapter = new YourKiuerDbAdapter(getActivity(),cursor);
        listView.setAdapter(yourKiuerDbAdapter);
        db.close();
    }



}
