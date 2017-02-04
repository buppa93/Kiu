package com.domain.my.giuseppe.kiu.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.kiuwer.Kiuwer;
import com.domain.my.giuseppe.kiu.model.User;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.domain.my.giuseppe.kiu.signin.UserLoggingDetails;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Diego on 08/09/2016.
 */
public class LatLngService extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final int NOTIFICATION_ID = 1;
    private boolean started = false;
    private Handler handler = new Handler();

    private Runnable runnable;

    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void start() {
        started = true;
        handler.postDelayed(runnable, 300000);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
         runnable = new Runnable() {
            @Override
            public void run() {
                //DO THE WORK HERE
                int permissionCheck = ContextCompat.checkSelfPermission(LatLngService.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    Context context = getApplicationContext();
                    CharSequence text ="Lat:" + (String.valueOf(mLastLocation.getLatitude())) +
                            "Long:" + String.valueOf(mLastLocation.getLongitude());
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    //SETTO I CAMPI RELATIVI ALLA POSIZIONE SUL REMOTE DATABASE
                    RemoteDBAdapter re = new RemoteDBAdapter();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    String mail = user.getEmail();
                    String username = User.getUserName(mail);
                    Log.d("blablabla", "ecc -> " + String.valueOf(mLastLocation.getLatitude()) + " " +
                            String.valueOf(mLastLocation.getLongitude()));
                    re.setLatLngAttribute(username,String.valueOf(mLastLocation.getLatitude()),
                            String.valueOf(mLastLocation.getLongitude()));
                    Log.i("notify", "Lat:" + (String.valueOf(mLastLocation.getLatitude())) + "Long:"
                            + String.valueOf(mLastLocation.getLongitude()));
                }
                else{
                    Log.i("notify","LOCATION NULL");
                    //Random rand = new Random();
                }
                //Log.i("notify","Message" + rand.nextInt(10));
                if(started) {
                    start();
                }
            }
        };
        final Intent notificationIntent = new Intent(getApplicationContext(), Kiuwer.class);
        Log.i("notify","STO MANDANDO PARAMETRO BOOL");
        notificationIntent.putExtra("fromNotification", true);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        final Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setOngoing(false)
                .setContentTitle("Kiù")
                .setContentText(getString(R.string.notification))
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(NOTIFICATION_ID, notification);
        // si mette in primo piano per evitare che venga rimosso
        // ripetutamente dal sistema
        //startForeground(NOTIFICATION_ID, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        mGoogleApiClient.connect();
        start();
        //NON GLI PIACCIONO I WHILE
        //CERCARE DI EVITARE I THREAD.SLEEP CHE NON GLI PIACCIONO MOLTO...USARE POST DELAYED
        //SE RUNNABLE DICHIARATO GLOBALE(FUORI DA ONSTARTCOMMAND()), FUNZIONA STOPSERVICE() MA SI STOPPA IN BACKGROUND
        //SE RUNNABLE DICHIARATO IN ONSTARTCOMMAND(), FUNZIONA IN BACKGROUND MA NON FUNZIONA STOPSERVICE
            return START_NOT_STICKY;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently
        Context context = getApplicationContext();
        CharSequence text = "CONNECTION FAILED";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        // ...
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Assume thisActivity is the current activity
        Context context = getApplicationContext();
        CharSequence text = "CONNECTED";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            text ="Lat:" + (String.valueOf(mLastLocation.getLatitude())) + "Long:" + String.valueOf(mLastLocation.getLongitude());
            toast = Toast.makeText(context, text, duration);
            toast.show();
            //SETTO I CAMPI RELATIVI ALLA POSIZIONE SUL REMOTE DATABASE
            RemoteDBAdapter re = new RemoteDBAdapter();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            String mail = user.getEmail();
            String username = User.getUserName(mail);
            re.setLatLngAttribute(username,String.valueOf(mLastLocation.getLatitude()),
                    String.valueOf(mLastLocation.getLongitude()));
            Log.i("notify", "Lat:" + (String.valueOf(mLastLocation.getLatitude())) + "Long:"
                    + String.valueOf(mLastLocation.getLongitude()));
            Log.i("notify", "Lat:" + (String.valueOf(mLastLocation.getLatitude())) + "Long:"
                    + String.valueOf(mLastLocation.getLongitude()));
        }
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        else {
            text = "LOCATION NULL";

            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        stop();
        //DO WORK WHEN THE SERVICE STOPS.
        Context context = getApplicationContext();
        CharSequence text = "Bye toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(NOTIFICATION_ID);
    }
    @Override
    public IBinder onBind(Intent intent) {return null;}
}
