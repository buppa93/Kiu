package com.domain.my.giuseppe.kiu.service;

import android.util.Log;

import com.domain.my.giuseppe.kiu.kiuwer.Kiuwer;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by giuseppe on 17/08/16.
 */
public class FirebaseIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
        Kiuwer.currentUserIstance.setRegistration_token(refreshedToken);
    }

    private void sendRegistrationToServer(String newToken)
    {
        RemoteDBAdapter remoteDBAdapter = new RemoteDBAdapter();
        remoteDBAdapter.updateRegistrationToken(Kiuwer.currentUserIstance, newToken);
    }
}
