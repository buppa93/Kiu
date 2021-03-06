package com.domain.my.giuseppe.kiu.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.domain.my.giuseppe.kiu.kiuwer.Kiuwer;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "MyFirebaseIDService";

    @Override
    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Refreshed token: " + refreshedToken);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        //sendRegistrationToServer(refreshedToken);
        //Kiuwer.currentUserIstance.setRegistration_token(refreshedToken);

        //Salvo il nuovo token nelle preferences per settarlo sul database in kiuer.class dopo il login
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id", refreshedToken);
        editor.apply();

        //TODO da testare
        if(user!=null){
            sendRegistrationToServer(refreshedToken);
        }
//        Kiuwer.currentUserIstance.setRegistration_token(refreshedToken);
    }

    private void sendRegistrationToServer(String newToken)
    {
        RemoteDBAdapter remoteDBAdapter = new RemoteDBAdapter();
        remoteDBAdapter.updateRegistrationToken(Kiuwer.currentUserIstance, newToken);
    }
}
