/*
 * Copyright 2020 Tradedoubler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tradedoubler.sdk

import android.annotation.SuppressLint
import android.content.Context

internal class TradeDoublerSdkSettings(private val context: Context) {

    companion object{
        private const val TDUIC_VALUE = "TDUDID_VLAUE"
        private const val GAID_VALUE = "GAID_VALUE"
        private const val WAS_INSTALL_TRACKED = "WAS_INSTALL_TRACKED"
        private const val WAS_INSTALL_TDUID_RETRIEVED = "WAS_INSTALL_TDUID_RETRIEVED"
        private const val WAS_ADVERTISING_ID_RETRIEVED = "WAS_ADVERTISING_ID_RETRIEVED"
        private const val USER_EMAIL_VALUE = "USER_EMAIL"
        private const val SECRET_CODE_VALUE = "SECRET_CODE"
        private const val ORGANIZATION_ID_VALUE = "ORGANIZATION_ID_VALUE"
        private const val TRACKING_FILE = "td-app-download-tracking.dat"
        private const val DEFAULT_LIFE_TIME_VALUE_DAYS = 365L
        internal const val LTV_EXPIRY = "ltvExpiry"
    }

    internal val settings = context.getSharedPreferences(TRACKING_FILE, Context.MODE_PRIVATE)

    val tduid: String?
        @SuppressLint("ApplySharedPref")
        get() {
            if(tduidExpireTime < System.currentTimeMillis()){
                settings.edit().apply {
                    remove(TDUIC_VALUE)
                    remove(LTV_EXPIRY)
                }.commit()
            }
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

    val advertisingIdentifier: String?
        get() {
            return settings.getString(GAID_VALUE, null)
        }

    val wasInstallTracked: Boolean
        get() {
            return settings.getBoolean(WAS_INSTALL_TRACKED, false)
        }

    val wasInstallTduidInvoked: Boolean
        get() {
            return settings.getBoolean(WAS_INSTALL_TDUID_RETRIEVED, false)
        }

    val wasAndroidIdRetrieved: Boolean
        get() {
            return settings.getBoolean(WAS_ADVERTISING_ID_RETRIEVED, false)
        }


    private val tduidExpireTime: Long
        get() {
            return settings.getLong(LTV_EXPIRY,calculateLtvExpiry())
        }

    fun storeTduid(tduid: String?) {
        val editor = settings.edit()
        if(this.tduid != tduid){
            editor.putLong(LTV_EXPIRY,calculateLtvExpiry())
        }
        editor.putString(TDUIC_VALUE, tduid)
        editor.commit()
    }

    fun storeAdvertisingIdentifier(googleAdvertisingId: String?) {
        val editor = settings.edit()
        editor.putString(GAID_VALUE, googleAdvertisingId)
        editor.commit()
    }

    fun storeOrganizationId(organizationId: String?) {
        val editor = settings.edit()
        editor.putString(ORGANIZATION_ID_VALUE, organizationId)
        editor.commit()
    }

    fun storeUserEmail(userEmail: String?) {
        val editor = settings.edit()
        editor.putString(USER_EMAIL_VALUE, userEmail)
        editor.commit()
    }

    fun setInstallTracked(wasInstallTracked: Boolean) {
        val editor = settings.edit()
        editor.putBoolean(WAS_INSTALL_TRACKED, wasInstallTracked)
        editor.commit()
    }

    fun setInstallReferrerChecked(wasInstallReferrerChecked: Boolean) {
        val editor = settings.edit()
        editor.putBoolean(WAS_INSTALL_TDUID_RETRIEVED, wasInstallReferrerChecked)
        editor.commit()
    }

    private fun calculateLtvExpiry(): Long {
        return System.currentTimeMillis() + DEFAULT_LIFE_TIME_VALUE_DAYS * 24 * 60 * 60 * 1000
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