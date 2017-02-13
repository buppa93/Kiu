package com.domain.my.giuseppe.kiu.notification;

import android.os.AsyncTask;
import android.util.Log;

/**
 * consente di inviare richiesta al server tramite altro task
 */

public class RequestService extends AsyncTask<Void, Void, Void> {

        private int id;
        private String localData;

    @Override
    public String toString() {
        return "RequestService{" +
                "id=" + id +
                ", localData='" + localData + '\'' +
                ", localTime='" + localTime + '\'' +
                ", placeName='" + placeName + '\'' +
                ", placeAddress='" + placeAddress + '\'' +
                ", note='" + note + '\'' +
                ", price=" + price +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", myTokens='" + myTokens + '\'' +
                ", destTokens='" + destTokens + '\'' +
                ", accepted=" + accepted +
                '}';
    }

    private String localTime;
        private String placeName;
        private String placeAddress;
        private String note;
        private double price;
        private double latitude;
        private double longitude;
        private String myTokens;
        private String destTokens;
        private int accepted;

        /**
         *
         * @param id id della richiesta
         * @param localData data
         * @param localTime ora
         * @param placeName nome del posto
         * @param placeAddress indirizzo del posto
         * @param note note
         * @param destTokens token dell'helper
         * @param price tariffa
         * @param latitude latitudine
         * @param longitude longitudine
         * @param myTokens token del dispositivo corrente
         * @param accepted 0 se non accettato 1 altrimenti
         */
        public RequestService(int id, String localData, String localTime, String placeName,
                              String placeAddress, String note, String destTokens,
                              double price, double latitude, double longitude, String myTokens,
                              int accepted) {
            this.id = id;
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

        @Override
        protected Void doInBackground(Void... params)
        {
            HttpRequestManager httpRequestManager = new HttpRequestManager(id, localData, localTime, //TODO arraylist di tokens del ricevente??
                    placeName, placeAddress, note, destTokens, price,
                    latitude, longitude, myTokens, accepted);
            Log.d("RequestService",httpRequestManager.toString());
            httpRequestManager.sendRequest();
            return null;
        }
    }
