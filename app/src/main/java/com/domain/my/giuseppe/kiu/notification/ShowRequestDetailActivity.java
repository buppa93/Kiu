package com.domain.my.giuseppe.kiu.notification;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseListKiuer;
import com.domain.my.giuseppe.kiu.model.User;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ShowRequestDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "SRDA";

    TextView acrdKiuwerMod;
    TextView acrdTimeMod;
    TextView acrdDateMod;
    TextView acrdRateMod;
    TextView acrdLocationMod;
    TextView acrdNoteMod;
    Button negotiates;
    EditText input;

    Button getDrivingDirectionBttn;
    Button acceptBttn;
    Button ignoreBttn;
    MapFragment mapFragment;
    FirebaseDatabase database;

    String kiuertoken;
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

    //DONATO
    private static DatabaseListKiuer databaseListKiuer;
    public static final String KEY_ID = "id_kiuer";
    public Cursor cursor;
    int id_kiuer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_request_detail);
        setTitle("Kiù - Hai una nuova richiesta!");
        final Bundle bundle = getIntent().getExtras();
        kiuertoken = bundle.getString(KIUERTOKEN);

        // acrdKiuwerMod = (TextView) findViewById(R.id.acrd_kiuwer_mod);
        acrdTimeMod = (TextView) findViewById(R.id.acrd_time_mod);
        acrdDateMod = (TextView) findViewById(R.id.acrd_date_mod);
        acrdRateMod = (TextView) findViewById(R.id.acrd_rate_mod);
        acrdLocationMod = (TextView) findViewById(R.id.acrd_location_mod);
        acrdNoteMod = (TextView) findViewById(R.id.acrd_note_mod);
        acrdKiuwerMod = (TextView) findViewById(R.id.acrd_kiuwer_mod);
        getDrivingDirectionBttn = (Button) findViewById(R.id.get_driving_direction_bttn);
        acceptBttn = (Button) findViewById(R.id.accept_bttn);
        ignoreBttn = (Button) findViewById(R.id.ignore_bttn);
        negotiates = (Button) findViewById(R.id.negotiates);

        //DONATO
        id_kiuer = Integer.valueOf(bundle.getString(KEY_ID));


        //END DONATO

        // cliccando il button invio conferma di accettazione al server
        acceptBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestService requestService = new RequestService(2, bundle.getString(LOCALDATA), bundle.getString(LOCALTIME),
                        bundle.getString(PLACENAME), bundle.getString(PLACEADDRESS), bundle.getString(NOTE),
                        bundle.getString(KIUERTOKEN), Double.valueOf(bundle.getString(PRICE)),
                        Double.valueOf(bundle.getString(LATITUDE)), Double.valueOf(bundle.getString(LONGITUDE)),
                        FirebaseInstanceId.getInstance().getToken(), 1);
                requestService.execute();
                Log.d(TAG, "invio conferma al kiuer ->" + requestService.toString());

                //DONATO
                databaseListKiuer = new DatabaseListKiuer(getApplication());
                databaseListKiuer.open();
                databaseListKiuer.setRequestAccepted(id_kiuer);
                finish();
                //END DONATO
            }
        });

        negotiates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(ShowRequestDetailActivity.this);
                input = new EditText(ShowRequestDetailActivity.this);
                input.setWidth(5);
                input.setSingleLine(true);
                input.setHorizontallyScrolling(false);

                a_builder.setView(input);
                a_builder.setMessage(R.string.negotiates)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                RequestChaffer requestChaffer = new RequestChaffer(3,
                                        FirebaseInstanceId.getInstance().getToken(),
                                        bundle.getString(KIUERTOKEN),
                                        input.getText().toString(),
                                        bundle.getString(acrdKiuwerMod.getText().toString()),
                                        bundle.getString(User.getUserName(firebaseUser.getEmail())), 0
                                );
                                requestChaffer.execute();
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle(R.string.text_negotiates);
                alert.show();
            }
        });

        //setta i campi con i dati che ricevo tramite server
        // acrdKiuwerMod.setText(userName);
        acrdDateMod.setText(bundle.getString(LOCALDATA));
        acrdLocationMod.setText(bundle.getString(PLACENAME) + ", " + bundle.getString(PLACEADDRESS));
        acrdRateMod.setText(bundle.getString(PRICE));
        acrdTimeMod.setText(bundle.getString(LOCALTIME));
        acrdNoteMod.setText(bundle.getString(NOTE));

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.preview_map);
        mapFragment.getMapAsync(this);
        // acrdLocationMod.setText("123 Testing Rd City State zip");

        getDrivingDirectionBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //faccio partire l'intent per aprire la mappa su un app esterna.
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + bundle.getString(LATITUDE) + "," +
                        bundle.getString(LONGITUDE) + "(" + bundle.getString(PLACENAME) + ", " + bundle.getString(PLACEADDRESS) + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        //recupero il nome del kiuer sul remoto tramite il token e setto il relativo campo
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference().child(RemoteDatabaseString.KEY_USER_NODE)
                .getRef();
        Query query = dbRef.orderByChild(RemoteDatabaseString.KEY_REGISTRATION_NODE)
                .equalTo(kiuertoken);
        Log.d(TAG, "KIUWER-TOKEN -> " + kiuertoken);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> dataRetrived = (HashMap<String, Object>) dataSnapshot
                        .getValue();
                Log.d(TAG, "HASHMAP -> " + dataRetrived.toString());
                Set<String> keySet = dataRetrived.keySet();
                Log.d(TAG, "KEYSET -> " + keySet.toString());
                String usr = "";
                for (String key : keySet) {
                    usr = key;
                    Log.d(TAG, "KEY -> " + key);
                    Log.d(TAG, "USR DENTRO L'IF -> " + usr);
                }
                Log.d(TAG, "USR FUORI DALL'IF -> " + usr);
                acrdKiuwerMod.setText(usr);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        RemoteDBAdapter dbAdapter = new RemoteDBAdapter();
//        Log.d(TAG, "Cazzo di token-> " + kiuertoken);
//        String kiuwerName = dbAdapter.getUsernameByToken(kiuertoken);
//        Log.d(TAG, "Cazzo di nome-> " + kiuwerName);
//        acrdKiuwerMod.setText(kiuwerName); //setta nome del kiuer
//        database = FirebaseDatabase.getInstance();
//        DatabaseReference dbRef = database.getReference().child(RemoteDatabaseString.KEY_USER_NODE)
//                .getRef();
//
//        Query query = dbRef.orderByChild(RemoteDatabaseString.KEY_REGISTRATION_NODE)
//                .equalTo(kiuertoken);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                HashMap<String, Object> dataRetrived = (HashMap<String, Object>) dataSnapshot
//                        .getValue();
//                Set<String> keySet = dataRetrived.keySet();
//                String usr = "";
//                for(String key : keySet) {usr = key;}
//
//                acrdKiuwerMod.setText(usr);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });


    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        GoogleMap googleMap = mMap;
        Bundle bundle = getIntent().getExtras();

        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng location = new LatLng((Double.parseDouble(bundle.getString(LATITUDE))), (Double.parseDouble(bundle.getString(LONGITUDE))));
        googleMap.addMarker(new MarkerOptions().position(location).title(bundle.getString(PLACENAME)).snippet(bundle.getString(PLACEADDRESS)));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    /**
     * Search username of kiuer by token.
     *
     * @param token user's token
     */
    public String getUserbyToken(String token) {

        final ArrayList<String> tokenlist = new ArrayList<>();
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE);

        Query queries = databaseReference.orderByChild(RemoteDatabaseString.KEY_REGISTRATION_NODE)
                .equalTo(token);
        queries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> tokenfound = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                tokenlist.add(/*createHelpersTokenList(tokenfound).toString()*/0, createHelpersTokenList(tokenfound).toString());
                acrdKiuwerMod = (TextView) findViewById(R.id.acrd_kiuwer_mod);
                String st = tokenlist.get(0);
                acrdKiuwerMod.setText(tokenlist.get(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return tokenlist.toString();
    }

    private ArrayList<String> createHelpersTokenList(HashMap<String, HashMap<String, String>> helpers) {
        ArrayList<String> helpersSelected = new ArrayList<>();

        for (Map.Entry<String, HashMap<String, String>> entry : helpers.entrySet()) {
            HashMap<String, ?> userMap = entry.getValue();
            helpersSelected.add(String.valueOf(userMap.get("userName")));
        }
        return helpersSelected;
    }
}