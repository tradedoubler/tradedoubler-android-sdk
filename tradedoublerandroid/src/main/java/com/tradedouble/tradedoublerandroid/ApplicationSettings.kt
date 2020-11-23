package com.tradedouble.tradedoublerandroid

import android.content.Context
import android.content.SharedPreferences
import com.tradedouble.tradedoublerandroid.utils.Constant.DEFAULT_LIFE_TIME_VALUE_DAYS
import com.tradedouble.tradedoublerandroid.utils.Constant.GAID_VALUE
import com.tradedouble.tradedoublerandroid.utils.Constant.LTV_EXPIRY
import com.tradedouble.tradedoublerandroid.utils.Constant.ORGANIZATION_ID_VALUE
import com.tradedouble.tradedoublerandroid.utils.Constant.TDUIC_VALUE
import com.tradedouble.tradedoublerandroid.utils.Constant.TRACKING_FILE
import com.tradedouble.tradedoublerandroid.utils.Constant.USER_EMAIL_VALUE

class ApplicationSettings(private val context: Context) {

    fun storeTduid(tduidValue: String?) {
        val settings =
            context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
        val editor = settings.edit()
        val previousTduidValue = tduidValue
        if (tduidValue != null) {
            if (tduidValue != previousTduidValue) {
                editor.putString(TDUIC_VALUE, tduidValue)
            }
        }
        editor.apply()
    }

    fun storeGoogleAdvertisingId(googleAdvertisingId: String?) {
        val settings =
            context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
        val editor = settings.edit()
        if (googleAdvertisingId != null) editor.putString(GAID_VALUE, googleAdvertisingId)
        editor.apply()
    }

    fun storeOrganizationId(organizationId: String?) {
        val settings =
            context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
        val editor = settings.edit()
        if (organizationId != null) editor.putString(ORGANIZATION_ID_VALUE, organizationId)
        editor.apply()
    }

    fun storeUserEmail(userEmail: String?) {
        val settings =
            context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
        val editor = settings.edit()
        if (userEmail != null) editor.putString(USER_EMAIL_VALUE, userEmail)
        editor.apply()
    }

    val ltvExpiry: String?
        get() {
            val settings= context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
            return settings.getString(LTV_EXPIRY, "")
        }

    fun calculateLtvExpiry(ltvDays: Long): Long {
        var days = ltvDays
        if (days <= 0) {
            days = DEFAULT_LIFE_TIME_VALUE_DAYS
        }
        return System.currentTimeMillis() + days * 24 * 60 * 60 * 1000
    }

    val tduidValue: String?
        get() {
            val settings =
                context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
            return settings.getString(TDUIC_VALUE, null)
        }

    val organizationId: String?
        get() {
            val settings =
                context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
            return settings.getString(ORGANIZATION_ID_VALUE, null)
        }

    val userEmail: String?
        get() {
            val settings =
                context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
            return settings.getString(USER_EMAIL_VALUE, null)
        }

    val googleAdvertisingId: String?
        get() {
            val settings =
                context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)
            return settings.getString(GAID_VALUE, null)
        }

}