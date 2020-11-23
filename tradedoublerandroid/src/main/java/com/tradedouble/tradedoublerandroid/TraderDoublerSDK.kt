package com.tradedouble.tradedoublerandroid

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.util.Patterns
import com.tradedouble.tradedoublerandroid.network.HttpRequest
import com.tradedouble.tradedoublerandroid.network.NetClient
import com.tradedouble.tradedoublerandroid.network.NetworkConnection
import com.tradedouble.tradedoublerandroid.network.ResultRequest
import java.io.IOException

class TraderDoublerSDK private constructor(context: Context?) {

    companion object {
        private var context: Context? = null
        private var settings: ApplicationSettings? = null
        private var networkConnection: NetworkConnection? = null

        @Volatile
        private var instance: TraderDoublerSDK? = null

        fun getInstance(): TraderDoublerSDK? {
            if (instance == null) {
                synchronized(TraderDoublerSDK::class.java) {
                    if (instance == null) {
                        return TraderDoublerSDK(context)
                    }
                }
            }
            return instance
        }

        fun create(context: Context): TraderDoublerSDK? {
            if (instance == null) {
                synchronized(TraderDoublerSDK::class.java) {
                    if (instance == null) {
                        instance = TraderDoublerSDK(context)
                    }
                }
            }
            return instance
        }
    }

    init {
        init(context)
    }

    @Synchronized
    fun init(ctx: Context?) {
        if (context == null) {
            context = ctx!!.applicationContext
            context?.let {
                try {
                    settings =
                        ApplicationSettings(it)
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                networkConnection =
                    NetworkConnection(it)
            }
        }
    }

    fun setTduid(tduid: String?) {
        settings!!.storeTduid(tduid)
    }

    var organizationId: String?
        get() = settings!!.organizationId
        set(organizationId) {
            settings!!.storeOrganizationId(organizationId)
        }

    val tudid: String?
        get() = settings!!.tduidValue

    var userEmail: String?
        get() = settings!!.userEmail
        set(userEmail) {
            if (userEmail != null && userEmail.isNotEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    val generateSHA56HashEmail =
                        Cryptography.generateSHA56Hash(userEmail)
                    settings!!.storeUserEmail(generateSHA56HashEmail)
                }
            }
        }

    var googleAdvertisingId: String?
        get() = settings!!.googleAdvertisingId
        set(googleAdvertisingId) {
            val generateSHA56Hash = Cryptography.generateSHA56Hash(googleAdvertisingId)
            settings!!.storeGoogleAdvertisingId(generateSHA56Hash)
        }

    fun trackingOpenURl() {
        val organizationId = settings!!.organizationId
        val tudid = settings!!.tduidValue
        val userEmail = settings!!.userEmail
        val googleAdvertisingId =
            settings!!.googleAdvertisingId
        if (userEmail != null && !userEmail.isEmpty()) {
            val url = HttpRequest.trackingOpen(organizationId, userEmail, tudid, "1")
            try {
                NetClient.netClient
                    ?.callResponse(url, object : ResultRequest {
                        override fun onFailure(code: Int) {
                            Log.e("Response Error", "Problem with reqest$code")
                        }

                        override fun onResponseSuccess(code: Int, responseBody: String?) {}
                    })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val url =
            HttpRequest.trackingOpen(organizationId, googleAdvertisingId, tudid, "0")
        try {
            NetClient.netClient
                ?.callResponse(url, object : ResultRequest {
                    override fun onFailure(code: Int) {
                        Log.e("Response Error", "Problem with reqest$code")
                    }

                    override fun onResponseSuccess(code: Int, responseBody: String?) {}
                })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}