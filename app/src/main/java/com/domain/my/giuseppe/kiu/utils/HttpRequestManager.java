package com.domain.my.giuseppe.kiu.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HttpRequestManager {

    private final static String DATA = "data";
    private final static String TOKENS = "tokens";
    private final static String METHOD_POST = "POST";

    public final static String TAG = "HttpRequestManager";

    private int id;
    private int idType;
    private String localData;
    private String localTime;
    private String placeName;
    private String placeAddress;
    private String note;
    private double price;
    private double latitude;
    private double longitude;
    private int accepted; //0 if not accepted 1 else

    private String destTokens;

    private String myTokens;



    public HttpRequestManager(int id, int idType, String localData, String localTime,
                              String placeName, String placeAddress, String note, String destTokens,
                              double price, double latitude, double longitude, String myTokens,
                              int accepted)
    {
        this.id = id;
        this.idType = idType;
        this.localData = localData;
        this.localTime = localTime;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.note = note;
        this.destTokens = destTokens;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.myTokens = myTokens;
        this.accepted = accepted;
    }



    public HttpRequestManager(){}

    public void sendRequest()
    {
        // Creo l'oggetto URL che rappresenta l'indirizzo della pagina da richiamare
        URL paginaURL;

        try
        {
            //"http://kiuapp.altervista.org/fcmServer.php"
            paginaURL = new URL("http://kiuapp.altervista.org/fcmServer.php");
            // creo l'oggetto HttpURLConnection paragonabile all'apertura di una finestra del browser
            HttpURLConnection client = (HttpURLConnection) paginaURL.openConnection();
            client.setRequestMethod(METHOD_POST);
            String datiPost = makePostString();
            // se devo inviare il dato in POST
            client.setDoOutput(true);
            client.setChunkedStreamingMode(0);
            OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream());
            wr.write(datiPost);
            wr.flush();
            Log.d(TAG, "Http Response: " + client.getResponseMessage());

        }
        catch (MalformedURLException e) {}
        catch (IOException e) {}

    }

    public static String getDATA() {
        return DATA;
    }

    public static String getTOKENS() {
        return TOKENS;
    }

    public static String getMethodPost() {
        return METHOD_POST;
    }

    public static String getTAG() {
        return TAG;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdType() {return this.idType;}

    public void setIdType(int idType) {this.idType = idType;}

    public String getLocalData() {
        return localData;
    }

    public void setLocalData(String localData) {
        this.localData = localData;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

   /* public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }*/

    public String getMyTokens() {
        return myTokens;
    }

    public void setMyTokens(String myTokens) {
        this.myTokens = myTokens;
    }

    public String getDestTokens() {
        return destTokens;
    }

    public void setDestTokens(String destTokens) {
        this.destTokens = destTokens;
    }

    /*private String makePostString()
    {
        String postString = null;

        try

        {
            postString = URLEncoder.encode(DATA, "UTF-8")+"="
                    +URLEncoder.encode(String.valueOf(this.id)+"*"+this.localData+"*"
                    +this.localTime+"*"+this.placeName+"*"+this.placeAddress+"*"+this.note
                    +"*"+String.valueOf(this.price)+"*"+String.valueOf(this.latitude)+"*"
                    +String.valueOf(this.longitude)+"*"+this.myTokens,"UTF-8")
                    +"&" +URLEncoder.encode(TOKENS, "UTF-8") +"=";
            if(tokens.size() == 1) {postString += URLEncoder.encode(tokens.get(0),"UTF-8");}
            else
            {
                for (String s : this.tokens) {postString += URLEncoder.encode(s, "UTF-8") + ",";}
            }
        }
        catch (UnsupportedEncodingException e){}

        Log.d(TAG, "String in post -> " + postString);
        return postString;
    }*/

    private String makePostString()
    {
        JSONObject jsonObj= new JSONObject();
        try {
            jsonObj.put("localData", this.getLocalData());
            jsonObj.put("localTime", this.getLocalTime());
            jsonObj.put("placeName", this.getPlaceName());
            jsonObj.put("placeAddress", this.getPlaceAddress());
            jsonObj.put("note", this.getNote());
            jsonObj.put("id", this.getId());
            jsonObj.put("id_type", this.getIdType());
            jsonObj.put("price", this.getPrice());
            jsonObj.put("latitude", this.getLatitude());
            jsonObj.put("longitude", this.getLongitude());
            jsonObj.put("mytoken", this.getMyTokens());
            jsonObj.put("destToken", this.getDestTokens());
            jsonObj.put("accepted", this.getAccepted());
            return jsonObj.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}