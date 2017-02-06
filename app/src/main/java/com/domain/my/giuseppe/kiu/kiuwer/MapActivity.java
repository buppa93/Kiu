package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseListHelperAdapter;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;
import com.domain.my.giuseppe.kiu.service.RequestService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

//import android.location.LocationListener;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback
{
    //Bundle passingData;
    public String TAG = "MapActivity";
    private DatabaseListHelperAdapter dbHelper;
    private long cursor;
    private ViewGroup infoWindow;
    private ViewGroup yoursearchinfoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private TextView yourinfoTitle;
    private TextView yourinfoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    //ArrayList<String> token= new ArrayList<>();
    private static final String LATLNG="LatLng";
    private static final String DATE="date";
    private static final String RATE="payment";
    private static final String TIME="time";
    private static final String NOTE="note";
    private static final String PLACEDETAILS="placeDetails";
    private static final String PLACENAME="placeName";
    private static final String LATITUDE="latitude";
    private static final String LONGITUDE="longitude";
    private static final String COORDINATES = "coordinates";
    private static final String RAY = "ray";
    Bundle receive;

    Map<String, Object> data;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        receive = getIntent().getExtras();

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);


        data = getBundleData(receive);

        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.infowindow, null);
        this.yoursearchinfoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.yoursearchinfowindow, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoButton = (Button)infoWindow.findViewById(R.id.button);
        this.yourinfoTitle = (TextView)yoursearchinfoWindow.findViewById(R.id.yourtitle);
        this.yourinfoSnippet = (TextView)yoursearchinfoWindow.findViewById(R.id.yoursnippet);
        infoButton.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_normal),
                getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_pressed))
        {
            @Override
            protected void onClickConfirmed(View v, final Marker marker)
            {
                // Here we can perform some action triggered after clicking the button
                //Toast.makeText(MapActivity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MapActivity.this)
                        .setTitle(getString(R.string.AlertMessage))
                        .setMessage(getString(R.string.AlertTitle))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // SEND REQUEST TO HELPER
                                //token.add(FirebaseInstanceId.getInstance().getToken());
                               // RequestService request=new RequestService(2,"12/02/2015", "12:00", "zona industriale",
                                //      "via ciucciu soruta, Gallipoli", "chupa",token, 3.00, 25.1423293, 49.2962342, FirebaseInstanceId.getInstance().getToken());
                               /*RequestService request=new RequestService(2,receive.getString(DATE),receive.getString(TIME),receive.getString(PLACENAME),
                                       receive.getString(PLACEDETAILS), receive.getString(NOTE), token, receive.getDouble("Rate"), receive.getDouble("Latitude"),
                                       receive.getDouble("Longitude"),FirebaseInstanceId.getInstance().getToken());*/

                               /* RequestService request = new RequestService(2,
                                        (String) data.get(DATE), (String) data.get(TIME),
                                        (String) data.get(PLACENAME), (String) data.get(PLACEDETAILS),
                                        (String) data.get(NOTE), token,
                                        Double.parseDouble(data.get(RATE).toString()),
                                        Double.parseDouble(data.get(LATITUDE).toString()),
                                        Double.parseDouble(data.get(LONGITUDE).toString()),
                                        FirebaseInstanceId.getInstance().getToken());
                                request.execute();*/
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference mDatabaseRef = database.getReference();
                                //Getting only available helpers
                                // Query queries = mDatabaseRef.orderByChild(RemoteDatabaseString.KEY_USERNAME)
                                // .equalTo(marker.getTitle());
                                Log.d(TAG, "USERNAME DA MARKER" + marker.getTitle());
                                mDatabaseRef.child("users").child(marker.getTitle())
                                        .child("registration_token")
                                        .addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot)
                                            {
                                                String tok;
                                                tok = (String) dataSnapshot.getValue();
                                                Log.d(TAG,"TOKEN" + tok);
                                                RequestService request = new RequestService(2,
                                                        (String) data.get(DATE),
                                                        (String) data.get(TIME),
                                                        (String) data.get(PLACENAME),
                                                        (String) data.get(PLACEDETAILS),
                                                        (String) data.get(NOTE), tok,
                                                        Double.parseDouble(data.get(RATE)
                                                                .toString()),
                                                        Double.parseDouble(data.get(LATITUDE)
                                                                .toString()),
                                                        Double.parseDouble(data.get(LONGITUDE)
                                                                .toString()),
                                                        FirebaseInstanceId.getInstance().getToken(),
                                                        0);
                                                request.execute();
                                            }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };

        this.infoButton.setOnTouchListener(infoButtonListener);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        final TextView raggio = (TextView) findViewById(R.id.raggio_editext);
        final int ray = receive.getInt("ray",99);
        raggio.setText(getString(R.string.search) + ray +" Km");

        Log.d(TAG, "RAGGIO -> " + ray);

        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));

        Log.v(TAG, receive.getString("coordinates"));

        final LatLng choice = new LatLng((double) data.get(LATITUDE), (double) data.get(LONGITUDE));

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindow(Marker marker) {return null;}

            @Override
            public View getInfoContents(Marker marker)
            {
                // Setting up the infoWindow with current's marker info
                LatLng pos = marker.getPosition();
                Geocoder geocoder= new Geocoder(MapActivity.this, Locale.ITALIAN);
                try
                {
                    List<Address> addresses = geocoder.getFromLocation(pos.latitude,pos.longitude, 1);
                    Address fetchedAddress = addresses.get(0);
                    StringBuilder strAddress = new StringBuilder();
                    for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++)
                    {strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");}

                    infoSnippet.setText("Pos: " + strAddress.toString());
                    yourinfoSnippet.setText("Pos: " + strAddress.toString());
                    infoTitle.setText(marker.getTitle());
                    yourinfoTitle.setText(marker.getTitle());
                    infoButtonListener.setMarker(marker);

                    //TODO insert rating value on rating bar from (database?)
                    dbHelper= new DatabaseListHelperAdapter(getApplicationContext());
                    dbHelper.open();
                     cursor=dbHelper.createContact("mario", (String) data.get(DATE).toString(),
                             (String) data.get(PLACEDETAILS), (String) data.get(TIME),
                             (String) data.get(RATE));
                    Toast.makeText(getApplicationContext(),"Ho imserito i dati nel db!!", Toast.LENGTH_SHORT).show();
                    dbHelper.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
                }

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
               // mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
               // return infoWindow;
                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                if((choice.latitude==pos.latitude) && (choice.longitude==pos.longitude)){
                    Log.d(TAG,"SONO UGUALI");
                    mapWrapperLayout.setMarkerWithInfoWindow(marker, yoursearchinfoWindow);
                    return yoursearchinfoWindow;
                }
                else {
                    Log.d(TAG,"NOOOON SONO UGUALI");
                    mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                    return infoWindow;
                }
            }
        });

        try
        {map.setMyLocationEnabled(true);}
        catch (SecurityException se) {}
        if(/*Integer.parseInt(RAY)*/ray<20)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(choice, 11));
        else if (/*Integer.parseInt(RAY)*/ray==20)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(choice, 10));
        else
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(choice,9));
        // Let's add a couple of markers
        map.addMarker(new MarkerOptions()
                .title(getString(R.string.your_search))
                //.snippet("Czech Republic")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.green_marker))
                .position(choice));
        Circle circle = map.addCircle(new CircleOptions()
                .center(choice)
                .radius(receive.getInt(RAY,99)*1000)
                .strokeColor(Color.BLUE)
                .strokeWidth(5));

        //ADD HERE MARKERS NEAR SEARCH POSITION

        /*map.addMarker(new MarkerOptions()
                .title("Paris")
                .snippet("France")
                .position(new LatLng(48.86,2.33))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.azur_marker)));

        map.addMarker(new MarkerOptions()
                .title("London")
                .snippet("United Kingdom")
                .position(new LatLng(51.51,-0.1))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.azur_marker)));
*/
        Map<String, LatLng> explCoords;
        explCoords = testCoodr();
        LatLng app;
        final Location myloc= new Location("");
        myloc.setLatitude(choice.latitude);
        myloc.setLongitude(choice.longitude);
        Location apploc= new Location("");
        /*double distance;
        for (int i=0; i<testCoodr().size(); i++){
            app=explCoords.get(Integer.toString(i+1));
            apploc.setLatitude(app.latitude);
            apploc.setLongitude(app.longitude);
            distance=myloc.distanceTo(apploc);

            //distance=(Math.acos(cos(mylong-applong)*cos(mylat)*cos(applat)+sin(mylat)*sin(applat))*6371*);
            if(distance<=(ray*1000)) {
                map.addMarker(new MarkerOptions()
                        .title("Helper" + i + 1)
                        .snippet("snippet" + i + 1)
                        .position(explCoords.get(Integer.toString(i + 1)))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.azur_marker)));
            }

        }*/
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE);
        //Getting only available helpers
        Query queries = mDatabaseRef.orderByChild(RemoteDatabaseString.KEY_AVAILABILITY)
                .equalTo(RemoteDatabaseString.KEY_AVAILABILITY_YES);
        queries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // HashMap<?, ?> dataRetrieved = (HashMap<?, ?>) dataSnapshot.getValue();
                String username=new String();
                String tok = new String();
                Location lochelper= new Location("");
                double distance;
                //getting name and coords from each helper
                for(DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    if (messageSnapshot.child(RemoteDatabaseString.KEY_LATITUDE).getValue() != null) {
                        lochelper.setLatitude( Double.parseDouble(messageSnapshot.child(RemoteDatabaseString.KEY_LATITUDE).getValue().toString()));
                        Log.d(TAG, "Database IN MAP Data Retrieved: " + String.valueOf(lochelper.getLatitude()));
                    }
                    if (messageSnapshot.child(RemoteDatabaseString.KEY_LONGITUDE).getValue() != null) {
                        lochelper.setLongitude( Double.parseDouble(messageSnapshot.child(RemoteDatabaseString.KEY_LONGITUDE).getValue().toString()));
                        Log.d(TAG, "Database IN MAP Data Retrieved: " + String.valueOf(lochelper.getLongitude()));
                    }
                    //showing only helpers near choice
                    Log.d(TAG,"MYLOC" + String.valueOf(myloc.getLatitude()));
                    distance=myloc.distanceTo(lochelper);
                    Log.d(TAG,"Distanza: " + String.valueOf(distance));
                    if(distance<=(ray*1000)) {
                        if (messageSnapshot.child(RemoteDatabaseString.KEY_USERNAME).getValue() != null) {
                            username = messageSnapshot.child(RemoteDatabaseString.KEY_USERNAME).getValue().toString();
                            Log.d(TAG, "Database IN MAP Data Retrieved: " + username);
                        }
                        Log.d(TAG, "helper vicino");
                        LatLng HelpLatLng = new LatLng(lochelper.getLatitude(), lochelper.getLongitude());
                        map.addMarker(new MarkerOptions()
                                .title(username)
                                .position(HelpLatLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.azur_marker)));
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public ArrayList<Double> getLatLng(String coordinate)
    {
        StringTokenizer st = new StringTokenizer(coordinate, " (),");
        int i = 0;
        st.nextToken();
        Double lat = Double.valueOf(st.nextToken());
        Double lng = Double.valueOf(st.nextToken());
        ArrayList<Double> coord = new ArrayList<>(2);
        coord.add(0, lat);
        coord.add(1, lng);
        return coord;
    }

    public Map<String, LatLng> testCoodr ()
    {
        Map<String, LatLng> coords = new HashMap<String, LatLng>();

        coords.put("1", new LatLng(40.2679664,18.0378201));
        coords.put("2", new LatLng(40.2587896,18.0424882));
        coords.put("3", new LatLng(40.2643715,18.0456148));
        coords.put("4", new LatLng(40.2702469,18.0485813));
        coords.put("5", new LatLng(40.2686927,18.0505273));
        coords.put("6", new LatLng(40.268529,18.0488789));
        coords.put("7", new LatLng(40.2759414,18.0457136));
        coords.put("8", new LatLng(40.2742992,18.0651668));
        coords.put("9", new LatLng(48.86,2.33));
        coords.put("10", new LatLng(51.51,-0.1));

        return coords;
    }

    public static int getPixelsFromDp(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    private Map<String, Object> getBundleData(Bundle bundle)
    {
        Map<String, Object> bundleData = new HashMap<>();
        ArrayList<Double> coord = getLatLng(bundle.getString(COORDINATES));
        bundleData.put(LATITUDE, coord.get(0));
        bundleData.put(LONGITUDE, coord.get(1));
        bundleData.put(DATE, bundle.get(DATE));
        bundleData.put(TIME, bundle.get(TIME));
        bundleData.put(RATE, bundle.get(RATE));
        bundleData.put(NOTE, bundle.get(NOTE));
        bundleData.put(PLACENAME, bundle.get(PLACENAME));
        bundleData.put(PLACEDETAILS, bundle.get(PLACEDETAILS));
        bundleData.put(RAY, bundle.get(RAY));

        debugMapData(bundleData);

        return bundleData;
    }

    private void debugMapData(Map<String, Object> dataMap)
    {
        Log.d(TAG, "DATA -> " + dataMap.toString());
    }
}

