package com.tradedouble.tradedoublerandroid;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Patterns;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.regex.Pattern;

public class TraderDoublerSDK {

    private static Context context;
    private static ApplicationSettings settings;
    private static NetworkConnection networkConnection;
    private static volatile TraderDoublerSDK instance;


    public static TraderDoublerSDK getInstance() {
        if (instance == null) {
            synchronized (TraderDoublerSDK.class) {
                if (instance == null) {
                    return new TraderDoublerSDK(context);
                }
            }
        }
        return instance;
    }

    private TraderDoublerSDK(Context context) {
        init(context);
    }


    public synchronized void init(final Context ctx) {
        if (context == null) {
            context = ctx.getApplicationContext();
            try {
                settings = new ApplicationSettings(context);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            networkConnection = new NetworkConnection(context);
            callResponse();
        }
    }

    public static TraderDoublerSDK createLibrary(@NonNull final Context context) {
        if (instance == null) {
            synchronized (TraderDoublerSDK.class) {
                if (instance == null) {
                    instance = new TraderDoublerSDK(context);
                }
            }
        }
        return instance;
    }

    public void setTduid(String tduid) {
        settings.storeTduid(tduid);
    }

    public void setGoogleAdvertisingId(String googleAdvertisingId) {
        String generateSHA56Hash = Cryptography.generateSHA56Hash(googleAdvertisingId);
        settings.storeGoogleAdvertisingId(generateSHA56Hash);
    }

    public void setUserEmail(String userEmail) {
        if (userEmail != null && userEmail.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                String generateSHA56HashEmail = Cryptography.generateSHA56Hash(userEmail);
                settings.storeUserEmail(generateSHA56HashEmail);
            }
        }
    }

    public void setOrganizationId(String organizationId) {
        settings.storeOrganizationId(organizationId);
    }

    private String getOrganizationId() {
        return settings.getOrganizationId();
    }

    private String getTudid() {
        return settings.getTduidValue();
    }

    private String getUserEmail() {
        return settings.getUserEmail();
    }

    private String getGoogleAdvertisingId() {
        return settings.getGoogleAdvertisingId();
    }
    
    private void callResponse() {

        String organizationId = getOrganizationId();
        String tudid = getTudid();
        String userEmail = getUserEmail();
        String googleAdvertisingId = getGoogleAdvertisingId();


        String url = HttpRequest.trackingOpen(organizationId, googleAdvertisingId, tudid);
        try {
            NetClient.getNetClient().callResponse(url, new ResultRequest() {
                @Override
                public void onFailure(int code) {

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
