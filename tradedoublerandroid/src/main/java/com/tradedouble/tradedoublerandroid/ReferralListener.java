package com.tradedouble.tradedoublerandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tradedouble.tradedoublerandroid.network.HttpRequest;
import com.tradedouble.tradedoublerandroid.network.NetClient;
import com.tradedouble.tradedoublerandroid.network.ResultRequest;

import java.io.IOException;

public class ReferralListener extends BroadcastReceiver {
    private ApplicationSettings settings;
    private Context context = null;

    public ReferralListener(Context context) throws PackageManager.NameNotFoundException {
        this.context = context;
        settings = new ApplicationSettings(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String referrer = intent.getStringExtra("referrer");

        String organizationId = settings.getOrganizationId();
        String tudid = settings.getTduidValue();
        String userEmail = settings.getUserEmail();
        String googleAdvertisingId = settings.getGoogleAdvertisingId();

        if (userEmail != null && !userEmail.isEmpty()){
            String url  = HttpRequest.trackingOpen(organizationId, userEmail, tudid, "1");
            try {

                NetClient.getNetClient().callResponse(url, new ResultRequest() {
                    @Override
                    public void onFailure(int code) {

                        Log.e("Response Error", "Problem with reqest" + code);
                    }

                    @Override
                    public void onResponseSuccess(int code) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String url = HttpRequest.trackingOpen(organizationId, googleAdvertisingId, tudid,"0");
        try {

            NetClient.getNetClient().callResponse(url, new ResultRequest() {
                @Override
                public void onFailure(int code) {

                    Log.e("Response Error", "Problem with reqest" + code);
                }

                @Override
                public void onResponseSuccess(int code) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
