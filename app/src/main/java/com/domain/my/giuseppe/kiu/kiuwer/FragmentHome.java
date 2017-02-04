//package universit.ivasco92.kiu;
package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseAdapter;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.domain.my.giuseppe.kiu.signin.SignInActivity;
import com.domain.my.giuseppe.kiu.utils.DatePickerFragment;
import com.domain.my.giuseppe.kiu.utils.PlaceView;
import com.domain.my.giuseppe.kiu.utils.ResearchOpt;
import com.domain.my.giuseppe.kiu.utils.TimePickerFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class FragmentHome extends Fragment
{
    EditText dateED;
    EditText timeED;
    EditText rateED;
    EditText noteED;
    //ListView listView;
    View rootView;
    public int ray = 0;
    Spinner spinner;
    SupportPlaceAutocompleteFragment searchBar;
    FloatingActionButton searchFab;

    /* Attributi del luogo */
    String placeName;
    String placeAddress;
    LatLng coordinates;

    private static final String DATE="Date";
    private static final String TIME="Time";
    private static final String NOTE="";
    private static final String LATLNG="LatLng";

    public static final String TAG = "Fragment Home";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.content_home_layout, container, false);

        //inizializzo la data
        dateED = (EditText) rootView.findViewById(R.id.dateED);
        dateED.setText(getCurrentData());
        dateED.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerFragment dialog = new DatePickerFragment(dateED);
                dialog.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });

        //inizializzo l'ora
        timeED = (EditText) rootView.findViewById(R.id.timeED);
        timeED.setText(getCurrentTime());
        timeED.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerFragment dialog = new TimePickerFragment(timeED);
                dialog.show(getActivity().getSupportFragmentManager(), "TimePicker");
            }
        });

        rateED = (EditText) rootView.findViewById(R.id.rateED);
        noteED = (EditText) rootView.findViewById(R.id.noteED);

        //istanzio lo spinner per il raggio di ricerca
        spinner = (Spinner) rootView.findViewById(R.id.rayspinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.ray_array, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                parent.getContext();
                ray = Integer.parseInt(parent.getItemAtPosition(pos).toString());
                // ray=parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchBar = new SupportPlaceAutocompleteFragment();
        getFragmentManager().beginTransaction().replace(R.id.serachAutocompleteContainer,
                searchBar).commit();

        searchBar.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            {
                placeName = place.getName().toString();
                placeAddress = place.getAddress().toString();
                coordinates = place.getLatLng();
                searchBar.setText(placeName);
            }

            @Override
            public void onError(Status status) {}
        });

        searchFab = (FloatingActionButton) rootView.findViewById(R.id.searchFab);
        searchFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (validateForm())
                {
                    Intent mapIntent = new Intent(getContext(), MapActivity.class);
                    mapIntent.putExtra("coordinates", coordinates.toString());
                    mapIntent.putExtra("time", timeED.getText().toString());
                    mapIntent.putExtra("date", dateED.getText().toString());
                    mapIntent.putExtra("payment", rateED.getText().toString());
                    mapIntent.putExtra("ray", ray);
                    if (noteED.getText().equals(""))
                        mapIntent.putExtra("note", "N/D");
                    else
                        mapIntent.putExtra("note", noteED.getText().toString());
                    mapIntent.putExtra("placeName", placeName.toString());
                    mapIntent.putExtra("placeDetails", placeAddress).toString();
                    startActivity(mapIntent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    clearBackStack();
                    Intent intent = new Intent(getContext(), SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        } );

        Log.d(TAG, "MyToken -> " + FirebaseInstanceId.getInstance().getToken());

    }

    private void clearBackStack()
    {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private String getCurrentData()
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day+"/"+month+"/"+year;
    }

    private String getCurrentTime()
    {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return hour+":"+minute;
    }

    private boolean validateForm()
    {
        boolean valid = true;
        String required = getResources().getString(R.string.required);

        String date = dateED.getText().toString();
        if(TextUtils.isEmpty(date))
        {
            dateED.setError(required);
            valid = false;
        }
        else {dateED.setError(null);}

        String time = timeED.getText().toString();
        if(TextUtils.isEmpty(time))
        {
            timeED.setError(required);
            valid = false;
        }
        else {timeED.setError(null);}

        String rate = rateED.getText().toString();
        if(TextUtils.isEmpty(rate))
        {
            rateED.setError(required);
            valid = false;
        }
        else {rateED.setError(null);}

        return valid;
    }

}
