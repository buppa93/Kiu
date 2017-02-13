package com.domain.my.giuseppe.kiu.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by PaoloPepe on 11/01/2017.
 */

public class HttpChafferManager {

    private int ID;   //id casuale
    private int IDTYPE;
    private String FROM;  //il registration token del telefono da cui mandare la richiesta
    private String TO ;  //il registration token del telefono a cui mandare la richiesta
    private String PRICE ;  //la tariffa proposta
    private String BUYER_NAME ;  //il nome del kiuwer
    private String SELLER_NAME ;  //il nome dell'helper
    private int FLAG_ACCEPTED; //0 if not accepted 1 else
    private final static String METHOD_POST = "POST";
    public final static String TAG = "HttpChafferManager";

    public HttpChafferManager(int id, int idType, String from, String to, String price,
                              String buyerName, String sellerName, int accepted){

        this.ID = id;
        this.IDTYPE= idType;
        this.FROM = from;
        this.TO = to;
        this.PRICE = price;
        this.BUYER_NAME = buyerName;
        this.SELLER_NAME = sellerName;
        this.FLAG_ACCEPTED = accepted;

    }

    public int getID() {
        return ID;
    }

    public int getIDTYPE() {return IDTYPE;}

    public String getFROM() {
        return FROM;
    }

    public String getTO() {
        return TO;
    }

    public String getPRICE() {
        return PRICE;
    }

    public String getBuyerName() {
        return BUYER_NAME;
    }

    public String getSellerName() {
        return SELLER_NAME;
    }

    public int getFLAG_ACCEPTED() {return FLAG_ACCEPTED;}

    public void sendRequest()
    {
        // Creo l'oggetto URL che rappresenta l'indirizzo della pagina da richiamare
        URL paginaURL;

        try
        {
            //"http://kiuapp.altervista.org/chafferSrv.php"
            paginaURL = new URL("http://kiuapp.altervista.org/chafferSrv.php");
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


    private String makePostString()
    {
        JSONObject jsonObj= new JSONObject();
        try {
            jsonObj.put("id", this.getID());
            jsonObj.put("id-type", this.getIDTYPE());
            jsonObj.put("from", this.getFROM());
            jsonObj.put("to", this.getTO());
            jsonObj.put("price", this.getPRICE());
            jsonObj.put("buyer_name", this.getBuyerName());
            jsonObj.put("seller_name", this.getSellerName());
            jsonObj.put("accepted", this.getFLAG_ACCEPTED());
            return jsonObj.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
