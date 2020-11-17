package com.tradedouble.tradedoublerandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import static com.tradedouble.tradedoublerandroid.utils.Constans.DEFAULT_LIFE_TIME_VALUE_DAYS;
import static com.tradedouble.tradedoublerandroid.utils.Constans.GAID_VALUE;
import static com.tradedouble.tradedoublerandroid.utils.Constans.LTV_EXPIRY;
import static com.tradedouble.tradedoublerandroid.utils.Constans.TDUIC_VALUE;
import static com.tradedouble.tradedoublerandroid.utils.Constans.TRACKING_FILE;

public class ApplicationSettings {

    private Context context;

    public ApplicationSettings(Context context) throws PackageManager.NameNotFoundException {
        this.context = context;
    }


    public void storeTduid(String tduidValue) {
        SharedPreferences settings = context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        if(tduidValue != null) editor.putString(TDUIC_VALUE, tduidValue);

        editor.apply();
    }

    public void storeGoogleAdvertisingId(String googleAdvertisingId){
        SharedPreferences settings = context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        if(googleAdvertisingId != null) editor.putString(GAID_VALUE, googleAdvertisingId);
        editor.apply();
    }


    public String getLtvExpiry() {
        SharedPreferences settings;
        settings = context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE);

        if(settings != null) {
            return settings.getString(LTV_EXPIRY, "");
        } else {
            return null;
        }
    }

    public long calculateLtvExpiry(long ltvDays) {
        long days = ltvDays;
        if(days <= 0) {
            days = DEFAULT_LIFE_TIME_VALUE_DAYS;
        }
        return System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000);
    }


    public String getTduidValue() {
        SharedPreferences settings = context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE);
        return settings.getString(TDUIC_VALUE, null);
    }

    public String getGoogleAdvertisingId() {
        SharedPreferences settings = context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE);
        return settings.getString(GAID_VALUE, null);
    }

}
