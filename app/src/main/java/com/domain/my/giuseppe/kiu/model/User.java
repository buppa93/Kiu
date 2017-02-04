package com.domain.my.giuseppe.kiu.model;

import android.graphics.Bitmap;

import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by giuseppe on 18/08/16.
 */
public class User {
    public static final String AVAILABILITY_YES = "yes";
    public static final String AVAILABILITY_NO = "no";

    private String email;
    private String userName;
    private String registration_token;
    private double lat;
    private double lng;
    private String availability;
    private ArrayList<Feedback> feedback;
    private String photoUrl;
    private Bitmap profilePhoto;

    /**
     * Empty constructor.
     */
    public User() {}

    /**
     * Constructor for User
     * @param email the user's email
     * @param registration_token the user's registration token
     * @param lat latitude point
     * @param lng longitude point
     * @param availability the user's availability (yes/no)
     * @param feedback the array of the users'feedback
     */
    public User(String email, String registration_token, double lat, double lng,
                String availability, ArrayList<Feedback> feedback)
    {
        this.email = email;
        this.registration_token = registration_token;
        this.lat = lat;
        this.lng = lng;
        this.availability = availability;
        this.feedback = feedback;
    }

    /**
     * Constructor for User
     * @param email the user's email
     * @param registration_token the user's registration token
     * @param availability the user's availability (yes/no)
     * @param photoUrl
     */
    public User(String email, String registration_token, String availability, String photoUrl)
    {
        this.email = email;
        this.registration_token = registration_token;
        this.availability = availability;
        this.photoUrl = photoUrl;
    }

    /**
     * Constructor for User
     * @param email the user's email
     * @param userName the user's username
     * @param availability the user's availability (yes/no)
     * @param photoUrl
     * @param registration_token the user's registration token
     * @param lat latitude point
     * @param lng longitude point
     * @param feedback the array of the users'feedback
     */
    public User(String email, String userName, String availability, String photoUrl,
                String registration_token, double lat, double lng, ArrayList<Feedback> feedback)
    {
        this.email = email;
        this.userName = userName;
        this.availability = availability;
        this.photoUrl = photoUrl;
        this.registration_token = registration_token;
        this.lat = lat;
        this.lng = lng;
        this.feedback = feedback;
    }

    public User(String availability, String email, double lat, double lng, String photoUrl,
                String registration_token, String userName)
    {
        this.availability = availability;
        this.email = email;
        this.lat = lat;
        this.lng = lng;
        this.photoUrl = photoUrl;
        this.registration_token = registration_token;
        this.userName = userName;
    }

    /**
     * Get the user's email
     * @return the user's email
     */
    public String getEmail() {return this.email;}

    /**
     * Get the user's registration token
     * @return th user's registration token
     */
    public String getRegistration_token() {return this.registration_token;}

    /**
     * Get the user's latitude
     * @return the user's latitude
     */
    public double getLat() {return this.lat;}

    /**
     * Get the user's longitude
     * @return the user's longitude
     */
    public double getLng() {return this.lng;}

    /**
     * Get the user's availability
     * @return the user's availability
     */
    public String getAvailability() {return this.availability;}

    /**
     * Get an ArrayList which represents the user's feedback
     * @return the list of user feedback
     */
    public ArrayList<Feedback> getFeedback() {return this.feedback;}

    public String getPhotoUrl() {return this.photoUrl;}

    public Bitmap getProfilePhoto() {return this.profilePhoto;}

    /**
     * Set the user's email
     * @param email the email url
     */
    public void setEmail(String email) {this.email = email;}

    /**
     * Set the user's registration token
     * @param registration_token the registration token
     */
    public void setRegistration_token(String registration_token)
    {this.registration_token = registration_token;}

    /**
     * Set the user's latitude
     * @param lat the latitude point
     */
    public void setLat(double lat) {this.lat = lat;}

    /**
     * Set the user's longitude
     * @param lng the longitude point
     */
    public void setLng(double lng) {this.lng = lng;}

    /**
     * Set the list of user's feedback
     * @param feedback the list of feedback
     */
    public void setFeedback(ArrayList<Feedback> feedback) {this.feedback = feedback;}

    /**
     * Set the user's availability
     * @param availability the availability (yes/no)
     */
    public void setAvailability(String availability) {this.availability = availability;}

    public void setPhotoUrl(String photoUrl) {this.photoUrl = photoUrl;}

    public void setProfilePhoto(Bitmap profilePhoto) {this.profilePhoto = profilePhoto;}

    public void addFeedback(Feedback feedback) {this.feedback.add(feedback);}

    public String toString()
    {
        return "Email: " + this.email + ", Registration Token: "
                + this.registration_token + ", Lat: " + String.valueOf(this.lat) + ", Lng: "
                + String.valueOf(this.lng) + ", Availability: " + this.availability + ", Feedback: "
               /* + this.getFeedback().toString()*/;
    }

    /*public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(RemoteDatabaseString.KEY_REGISTRATION_NODE, this.getRegistration_token());
        valueMap.put(RemoteDatabaseString.KEY_LATITUDE, this.getLat());
        valueMap.put(RemoteDatabaseString.KEY_LONGITUDE, this.getLng());
        valueMap.put(RemoteDatabaseString.KEY_AVAILABILITY, this.getAvailability());
        valueMap.put(RemoteDatabaseString.KEY_FEEDBACK_NODE, this.getFeedback().toHashMap()
                .toString());
        hashMap.put(this.getEmail(), valueMap.toString());
        return hashMap;
    }*/

    public HashMap<String, Object> toHashMapNoUserName()
    {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(RemoteDatabaseString.KEY_REGISTRATION_NODE, this.getRegistration_token());
        valueMap.put(RemoteDatabaseString.KEY_LATITUDE, this.getLat());
        valueMap.put(RemoteDatabaseString.KEY_LONGITUDE, this.getLng());
        valueMap.put(RemoteDatabaseString.KEY_AVAILABILITY, this.getAvailability());
        valueMap.put(RemoteDatabaseString.KEY_FEEDBACK_NODE, this.getFeedback()
                .toString());
        valueMap.put(RemoteDatabaseString.KEY_PHOTOURL, this.getPhotoUrl());
        valueMap.put(RemoteDatabaseString.KEY_EMAIL, this.getEmail());
        return valueMap;
    }

    public String getUserName()
    {
        String userName = "";
        String email = this.getEmail();

        StringTokenizer str = new StringTokenizer(email,"@");
        userName = str.nextToken();

        String transformedUsername = "";

        for(int i=0; i<userName.length(); i++)
        {
            if(userName.charAt(i)=='.')
                transformedUsername += ",";
            else
                transformedUsername += userName.charAt(i);
        }
        return transformedUsername;
    }

    public static String getUserName(String UserEmail)
    {
        String userName = "";
        String email = UserEmail;

        StringTokenizer str = new StringTokenizer(email,"@");
        userName = str.nextToken();

        String transformedUsername = "";

        for(int i=0; i<userName.length(); i++)
        {
            if(userName.charAt(i)=='.')
                transformedUsername += ",";
            else
                transformedUsername += userName.charAt(i);
        }
        return transformedUsername;
    }


}
