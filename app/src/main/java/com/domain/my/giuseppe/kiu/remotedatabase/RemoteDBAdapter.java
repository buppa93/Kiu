package com.domain.my.giuseppe.kiu.remotedatabase;

import android.provider.ContactsContract;
import android.util.Log;

import com.domain.my.giuseppe.kiu.kiuwer.Kiuwer;
import com.domain.my.giuseppe.kiu.model.Feedback;
import com.domain.my.giuseppe.kiu.model.User;
import com.domain.my.giuseppe.kiu.utils.SingletonObject;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

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

    public FirebaseDatabase getAdapter()
    {
        return database;
    }

    /**
     * Write an user on database
     * @param user an istance of User class
     */
    public void writeUser(User user)
    {
        Log.d(TAG, "Sono in writeUser()");
        DatabaseReference mDatabase = database.getReference().child("users");
        mDatabase.child(transformEmail(user.getUserName()));
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).child(transformEmail(user.getUserName()));
        databaseReference.setValue(user);
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
     * Get a User by its userName
     * @param userName username of the user (email without domain and , instead of .)
     * @return an istance of User class
     */
    public User getUser(final String userName)
    {
        Log.d(TAG, "Sono in getUser()");
        final User user = new User();

        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).child(userName);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<?,?> userMap = (HashMap<?,?>) dataSnapshot.getValue();

                ArrayList<HashMap<String,String>> feedbacksMap =
                        (ArrayList<HashMap<String,String>>) userMap
                                .get(RemoteDatabaseString.KEY_FEEDBACK_NODE);
                ArrayList<Feedback> feedbacksList = getFeedbacks(feedbacksMap);

                user.setFeedback(feedbacksList);
                user.setEmail(userMap.get(RemoteDatabaseString.KEY_EMAIL).toString());
                user.setLat(Double.parseDouble(userMap.get(RemoteDatabaseString.KEY_LATITUDE)
                        .toString()));
                user.setLng(Double.parseDouble(userMap.get(RemoteDatabaseString.KEY_LONGITUDE)
                        .toString()));
                user.setAvailability(userMap.get(RemoteDatabaseString.KEY_AVAILABILITY).toString());
                user.setRegistration_token(userMap.get(RemoteDatabaseString.KEY_REGISTRATION_NODE)
                        .toString());
                user.setPhotoUrl(userMap.get(RemoteDatabaseString.KEY_PHOTOURL).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return user;
    }

    /**
     * aggiorna il nuovo token sul database al nodo utente
     * @param user utente loggato
     * @param newToken nuovo token da inserire
     */
    public void updateRegistrationToken(User user, String newToken)
    {
        Log.d(TAG, "Sono in updateRegistrationToken()");

        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE);
        DatabaseReference mdatabaseReference = databaseReference
                .child(user.getUserName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .child(RemoteDatabaseString.KEY_REGISTRATION_NODE);
        //  user.setRegistration_token(newToken);
        mdatabaseReference.setValue(newToken);

    }

    /*public void updateRegistrationToken(String email, String token)
    {
        if((email != null) && (email.equals("")))
        {
            DatabaseReference databaseReference = database.getReference()
                    .child(RemoteDatabaseString.KEY_USER_NODE).child(User.getUserName(email));
            Map<String, Object> valueUpdated = new HashMap<>();
            valueUpdated.put(RemoteDatabaseString.KEY_REGISTRATION_NODE, token);
            databaseReference.updateChildren(valueUpdated);
        }
    }*/

    /**
     * Only test method
     */
    public void getDb()
    {
        Log.d(TAG, "Sono in getDb()");
        DatabaseReference mDatabaseRef = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<?, ?> dataRetrieved = (HashMap<?, ?>) dataSnapshot.getValue();
                Log.d(TAG, "Database Data Retrieved: " + dataRetrieved.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Set the current User instance with the data received from the database
     *
     * @param userName the username of the current user
     */
    public void getCurrentUser(final String userName)
    {
        Log.d(TAG, "Sono in getCurrentUser()");
        final DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).child(userName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<?, ?> user = (HashMap<?, ?>) dataSnapshot.getValue();
                Log.d(TAG, "getCurrentUser: " + user.toString());

                ArrayList<HashMap<String, String>> feedbacks = (ArrayList<HashMap<String, String>>)
                        user.get(RemoteDatabaseString.KEY_FEEDBACK_NODE);
                //Log.d(TAG, "Array List Feedbacks -> " + feedbacks.toString());
                ArrayList<Feedback> feedbacksArray = getFeedbacks(feedbacks);

                Log.d(TAG, "FeedbcksArray: " + feedbacksArray.toString());

                Log.d(TAG, "AVAILABILITY: " + user
                        .get(RemoteDatabaseString.KEY_AVAILABILITY).toString());
                Log.d(TAG, "EMAIL: " + user
                        .get(RemoteDatabaseString.KEY_EMAIL).toString());
                Log.d(TAG, "LATITUDE: " + user
                        .get(RemoteDatabaseString.KEY_LATITUDE).toString());
                Log.d(TAG, "LONGITUDE: " + user
                        .get(RemoteDatabaseString.KEY_LONGITUDE).toString());
                Log.d(TAG, "REGISTRATION: " + user
                        .get(RemoteDatabaseString.KEY_REGISTRATION_NODE).toString());
                Log.d(TAG, "PHOTOURL: " + user
                        .get(RemoteDatabaseString.KEY_PHOTOURL).toString());
                setCurrentUserAttribute(user, feedbacksArray);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Set attribute for current user logged in
     * @param user an hashmap which represents user
     * @param feedbacks an arraylist which represents the feedbacks list
     */
    private void setCurrentUserAttribute(HashMap<?, ?> user, ArrayList<Feedback> feedbacks)
    {
        Log.d(TAG, "Sono in setCurrentUserAttribute()");
        Kiuwer.currentUserIstance.setAvailability(user.get(RemoteDatabaseString.KEY_AVAILABILITY)
                .toString());
        Kiuwer.currentUserIstance.setLat(restoreCoord(user.get(RemoteDatabaseString.KEY_LATITUDE)
                .toString()));
        Kiuwer.currentUserIstance.setLng(restoreCoord(user.get(RemoteDatabaseString.KEY_LONGITUDE)
                .toString()));
        Kiuwer.currentUserIstance.setRegistration_token(user.get(RemoteDatabaseString
                .KEY_REGISTRATION_NODE).toString());
        Kiuwer.currentUserIstance.setEmail(user.get(RemoteDatabaseString.KEY_EMAIL).toString());
        Kiuwer.currentUserIstance.setPhotoUrl(user.get(RemoteDatabaseString.KEY_PHOTOURL)
                .toString());
        Kiuwer.currentUserIstance.setFeedback(feedbacks);
    }

    /**
     * Transform a crude ArrayList of HashMap, that describe the feedback,
     * in a ArrayList of Feedback
     * @param feedbacks a crude ArrayList of HashMap
     * @return a ArrayList<Feedback> contains all the Feedback
     */
    private ArrayList<Feedback> getFeedbacks(ArrayList<HashMap<String,String>> feedbacks)
    {
        Log.d(TAG, "Sono in getFeedbacks()");
        ArrayList<Feedback> feedbacksArray = new ArrayList<>();
        int i = 0;
        if(feedbacks != null)
        {
            Log.d(TAG, "Feedbacks list -> " + feedbacks.toString());
            for (HashMap<String, String> map : feedbacks)
            {
                if(map != null)
                {
                    Log.d(TAG, "Feedback no = " + i + " -> " + map.toString());
                    Log.d(TAG, "Singolo feedback" + map.toString());
                    feedbacksArray.add(new Feedback(String.valueOf(i), String.valueOf(map
                            .get(RemoteDatabaseString.KEY_FEEDBACK_RATING)), String.valueOf(map
                            .get(RemoteDatabaseString.KEY_FEEDBACK_COMMENT))));
                    i++;
                }
            }
        }
        return feedbacksArray;
    }

    /**
     * Getting the coordinates of specific user, identifer by username
     * @param userName the username of user
     * @return a LatLng object contain the coordinates of user specified in the userName params
     */
    public LatLng getHelperCoord(String userName)
    {
        Log.d(TAG, "Sono in getHelperCoord()");
        final HashMap<String, Double> coords = new HashMap<>();
        final DatabaseReference databaseReference = database.getReference().child
                (RemoteDatabaseString.KEY_USER_NODE).child(userName);
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<?,?> dataRetrived = (HashMap<?,?>) dataSnapshot.getValue();

                double lat = restoreCoord(dataRetrived.get(RemoteDatabaseString.KEY_LATITUDE)
                        .toString());
                double lng = restoreCoord(dataRetrived.get(RemoteDatabaseString.KEY_LONGITUDE)
                        .toString());

                coords.put("lat", lat);
                coords.put("lng", lng);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        LatLng latLng = new LatLng(coords.get("lat"), coords.get("lng"));
        return latLng;
    }

    public String getUsernameByToken(String token)
    {
        String username = "";
        DatabaseReference dbRef = database.getReference().child(RemoteDatabaseString.KEY_USER_NODE)
                .getRef();

        Query query = dbRef.orderByChild(RemoteDatabaseString.KEY_REGISTRATION_NODE).equalTo(token);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Object> dataRetrived = (HashMap<String, Object>) dataSnapshot
                        .getValue();
                Set<String> keySet = dataRetrived.keySet();
                String usr = "";
                for(String key : keySet) {usr = key;}
                SingletonObject.getInstance().setObj(usr);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        username = (String) SingletonObject.getInstance().getObj();
        return username;
    }

    /**
     * Search helpers in the zone.
     * @param latLng coordinates of point of interessed
     * @param ray ray of interest
     */
    public void searchHelpers(LatLng latLng, final int ray)
    {
        Log.d(TAG, "Sono in searchHelpers()");
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).getRef();

        Query helpersInLine = databaseReference.orderByChild(RemoteDatabaseString.KEY_AVAILABILITY)
                .equalTo("yes");

        helpersInLine.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int mRay = ray;
                Log.d(TAG, "Sono in onDataChange()");
                HashMap<String,HashMap<String,String>> helpersFound =
                        (HashMap<String,HashMap<String,String>>) dataSnapshot.getValue();

                Log.d(TAG, "Helpers found: " + String.valueOf(helpersFound.size()));
                Log.d(TAG, "Find User: " + helpersFound.toString());

                //createHelpersList(helpersFound);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    /**
     * Search username of kiuer by token.
     * @param token user's token
     */
    public String getUserbyToken(String token) {

        final ArrayList<String> tokenlist= new ArrayList<>();
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).getRef();

        Query queries = databaseReference.orderByChild(RemoteDatabaseString.KEY_REGISTRATION_NODE)
                .equalTo(token);
        queries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               HashMap<String,HashMap<String,String>> tokenfound= (HashMap<String,HashMap<String,String>>) dataSnapshot.getValue();
               tokenlist.add(/*createHelpersTokenList(tokenfound).toString()*/0,createHelpersTokenList(tokenfound).toString());
                Log.d("datasnapshot", tokenfound.toString());
                Log.d("TOKENLIST->", tokenlist.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("RETURN TOKENLIST->",tokenlist.toString());
        return tokenlist.toString();
    }

    /**
     * Creates a list of helpers from the search results of helpers
     * @param helpers helpers found
     * @return a list of helpers found
     */
    private ArrayList<User> createHelpersList (HashMap<String,HashMap<String,String>> helpers)
    {
        Log.d(TAG, "Sono in createHelpersList()");
        ArrayList<User> helpersSelected = new ArrayList<>();

        for(Map.Entry<String, HashMap<String,String>> entry : helpers.entrySet())
        {
            Log.d(TAG, "Entry -> " + entry.getValue());
            HashMap<String, ?> userMap = entry.getValue();
            ArrayList<HashMap<String, String>> feedbacks = (ArrayList<HashMap<String, String>>)
                    userMap.get(RemoteDatabaseString.KEY_FEEDBACK_NODE);
            //Log.d(TAG, "Array List feedbacks -> " + feedbacks.toString());
            ArrayList<Feedback> feedbacksArray = getFeedbacks(feedbacks);
            helpersSelected.add(new User(userMap.get(RemoteDatabaseString.KEY_EMAIL).toString(),
                    userMap.get("userName").toString(),
                    userMap.get(RemoteDatabaseString.KEY_AVAILABILITY).toString(),
                    userMap.get(RemoteDatabaseString.KEY_PHOTOURL).toString(),
                    userMap.get(RemoteDatabaseString.KEY_REGISTRATION_NODE).toString(),
                    Double.parseDouble(userMap.get(RemoteDatabaseString.KEY_LATITUDE).toString()),
                    Double.parseDouble(userMap.get(RemoteDatabaseString.KEY_LONGITUDE).toString()),
                    feedbacksArray));
        }
        return helpersSelected;
    }

    private ArrayList<String> createHelpersTokenList(HashMap<String,HashMap<String,String>> helpers)
    {
        Log.d(TAG, "Sono in createHelpersTokenList()");
        ArrayList<String> helpersSelected = new ArrayList<>();

        for(Map.Entry<String, HashMap<String,String>> entry : helpers.entrySet()) {
            HashMap<String, ?> userMap = entry.getValue();
            helpersSelected.add(String.valueOf(userMap.get("userName")));
        }
            return helpersSelected;

    }

    /**
     * Leave a Feedback to a specific user
     * @param username the username of the user
     * @param feedback the feedback as to leave
     */
    public void  leaveFeedback(String username, Feedback feedback)
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
    private String transformEmail(String email)
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
    private String restoreEmail(String email)
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
    private double restoreCoord(String coord)
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
    private String trasformCoord(double coord)
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
