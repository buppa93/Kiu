package com.domain.my.giuseppe.kiu.remotedatabase;

import android.util.Log;

import com.domain.my.giuseppe.kiu.model.Feedback;
import com.domain.my.giuseppe.kiu.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giuseppe on 22/07/16.
 */
public class RemoteDBAdapter
{
    FirebaseDatabase database;
    public Feedback lastFeedback;

    public static final String TAG = "RemoteDBAdapter";

    /**
     * Builder for this class
     */
    public RemoteDBAdapter()
    {
        Log.d(TAG, "Sto creando l'istanza di RemoteDBAdapter");
        database = FirebaseDatabase.getInstance();
    }

    /**
     * Write current user on database
     */

    //TODO da riscrivere urgente!
    public void writeUser()
    {
        Log.d(TAG, "Sono in writeUser()");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String registrationToken = FirebaseInstanceId.getInstance().getToken();

        HashMap<String, Object> userData = new HashMap<>();
        userData.put(RemoteDatabaseString.KEY_AVAILABILITY, RemoteDatabaseString
                .KEY_AVAILABILITY_NO);
        userData.put(RemoteDatabaseString.KEY_EMAIL, user.getEmail());
        userData.put(RemoteDatabaseString.KEY_FEEDBACK_NODE, null);
        userData.put(RemoteDatabaseString.KEY_LATITUDE, null);
        userData.put(RemoteDatabaseString.KEY_LONGITUDE, null);
        userData.put(RemoteDatabaseString.KEY_REGISTRATION_NODE, registrationToken);
        userData.put(RemoteDatabaseString.KEY_USERNAME, User.getUserName(user.getEmail()));

        DatabaseReference mDatabase = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE)
                .child(User.getUserName(transformEmail(user.getEmail())));

        mDatabase.setValue(userData);
    }

    public void upgradeAveragePrices(final double price)
    {
        final DatabaseReference reference = database.getReference()
                .child(RemoteDatabaseString.KEY_AVERAGE_PRICES);

        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String,Object> data = (HashMap<String,Object>) dataSnapshot.getValue();

                int n = Integer.parseInt(data.get(RemoteDatabaseString.KEY_N).toString());
                double sum = Double.parseDouble(data.get(RemoteDatabaseString.KEY_SUM).toString());
                n += 1;
                sum += price;
                double average = sum / n;

                data.put(RemoteDatabaseString.KEY_N, String.valueOf(n));
                data.put(RemoteDatabaseString.KEY_SUM, String.valueOf(sum));
                data.put(RemoteDatabaseString.KEY_AVERAGE, String.valueOf(average));

                reference.updateChildren(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * aggiorna il nuovo token sul database al nodo utente
     * @param user utente loggato
     * @param newToken nuovo token da inserire
     */
    public void updateRegistrationToken(String user, String newToken)
    {
        Log.d(TAG, "Sono in updateRegistrationToken()");
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE);
        DatabaseReference mdatabaseReference = databaseReference
                .child(user)
                .child(RemoteDatabaseString.KEY_REGISTRATION_NODE);
        mdatabaseReference.setValue(newToken);

    }

    /**
     * Leave a Feedback to a specific user
     * @param username the username of the user
     * @param feedback the feedback as to leave
     */
    public void leaveFeedback(String username, Feedback feedback)
    {
        Log.d(TAG, "Sono leaveFeedback()");
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE);
        Map<String, Object> map = new HashMap<>();
        map.put(RemoteDatabaseString.KEY_FEEDBACK_COMMENT, feedback.getComment());
        map.put(RemoteDatabaseString.KEY_FEEDBACK_RATING, feedback.getRating());
        databaseReference.child(username).child(RemoteDatabaseString.KEY_FEEDBACK_NODE)
                .child(feedback.getFeedbackno()).setValue(map);
    }

    /**
     * replaces the . with the , inside a string
     * @param email the string to be analyzed
     * @return the string transformed
     */
    public static String transformEmail(String email)
    {
        Log.d(TAG, "Sono in transformEmail()");
        String transformedEmail = "";

        for(int i=0; i<email.length(); i++)
        {
            if(email.charAt(i)=='.')
                transformedEmail += ",";
            else
                transformedEmail += email.charAt(i);
        }

        return transformedEmail;
    }

    /**
     * replaces the , with the . inside a string
     * @param email the string to be analyzed
     * @return the string transformed
     */
    public static String restoreEmail(String email)
    {
        Log.d(TAG, "restoreEmail()");
        String restoredEmail = "";

        for(int i=0; i<email.length(); i++)
        {
            if(email.charAt(i)==',')
                restoredEmail += ".";
            else
                restoredEmail += email.charAt(i);
        }

        return restoredEmail;
    }

    /**
     * Transform a string in a double and raplace , with .
     * @param coord a string representing a coordinate point
     * @return the transformed coordinate point
     */
    public static double restoreCoord(String coord)
    {
        Log.d(TAG, "Sono in restoreCoord()");
        double converted;
        String str = "";

        for(int i=0; i<coord.length(); i++)
        {
            if(coord.charAt(i)==',')
                str +='.';
            else
                str += coord.charAt(i);
        }

        converted = Double.parseDouble(str);
        return converted;
    }

    /**
     * Transform a double in a strig and raplace . with ,
     * @param coord the coordinate point
     * @return the transformed coordinate point
     */
    public static String trasformCoord(double coord)
    {
        Log.d(TAG, "trasformCoord()");
        String str = "";
        String toStr = String.valueOf(coord);

        for(int i=0; i<toStr.length(); i++)
        {
            if(toStr.charAt(i)=='.')
                str +=',';
            else
                str += toStr.charAt(i);
        }

        return str;
    }

    /**
     * Set latitude and longitude of the user's account
     * @param latitude
     * @param longitude
     */
    public void setLatLngAttribute(String username, String latitude, String longitude){
        final DatabaseReference databaseReference = database.getReference().child
                (RemoteDatabaseString.KEY_USER_NODE).child(username);
        // Log.v("USERNAME", UserLoggingDetails.getIstance().getName());
        Log.d("USERNAME",User.getUserName(FirebaseAuth.getInstance()
                .getCurrentUser().getEmail()));
        Map<String,Object> data = new HashMap<>();
        data.put(RemoteDatabaseString.KEY_LATITUDE, latitude);
        data.put(RemoteDatabaseString.KEY_LONGITUDE,longitude);
        databaseReference.updateChildren(data);

    }

    /**
     * set availability attribute on database by username.
     * @param value
     */
    public void SetAvailabilityAttribute (boolean value) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mail = user.getEmail();
        String username = User.getUserName(mail);
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).child(username).child(RemoteDatabaseString.KEY_AVAILABILITY);
        if(value==true){
            databaseReference.setValue(RemoteDatabaseString.KEY_AVAILABILITY_YES);
        }
        else{
            databaseReference.setValue(RemoteDatabaseString.KEY_AVAILABILITY_NO);
        }
    }
}
