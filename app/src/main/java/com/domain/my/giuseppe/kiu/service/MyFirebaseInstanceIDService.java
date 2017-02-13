package com.domain.my.giuseppe.kiu.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.domain.my.giuseppe.kiu.model.User;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        //sendRegistrationToServer(refreshedToken);
        //MainActivity.currentUserIstance.setRegistration_token(refreshedToken);

        //Salvo il nuovo token nelle preferences per settarlo sul database remoto in mainactivity dopo il login
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id", refreshedToken);
        editor.apply();

        //TODO da testare
        if (user != null) {
            sendRegistrationToServer(refreshedToken);
        }
//        MainActivity.currentUserIstance.setRegistration_token(refreshedToken);
    }

    /**
     * manda il nuovo token al database
     *
     * @param newToken nuovo tken da inviare al database
     */
    private void sendRegistrationToServer(String newToken) {
        RemoteDBAdapter remoteDBAdapter = new RemoteDBAdapter();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String username = User.getUserName(user.getEmail());
        remoteDBAdapter.updateRegistrationToken(username, newToken);
    }
}
