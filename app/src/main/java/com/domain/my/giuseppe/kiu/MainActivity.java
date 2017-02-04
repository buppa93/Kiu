package com.domain.my.giuseppe.kiu;

import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
       /* implements NavigationView.OnNavigationItemSelectedListener, PlaceSelectionListener*/ {

    /*EditText dateED;
    EditText timeED;
    EditText rateED;
    EditText noteED;
    ImageButton dateBttn;
    ImageButton timeBttn;
    ListView listView;
    public static final String TAG = "MY_APPLICATION";

    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setto la data
        /*dateED = (EditText) findViewById(R.id.dateED);
        dateED.setText(getCurrentData());

        dateBttn = (ImageButton) findViewById(R.id.dateBttn);
        dateBttn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerFragment dialog = new DatePickerFragment(dateED);
                dialog.show(getSupportFragmentManager(), "DatePicker");
            }
        });*/

    //setto l'ora
        /*timeED = (EditText) findViewById(R.id.timeED);
        timeED.setText(getCurrentTime());

        timeBttn =(ImageButton) findViewById(R.id.timeBttn);
        timeBttn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerFragment dialog = new TimePickerFragment(timeED);
                dialog.show(getSupportFragmentManager(), "TimePicker");
            }
        });

        rateED = (EditText) findViewById(R.id.rateED);
        noteED = (EditText) findViewById(R.id.noteED);*/

    //fragment search
        /*PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint(getResources().getString(R.string.searchHintStr));

        autocompleteFragment.setOnPlaceSelectedListener(this);

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
                //TODO ricerca alla zozza maniera proprio
                Log.d(TAG, "Clicked*******************: "+parent.getItemAtPosition(position).toString());
                String itm = parent.getItemAtPosition(position).toString();
                ArrayList<Double> coord = getLatLng(itm);
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                LatLng latLng = new LatLng(coord.get(0), coord.get(1));
                intent.putExtra("LatLng", latLng.toString());
                startActivity(intent);
            }
        });*/
    //}

    /*@Override
    public void onResume()
    {
        super.onResume();

        /*final ArrayList<PlaceView> list;
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

        listView.setAdapter(adapter);*/

    //}

   /* @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        if (id == R.id.home) {
            /*setTitle(R.string.userkiuer);
            fragment= new fragmenthome();
            ft.replace(R.id.content, fragment);
            ft.commit();*/
      /*  } if (id == R.id.helpers) {
            setTitle(R.string.your_helpers);
            fragment= new FragmentYourHelpers();
            ft.replace(R.id.content_, fragment); //R.id.content_main
            ft.commit();
        }  if (id == R.id.addrate) {
            setTitle(R.string.rate);
            fragment= new FragmentAddRate();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }  if (id == R.id.feedbackview) {
            setTitle(R.string.showfeedback);
            fragment=new FragmentsShowFeedback();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        } if (id == R.id.log_out) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.logout)
                    .setMessage(R.string.dialogmessage).setIcon(R.drawable.ic_logout)
                    .setPositiveButton(R.string.confirmbutton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(R.string.cancelbutton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public String getCurrentData()
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day+"/"+month+"/"+year;
    }

    public String getCurrentTime()
    {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return hour+":"+minute;
    }

    @Override
    public void onPlaceSelected(Place place)
    {
        /*Log.v(TAG, "Place Selected: " + place.getName());
        Log.v(TAG, "Place Selected: " + place.getAddress());
        String placeDetails = place.getAddress().toString();
        String placeName = place.getName().toString();
        LatLng latLng = place.getLatLng();
        String coordinate = latLng.toString();
        Log.v(TAG, "Place Selected: " + coordinate);
        DatabaseAdapter dbAdpt = new DatabaseAdapter(this);
        dbAdpt.open();
        dbAdpt.createPlace(placeName, placeDetails, latLng.latitude, latLng.longitude);
        dbAdpt.close();

        ResearchOpt.getIstance().setDate(dateED.getText().toString());
        ResearchOpt.getIstance().setTime(timeED.getText().toString());
        ResearchOpt.getIstance().setRate(rateED.getText().toString());
        ResearchOpt.getIstance().setNote(noteED.getText().toString());

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("LatLng", coordinate);
        startActivity(intent);*/
    //}

   /* @Override
    public void onError(Status status)
    {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    public ArrayList<PlaceView> populateList ()
    {
        ArrayList<PlaceView> list = new ArrayList<>();
        /*DatabaseAdapter dbadpt = new DatabaseAdapter(this);
        dbadpt.open();
        Cursor cursor = dbadpt.fetchAllPlaces();

        while (cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
            String address = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ADDRESS));
            double lat = cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter.KEY_LAT));
            double lng = cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter.KEY_LNG));
            list.add(new PlaceView(title, address, lat, lng));
        }

        cursor.close();
        dbadpt.close();

        debugArrayListPlace(list);*/

        /*return list;
    }

    public void debugArrayListPlace(ArrayList<PlaceView> list)
    {
        for(int i=0; i<list.size(); i++)
        {Log.d(TAG, "Title: " + list.get(i).getTitle() + " Address: " + list.get(i).getAddress());}
    }

    public ArrayList<HashMap<String, Object>> generateSrcList (ArrayList<PlaceView> list)
    {
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

       /* for(int i=0; i<list.size(); i++)
        {
            PlaceView pv = list.get(i);
            HashMap<String,Object> placeViewMap = new HashMap<>();
            placeViewMap.put("title", pv.getTitle());
            placeViewMap.put("address", pv.getAddress());
            placeViewMap.put("lat", pv.getLat());
            placeViewMap.put("lng", pv.getLng());
            data.add(placeViewMap);
        }
        debugArrayHashMap(data);*/
       /* return  data;
    }

    public void debugArrayHashMap(ArrayList<HashMap<String, Object>> data)
    {
        Log.d(TAG,"DATA SIZE ################################### "+data.size());
        for (int i=0; i<data.size(); i++)
        {
            Log.d(TAG,"HashMap -> Title: "+ (String)data.get(i).get("title") + " Address: "+
                    (String)data.get(i).get("address") + " Lat: " +
                    (double)data.get(i).get("lat") + " Lng: " +
                    (double)data.get(i).get("lng"));
        }
    }
    public ArrayList<Double> getLatLng(String itm)
    {
        StringTokenizer st = new StringTokenizer(itm, " =,");
        /*st.nextToken();
        Double lat = Double.valueOf(st.nextToken());
        Double lng = Double.valueOf(st.nextToken());*/
    //ArrayList<Double> coord = new ArrayList<>(2);
        /*coord.add(0, lat);
        coord.add(1, lng);*/

       /* while (st.hasMoreTokens())
        {
            Log.d(TAG, "TOKENNNN: " + st.nextToken());
        }*/

        /*for(int i=0; i<10; i++)
        {st.nextToken();}
        double lat = Double.parseDouble(st.nextToken());
        st.nextToken();
        double lng = Double.parseDouble(st.nextToken());

        coord.add(0, lat);
        coord.add(1, lng);

        Log.d(TAG, "LAT****************: " + lat);
        Log.d(TAG, "LNG****************: " + lng);
        return coord;
    }

}
*/
}