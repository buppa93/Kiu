package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseAdapter;
import com.domain.my.giuseppe.kiu.utils.PlaceView;
import com.domain.my.giuseppe.kiu.utils.ResearchOpt;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by giuseppe on 14/07/16.
 */
public class SearchActivity extends AppCompatActivity
{
    public String TAG = "MY APPLICATION";

    private static final String DATE="Date";
    private static final String TIME="Time";
    private static final String NOTE="";
    private static final String LATLNG="LatLng";
    private static final String PLACEDETAILS="placeDetails";
    private static final String PLACENAME="placeName";

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_auto_complete);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            {
                // TODO: Get info about the selected place.
                String placeDetails = place.getAddress().toString();
                String placeName = place.getName().toString();
                LatLng latLng = place.getLatLng();
                String coordinate = latLng.toString();
                DatabaseAdapter dbAdpt = new DatabaseAdapter(getApplicationContext());
                dbAdpt.open();
                ContentValues cv = dbAdpt.createContentValuesNoId(placeName, placeDetails, latLng.latitude,
                        latLng.longitude, 1);
                dbAdpt.updatePlace(cv);
                dbAdpt.close();
                //RICEVO DIMENSIONE RAGGIO DI RICERCA E RIMANDO SULLA MAPPA
                final Intent receive = getIntent();
                int ray = receive.getIntExtra("Ray",99);
                String date = receive.getStringExtra(DATE);
                String time = receive.getStringExtra(TIME);
                Double rate = receive.getDoubleExtra("Rate",99);
                String note = receive.getStringExtra(NOTE);
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra(LATLNG, coordinate);
                intent.putExtra("Ray",ray);
                intent.putExtra(DATE, date);
                intent.putExtra(TIME, time);
                intent.putExtra("Rate", rate);
                intent.putExtra(NOTE, note);
                intent.putExtra(PLACEDETAILS, placeDetails);
                intent.putExtra(PLACENAME, placeName);
                intent.putExtra("Latitude",latLng.latitude);
                intent.putExtra("Longitude",latLng.longitude);

                startActivity(intent);
            }

            @Override
            public void onError(Status status)
            {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        //istanzio i preferiti
        //TODO qui o in search activity?? bisogna recuperare gli input utente sull on click
        listView = (ListView) findViewById(R.id.preferPlaceLW);

        final ArrayList<PlaceView> list;
        list = populateList();

        ArrayList<HashMap<String, Object>> data = generateSrcList(list);

        String[] from = {"title", "address"};
        int[] to = {R.id.itemTitle, R.id.itemBody};

        SimpleAdapter adapter = new SimpleAdapter
                (getApplicationContext(),
                        data,
                        R.layout.my_item_layout,
                        from,
                        to);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //Ricavo la stringa che descrive l'item cliccato
                String itm = parent.getItemAtPosition(position).toString();
                //debugStringToken(itm);

                //Ricavo l'id dal Place dalla stringa che descrive l'item
                int placeId = getPlaceId(itm);

                //Imcremento il campo count nella tabella delle ricerche
                DatabaseAdapter dbAdpt = new DatabaseAdapter(getApplicationContext());
                dbAdpt.open();
                ContentValues cv = dbAdpt.fetchPlacesById(placeId);
                int count = cv.getAsInteger(DatabaseAdapter.KEY_COUNT) + 1;
                cv.put(DatabaseAdapter.KEY_COUNT, count);

                dbAdpt.update(cv);

                dbAdpt.close();

                //Lancio la mappa
                ArrayList<Double> coord = getLatLng(itm);
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                LatLng latLng = new LatLng(coord.get(0), coord.get(1));
                ResearchOpt.getIstance().setCity(getCity(itm));
                //intent.putExtra("Ray", ray);
                intent.putExtra(LATLNG, latLng.toString());
                startActivity(intent);
            }
        });



    }

    @Override
    public void onResume()
    {
        super.onResume();

        //aggiungo eventuali ricerche recenti ai preferiti
        final ArrayList<PlaceView> list;
        list = populateList();

        ArrayList<HashMap<String, Object>> data = generateSrcList(list);

        String[] from = {"title", "address"};
        int[] to = {R.id.itemTitle, R.id.itemBody};

        SimpleAdapter adapter = new SimpleAdapter
                (getApplicationContext(),
                        data,
                        R.layout.my_item_layout,
                        from,
                        to);

        listView.setAdapter(adapter);

        //listView.setAdapter(adapter);

    }


    private ArrayList<PlaceView> populateList ()
    {
        ArrayList<PlaceView> list = new ArrayList<>();
        DatabaseAdapter dbadpt = new DatabaseAdapter(getApplicationContext());
        dbadpt.open();
        Cursor cursor = dbadpt.fetchAllPlaces();

        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseAdapter.KEY_ID));
            String title = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
            String address = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ADDRESS));
            double lat = cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter.KEY_LAT));
            double lng = cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter.KEY_LNG));
            int count = cursor.getInt(cursor.getColumnIndex(DatabaseAdapter.KEY_COUNT));
            list.add(new PlaceView(id, title, address, lat, lng, count));
        }

        cursor.close();
        dbadpt.close();

        //debugArrayListPlace(list);

        return list;
    }

    private ArrayList<HashMap<String, Object>> generateSrcList (ArrayList<PlaceView> list)
    {
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        for(int i=0; i<list.size(); i++)
        {
            PlaceView pv = list.get(i);
            HashMap<String,Object> placeViewMap = new HashMap<>();
            placeViewMap.put("id", pv.getId());
            placeViewMap.put("title", pv.getTitle());
            placeViewMap.put("address", pv.getAddress());
            placeViewMap.put("lat", pv.getLat());
            placeViewMap.put("lng", pv.getLng());
            data.add(placeViewMap);
        }
        //debugArrayHashMap(data);
        return  data;
    }

    private int getPlaceId(String placeDetails)
    {
        StringTokenizer str = new StringTokenizer(placeDetails, "=");


        for(int i=0; i<4; i++) {str.nextToken();}

        int id = Integer.parseInt(stringCleaner(str.nextToken()));

        return id;
    }

    private String stringCleaner(String s)
    {
        String sClean = "";

        for (int i=0; i<s.length(); i++)
        {
            if(((s.charAt(i)>='0')&&(s.charAt(i)<='9'))||(s.charAt(i)=='.'))
            {sClean += s.charAt(i);}
        }
        return sClean;
    }

    private ArrayList<Double> getLatLng(String itm)
    {
        StringTokenizer st = new StringTokenizer(itm, "=");
        ArrayList<Double> coord = new ArrayList<>(2);

        for(int i=0; i<3; i++) {st.nextToken();}
        double lat = Double.parseDouble(stringCleaner(st.nextToken()));
        st.nextToken();
        double lng = Double.parseDouble(stringCleaner(st.nextToken()));

        coord.add(0, lat);
        coord.add(1, lng);

        return coord;
    }

    private String getCity(String itm)
    {
        Log.d(TAG, "ITEM -> " + itm);
        StringTokenizer stringTokenizer = new StringTokenizer(itm, "=");

        for(int i=0; i<1; i++) {stringTokenizer.nextToken();}
        String addressString = stringTokenizer.nextToken();
        Log.d(TAG, "addressString -> " + addressString);

        StringTokenizer tokenizerAddress = new StringTokenizer(addressString, ",");
        for(int i=0; i<2; i++) {tokenizerAddress.nextToken();}
        String completeCityInfo = tokenizerAddress.nextToken();
        Log.d(TAG, "completeCityInfo -> " + addressString);

        StringTokenizer tokenizerCity = new StringTokenizer(completeCityInfo, " ");
        for(int i=0; i<1; i++) {tokenizerCity.nextToken();}
        String city = tokenizerCity.nextToken();
        Log.d(TAG, "city -> " + addressString);

        return city;
    }


}
