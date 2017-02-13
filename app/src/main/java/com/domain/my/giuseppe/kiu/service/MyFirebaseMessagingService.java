package com.domain.my.giuseppe.kiu.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.helper.ShowRequestDetailActivity;
import com.domain.my.giuseppe.kiu.utils.NotificationData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final int CUSTOM_ACTION_NOTIFICATION = 9567;
    private static final String PROVA="prova messaggio";
    private static final String LOCALDATA="localdata";
    private static final String ID="id";
    private static final String ID_TYPE="idType";
    private static final String LOCALTIME="localtime";
    private static final String PLACENAME="placename";
    private static final String PLACEADDRESS="placeaddress";
    private static final String NOTE="note";
    private static final String PRICE="price";
    private static final String LATITUDE="latitude";
    private static final String LONGITUDE="longitude";
    private static final String FROM ="from";
    private static final String TO ="to";
    private static final String BUYER_NAME ="buyer_name";
    private static final String SELLER_NAME ="seller_name";
    private static final String KIUERTOKEN ="kiuertoken";
    private static final String ACCEPTED = "accepted";
    private static final String BODY = "body";
    private static final String TITLE = "title";
    private static final String SOUND = "sound";



    @Override
    public void onMessageReceived(RemoteMessage message)
    {
        NotificationChaffer notificationChaffer = new NotificationChaffer();
        NotificationRequest notificationRequest = new NotificationRequest();
        int id = Integer.parseInt(message.getData().get(ID_TYPE));

        //se l'id =3 la notifica è una notifica di richiesta negoziazione,  se l'id è 2 è una normale richiesta di fila
        if(id==3){
            notificationChaffer.setFROM(message.getData().get("from"));
            notificationChaffer.setBUYER_NAME(message.getData().get("buyer_name"));
            notificationChaffer.setPRICE(message.getData().get("price"));
            notificationChaffer.setSELLER_NAME(message.getData().get("seller_name"));
            notificationChaffer.setTO(message.getData().get("to"));
            notificationChaffer.setAccepted(Integer.parseInt(message.getData().get("accepted")));
            /*notificationChaffer.setBody(message.getNotification().getBody());
            notificationChaffer.setTitle(message.getNotification().getTitle());
            notificationChaffer.setSound(message.getNotification().getSound());*/
            notificationRequest.setBody(message.getData().get("body"));
            notificationRequest.setTitle(message.getData().get("title"));
            notificationRequest.setSound(message.getData().get("sound"));

            sendNotificationChaffer(notificationChaffer);

            //TODO da creare sendNotificationChaffer

        }else{
            notificationRequest.setId(message.getData().get("id"));
            notificationRequest.setIdType(message.getData().get("idType"));
            notificationRequest.setLocalData(message.getData().get("localData"));
            notificationRequest.setLocalTime(message.getData().get("localTime"));
            notificationRequest.setPlaceName(message.getData().get("placeName"));
            notificationRequest.setPlaceAddress(message.getData().get("placeAddress"));
            notificationRequest.setNote(message.getData().get("note"));
            notificationRequest.setPrice(message.getData().get("price"));
            notificationRequest.setLatitude(message.getData().get("latitude"));
            notificationRequest.setLongitude(message.getData().get("longitude"));
            notificationRequest.setKiuwerToken(message.getData().get("myToken")); //TODO
            notificationRequest.setAccepted(Integer.parseInt(message.getData().get("accepted")));
            /*notificationRequest.setBody(message.getNotification().getBody());
            notificationRequest.setTitle(message.getNotification().getTitle());
            notificationRequest.setSound(message.getNotification().getSound());*/
            notificationRequest.setBody(message.getData().get("body"));
            notificationRequest.setTitle(message.getData().get("title"));
            notificationRequest.setSound(message.getData().get("sound"));

            Log.d(PROVA,notificationRequest.toString());
       /* String icon = message.getNotification().getIcon();
        String title = message.getNotification().getTitle();
        String text = message.getNotification().getBody();
        String sound = message.getNotification().getSound();
        String prova=message.getData().get("placeAddress");
        Log.d(PROVA, prova);

        int id = 0;
        Object obj = message.getData().get("id");
        if(obj != null)
        {id = Integer.valueOf(obj.toString());}

*/
            // String from=message.getFrom();

            sendNotificationRequest(notificationRequest);


        }


    }

    private void sendNotificationRequest(NotificationRequest notificationData)
    {

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent actionIntentShowDetails;
        PendingIntent actionPendingIntentShowDetails;

        String moreTxt = getResources().getString(R.string.more);
        String ignoreTxt = getResources().getString(R.string.ignore);
        final String contextText = this.getResources().getString(R.string.new_request_for_queue);
        final String contextTitle = this.getResources().getString(R.string.new_kiu_request);
        actionIntentShowDetails = new Intent(this, ShowRequestDetailActivity.class);
        Bundle c = new Bundle();
        c.putString(LOCALDATA, notificationData.getLocalData());
        c.putString(ID, notificationData.getId());
        c.putString(ID_TYPE, notificationData.getIdType());
        c.putString(LOCALTIME, notificationData.getLocalTime());
        c.putString(PLACEADDRESS, notificationData.getPlaceAddress());
        c.putString(PLACENAME, notificationData.getPlaceName());
        c.putString(NOTE, notificationData.getNote());
        c.putString(PRICE, notificationData.getPrice());
        c.putString(LATITUDE, notificationData.getLatitude());
        c.putString(LONGITUDE, notificationData.getLongitude());
        c.putString(KIUERTOKEN, notificationData.getKiuwerToken());

        //This is the value I want to pass
        actionIntentShowDetails.putExtras(c);
        //actionIntentShowDetails.putExtra("notificationData", notificationData.getLocalData());
        actionIntentShowDetails.setAction(""+Math.random());
        actionPendingIntentShowDetails = PendingIntent.getActivity(this,
                0, actionIntentShowDetails, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifica3)
             //   .setColor(Color.BLUE)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(actionPendingIntentShowDetails)
                .setContentText(contextText)
               // .addAction(R.drawable.ic_decline, "", actionPendingIntentShowDetails)
                //.addAction(R.drawable.ic_rinegozia, "", actionPendingIntentShowDetails)  //TODO implementazione paolo
                //.addAction(R.drawable.ic_accept,"", actionPendingIntentShowDetails)
                .setContentTitle(contextTitle)
               // .setStyle(new NotificationCompat.BigTextStyle()
                 //       .bigText(R.string.kiuwername+":"+"picio \n"+R.string.location+":"+" via milano 11, gallipoli 73014\n"+
                  //              +R.string.time+":"+"20:34\n"+R.string.date+":"+"13/04/2016\n"+R.string.rate+":euro "+"3.00"))
                .setVibrate(new long[] { 0, 500, 500, 500, 500 }) // { delay, vibrate, sleep, vibrate, sleep }
                //.setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();
        notificationManager.notify(CUSTOM_ACTION_NOTIFICATION, notification);
    }

    private void sendNotificationChaffer(NotificationChaffer notificationData)
    {

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent actionIntentShowDetails;
        PendingIntent actionPendingIntentShowDetails;

        String moreTxt = getResources().getString(R.string.more);
        String ignoreTxt = getResources().getString(R.string.ignore);
        final String contextText = notificationData.getBody();
        final String contextTitle = notificationData.getTitle();
        actionIntentShowDetails = new Intent(this, ShowRequestDetailActivity.class);
        Bundle c = new Bundle();
        c.putString(FROM, notificationData.getFROM());
        c.putString(TO, notificationData.getTO());
        c.putString(PRICE, notificationData.getPRICE());
        c.putString(BUYER_NAME, notificationData.getBUYER_NAME());
        c.putString(SELLER_NAME, notificationData.getSELLER_NAME());
        c.putString(ACCEPTED, String.valueOf(notificationData.getAccepted()));


        //This is the value I want to pass
        actionIntentShowDetails.putExtras(c);
        //actionIntentShowDetails.putExtra("notificationData", notificationData.getLocalData());
        actionIntentShowDetails.setAction(""+Math.random());
        actionPendingIntentShowDetails = PendingIntent.getActivity(this,
                0, actionIntentShowDetails, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifica3)
                //   .setColor(Color.BLUE)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(actionPendingIntentShowDetails)
                .setContentText(contextText)
                // .addAction(R.drawable.ic_decline, "", actionPendingIntentShowDetails)
                //.addAction(R.drawable.ic_rinegozia, "", actionPendingIntentShowDetails)  //TODO implementazione paolo
                //.addAction(R.drawable.ic_accept,"", actionPendingIntentShowDetails)
                .setContentTitle(contextTitle)
                // .setStyle(new NotificationCompat.BigTextStyle()
                //       .bigText(R.string.kiuwername+":"+"picio \n"+R.string.location+":"+" via milano 11, gallipoli 73014\n"+
                //              +R.string.time+":"+"20:34\n"+R.string.date+":"+"13/04/2016\n"+R.string.rate+":euro "+"3.00"))
                .setVibrate(new long[] { 0, 500, 500, 500, 500 }) // { delay, vibrate, sleep, vibrate, sleep }
                //.setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();
        notificationManager.notify(CUSTOM_ACTION_NOTIFICATION, notification);
    }

    public class NotificationChaffer extends NotificationData{

        private  String FROM ;  //il registration token del telefono da cui mandare la richiesta
        private  String TO;  //il registration token del telefono a cui mandare la richiesta
        private  String PRICE ;  //la tariffa proposta
        private  String BUYER_NAME ;  //il nome del kiuwer
        private  String SELLER_NAME ;  //il nome dell'helper

        public NotificationChaffer(String title, String body, String sound, String FROM,
                                   String SELLER_NAME, String BUYER_NAME, String PRICE, String TO,
                                   int accepted) {
            super(title, body, sound, accepted);
            this.FROM = FROM;
            this.SELLER_NAME = SELLER_NAME;
            this.BUYER_NAME = BUYER_NAME;
            this.PRICE = PRICE;
            this.TO = TO;
        }

        public NotificationChaffer(){
            super();
        }

        public String getFROM() {
            return FROM;
        }

        public void setFROM(String FROM) {
            this.FROM = FROM;
        }

        public String getTO() {
            return TO;
        }

        public void setTO(String TO) {
            this.TO = TO;
        }

        public String getPRICE() {
            return PRICE;
        }

        public void setPRICE(String PRICE) {
            this.PRICE = PRICE;
        }

        public String getBUYER_NAME() {
            return BUYER_NAME;
        }

        public void setBUYER_NAME(String BUYER_NAME) {
            this.BUYER_NAME = BUYER_NAME;
        }

        public String getSELLER_NAME() {
            return SELLER_NAME;
        }

        public void setSELLER_NAME(String SELLER_NAME) {
            this.SELLER_NAME = SELLER_NAME;
        }


        @Override
        public String toString() {
            return "NotificationChaffer{" +
                    "FROM='" + FROM + '\'' +
                    ", TO='" + TO + '\'' +
                    ", PRICE='" + PRICE + '\'' +
                    ", BUYER_NAME='" + BUYER_NAME + '\'' +
                    ", SELLER_NAME='" + SELLER_NAME + '\'' +
                    '}';
        }
    }

    public class NotificationRequest extends NotificationData{

        private String latitude;
        private String longitude;
        private String localData;
        private String localTime;
        private String placeName;
        private String placeAddress;
        private String note;
        private String id;
        private String idType;
        private String kiuwerToken;
        private String helperToken;
        private String price;

        public NotificationRequest(){
            super();
        }

        public NotificationRequest(String title, String body, String sound, String latitude,
                                   String longitude, String localData, String localTime,
                                   String placeName, String placeAddress, String note, String id,
                                   String idType, String kiuwerToken, String helperToken,
                                   String price, int accepted) {
            super(title, body, sound, accepted);
            this.latitude = latitude;
            this.longitude = longitude;
            this.localData = localData;
            this.localTime = localTime;
            this.placeName = placeName;
            this.placeAddress = placeAddress;
            this.note = note;
            this.id = id;
            this.idType = idType;
            this.kiuwerToken = kiuwerToken;
            this.helperToken = helperToken;
            this.price = price;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {this.idType = idType;}

        public String getKiuwerToken() {
            return kiuwerToken;
        }

        public void setKiuwerToken(String kiuwerToken) {
            this.kiuwerToken = kiuwerToken;
        }

        public String getHelperToken() {
            return helperToken;
        }

        public void setHelperToken(String helperToken) {
            this.helperToken = helperToken;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "NotificationRequest{" +
                    "latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", localData='" + localData + '\'' +
                    ", localTime='" + localTime + '\'' +
                    ", placeName='" + placeName + '\'' +
                    ", placeAddress='" + placeAddress + '\'' +
                    ", note='" + note + '\'' +
                    ", id='" + id + '\'' +
                    ", kiuwerToken='" + kiuwerToken + '\'' +
                    ", helperToken='" + helperToken + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }
    }

/*
    public class NotificationData implements Serializable
    {
        public static final String TEXT = "TEXT";

        private String title;
        private String body;
        private String sound;
        private String latitude;
        private String longitude;
        private String localData;
        private String localTime;
        private String placeName;
        private String placeAddress;
        private String note;
        private String id;
        private String kiuwerToken;
        private String helperToken;
        private String price;


        public NotificationData(String title, String body, String sound, String latitude,
                                String longitude, String localData, String localTime,
                                String placeName, String placeAddress, String note, String id,
                                String kiuwerToken, String price)
        {
            this.title = title;
            this.body = body;
            this.sound = sound;
            this.latitude = latitude;
            this.longitude = longitude;
            this.localData = localData;
            this.localTime = localTime;
            this.placeName = placeName;
            this.placeAddress = placeAddress;
            this.note = note;
            this.id = id;
            this.kiuwerToken = kiuwerToken;
            this.price=price;
        }

        public NotificationData(){}

        public String getTitle() {return title;}

        public void setTitle(String title) {this.title = title;}

        public String getBody() {return body;}

        public void setBody(String body) {this.body = body;}

        public String getSound() {return sound;}

        public void setSound(String sound) {this.sound = sound;}

        public String getLatitude() {return latitude;}

        public void setLatitude(String latitude) {this.latitude = latitude;}

        public String getLongitude() {return longitude;}

        public void setLongitude(String longitude) {this.longitude = longitude;}

        public String getLocalData() {return localData;}

        public void setLocalData(String localData) {this.localData = localData;}

        public String getLocalTime() {return localTime;}

        public void setLocalTime(String localTime) {this.localTime = localTime;}

        public String getPlaceName() {return placeName;}

        public void setPlaceName(String placeName) {this.placeName = placeName;}

        public String getPlaceAddress() {return placeAddress;}

        public void setPlaceAddress(String placeAddress) {this.placeAddress = placeAddress;}

        public String getNote() {return note;}

        public void setNote(String note) {this.note = note;}

        public String getId() {return id;}

        public void setId(String id) {this.id = id;}

        public String getKiuwerToken() {return kiuwerToken;}

        public void setKiuwerToken(String kiuwerToken) {this.kiuwerToken = kiuwerToken;}

        public String getPrice(){return price;}

        public void setPrice(String price){this.price=price;}


        @Override
        public String toString() {
            return "NotificationData{" +
                    "title='" + title + '\'' +
                    ", body='" + body + '\'' +
                    ", sound='" + sound + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", localData='" + localData + '\'' +
                    ", localTime='" + localTime + '\'' +
                    ", placeName='" + placeName + '\'' +
                    ", placeAddress='" + placeAddress + '\'' +
                    ", note='" + note + '\'' +
                    ", id='" + id + '\'' +
                    ", kiuwerToken='" + kiuwerToken + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }

    }
    */
}
