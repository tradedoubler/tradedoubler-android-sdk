package com.tradedouble.tradedoublerandroid;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Patterns;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;

public class TraderDoublerSDK {

    private static ApplicationSettings settings;
    private static Context context;
    private static NetworkConnection networkConnection;
    private static volatile TraderDoublerSDK instance;

    private String organizationId;

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
    }


    public static synchronized void init(final Context ctx) {
        if (context == null) {
            context = ctx.getApplicationContext();
            try {
                settings = new ApplicationSettings(context);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static TraderDoublerSDK create(@NonNull final Context context) {
        if (instance == null) {
            synchronized (TraderDoublerSDK.class) {
                if (instance == null) {
                    instance = new TraderDoublerSDK(context);
                    try {
                        settings = new ApplicationSettings(context);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    networkConnection = new NetworkConnection(context);
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
        return organizationId;
    }

    private String getTudid() {
        return settings.getTduidValue();
    }

    public String getUserEmail() {
        return settings.getUserEmail();
    }

    public String getGoogleAdvertisingId() {
        return settings.getGoogleAdvertisingId();
    }


    private void callResponse(){

        String organizationId = getOrganizationId();
        String tudid = getTudid();
        String userEmail = getUserEmail();
        String googleAdvertisingId = getGoogleAdvertisingId();

        if (userEmail )


    }

}
