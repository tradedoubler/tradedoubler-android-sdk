package com.tradedouble.tradedoublerandroid

import android.annotation.SuppressLint
import android.content.Context

internal class TradeDoublerSdkSettings(private val context: Context) {

    companion object{
        private const val TDUIC_VALUE = "TDUDID_VLAUE"
        private const val GAID_VALUE = "GAID_VALUE"
        private const val WAS_INSTALL_TRACKED = "WAS_INSTALL_TRACKED"
        private const val USER_EMAIL_VALUE = "USER_EMAIL"
        private const val SECRET_CODE_VALUE = "SECRET_CODE"
        private const val ORGANIZATION_ID_VALUE = "ORGANIZATION_ID_VALUE"
        private const val TRACKING_FILE = "td-app-download-tracking.dat"
        private const val DEFAULT_LIFE_TIME_VALUE_DAYS = 365L
        private const val LTV_EXPIRY = "ltvExpiry"
    }

    private val settings = context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)

    val tduid: String?
        get() {
            return settings.getString(TDUIC_VALUE, null)
        }

    val organizationId: String?
        get() {
            return settings.getString(ORGANIZATION_ID_VALUE, null)
        }

    val userEmail: String?
        get() {
            return settings.getString(USER_EMAIL_VALUE, null)
        }

    val secretCode: String?
        get() {
            return settings.getString(SECRET_CODE_VALUE, null)
        }

    val deviceIdentifier: String?
        get() {
            return settings.getString(GAID_VALUE, null)
        }

    val wasInstallTracked: Boolean?
        get() {
            return settings.getBoolean(WAS_INSTALL_TRACKED, false)
        }

    fun storeTduid(tduid: String?) {
        val editor = settings.edit()
        editor.putString(TDUIC_VALUE, tduid)
        editor.commit()
    }

    fun storeDeviceIdentifier(googleAdvertisingId: String?) {
        val editor = settings.edit()
        if (googleAdvertisingId != null) editor.putString(GAID_VALUE, googleAdvertisingId)
        editor.commit()
    }

    fun storeOrganizationId(organizationId: String?) {
        val editor = settings.edit()
        if (organizationId != null) editor.putString(ORGANIZATION_ID_VALUE, organizationId)
        editor.commit()
    }

    fun storeUserEmail(userEmail: String?) {
        val editor = settings.edit()
        if (userEmail != null) editor.putString(USER_EMAIL_VALUE, userEmail)
        editor.commit()
    }

    val ltvExpiry: String?
        get() {
            return settings.getString(LTV_EXPIRY, "")
        }

    fun calculateLtvExpiry(ltvDays: Long): Long {
        var days = ltvDays
        if (days <= 0) {
            days = DEFAULT_LIFE_TIME_VALUE_DAYS
        }
        return System.currentTimeMillis() + days * 24 * 60 * 60 * 1000
    }

    @SuppressLint("ApplySharedPref")
    fun setSecretCode(secretCode: String?) {
        val editor = settings.edit()
        editor.putString(SECRET_CODE_VALUE, secretCode)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun clearParameters() {
        settings.edit().apply {
            remove(USER_EMAIL_VALUE)
            remove(TDUIC_VALUE)
            remove(ORGANIZATION_ID_VALUE)
            remove(GAID_VALUE)
        }.commit()
    }


}