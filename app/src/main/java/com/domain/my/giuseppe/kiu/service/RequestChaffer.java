package com.domain.my.giuseppe.kiu.service;

import android.os.AsyncTask;

import com.domain.my.giuseppe.kiu.utils.HttpChafferManager;


/**
 * Created by PaoloPepe on 11/01/2017.
 */

public class RequestChaffer extends AsyncTask<Void, Void, Void> {

    private  int ID ;   //id casuale
    private  String FROM ;  //il registration token del telefono da cui mandare la richiesta
    private  String TO;  //il registration token del telefono a cui mandare la richiesta
    private  String PRICE ;  //la tariffa proposta
    private  String BUYER_NAME ;  //il nome del kiuwer
    private  String SELLER_NAME ;  //il nome dell'helper
    private  int ACCEPTED;


    public RequestChaffer(int id, String from, String to, String price, String buyerName,
                          String sellerName, int accepted) {

        this.ID = id;
        this.FROM = from;
        this.TO = to;
        this.PRICE = price;
        this.BUYER_NAME = buyerName;
        this.SELLER_NAME = sellerName;
        this.ACCEPTED = accepted;
    }


    @Override
    protected Void doInBackground(Void... params){
        HttpChafferManager httpChafferManager = new HttpChafferManager(ID, FROM, TO,PRICE,
                BUYER_NAME,SELLER_NAME,ACCEPTED);
        httpChafferManager.sendRequest();
        return null;

    }
}