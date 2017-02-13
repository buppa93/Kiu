package com.domain.my.giuseppe.kiu.service;

import android.os.AsyncTask;

import com.domain.my.giuseppe.kiu.utils.HttpChafferManager;

import java.util.Random;


/**
 * Created by PaoloPepe on 11/01/2017.
 */

public class RequestChaffer extends AsyncTask<Void, Void, Void> {

    private static final int IDTYPENUMBER = 2;
    private static final int MAXIDNUMBER =3600;

    private int ID ;   //id casuale
    private int IDTYPE;
    private String FROM ;  //il registration token del telefono da cui mandare la richiesta
    private String TO;  //il registration token del telefono a cui mandare la richiesta
    private String PRICE ;  //la tariffa proposta
    private String BUYER_NAME ;  //il nome del kiuwer
    private String SELLER_NAME ;  //il nome dell'helper
    private int ACCEPTED;


    public RequestChaffer(String from, String to, String price, String buyerName,
                          String sellerName, int accepted) {

        this.ID = makeRandomID();
        this.IDTYPE = IDTYPENUMBER;
        this.FROM = from;
        this.TO = to;
        this.PRICE = price;
        this.BUYER_NAME = buyerName;
        this.SELLER_NAME = sellerName;
        this.ACCEPTED = accepted;
    }


    @Override
    protected Void doInBackground(Void... params){
        HttpChafferManager httpChafferManager = new HttpChafferManager(ID, IDTYPE, FROM, TO,PRICE,
                BUYER_NAME,SELLER_NAME,ACCEPTED);
        httpChafferManager.sendRequest();
        return null;

    }

    private int makeRandomID()
    {
        Random random = new Random();
        return random.nextInt(MAXIDNUMBER);
    }
}