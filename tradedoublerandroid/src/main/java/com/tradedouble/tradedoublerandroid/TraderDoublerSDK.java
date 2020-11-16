package com.tradedouble.tradedoublerandroid;

import android.content.Context;

import androidx.annotation.NonNull;

public class TraderDoublerSDK {

    private static Context context;

    private static volatile TraderDoublerSDK instance;

    public String tudid;

    public String googleAdvertisingId;

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
        }
    }

    public static TraderDoublerSDK create(@NonNull final Context context) {
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
        this.tudid = tduid;
    }


    public void setGoogleAdvertisingId(String googleAdvertisingId) {
        this.googleAdvertisingId = googleAdvertisingId;
    }


}
