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

class TraderDoublerSdk private constructor(context: Context?) {

    companion object {
        private var context: Context? = null
        lateinit var settings: ApplicationSettings
        private var networkConnection: NetworkConnection? = null

        @Volatile
        private var instance: TraderDoublerSdk? = null

        fun getInstance(): TraderDoublerSdk? {
            if (instance == null) {
                synchronized(TraderDoublerSdk::class.java) {
                    if (instance == null) {
                        return TraderDoublerSdk(context)
                    }
                }
            }
            return instance
        }

        fun create(context: Context): TraderDoublerSdk? {
            if (instance == null) {
                synchronized(TraderDoublerSdk::class.java) {
                    if (instance == null) {
                        instance = TraderDoublerSdk(context)
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
        settings.storeTduid(tduid)
    }

    var organizationId: String?
        get() = settings.organizationId
        set(organizationId) {
            settings.storeOrganizationId(organizationId)
        }

    val tudid: String?
        get() = settings.tduidValue

    var userEmail: String?
        get() = settings.userEmail
        set(userEmail) {
            if (userEmail != null && userEmail.isNotEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    val generateSHA56HashEmail =
                        TraderDoublerSdkUtils.generateSHA56Hash(userEmail)
                    settings.storeUserEmail(generateSHA56HashEmail)
                }
            }
        }

    var googleAdvertisingId: String?
        get() = settings.googleAdvertisingId
        set(googleAdvertisingId) {
            val generateSHA56Hash = TraderDoublerSdkUtils.generateSHA56Hash(googleAdvertisingId)
            settings.storeGoogleAdvertisingId(generateSHA56Hash)
        }

    var currencyValue: String? = null
        get() = currencyValue
        set(value) {
            field = currencyValue
        }

    var leadEventId: String? = null
        get() = leadEventId
        set(value) {
            field = leadEventId
        }

    var leadId: String? = null
        get() = leadId
        set(value) {
            field = leadId
        }

    var saleEventId: String? = null
        get() = saleEventId
        set(value) {
            field = saleEventId
        }

    var orderNumber: String? = null
        get() = orderNumber
        set(value) {
            field = orderNumber
        }

    var orderValue: String? = null
        get() = orderValue
        set(value) {
            field = orderValue
        }

    var voucherCode: String? = null
        get() = voucherCode
        set(value) {
            field = voucherCode
        }

    var reportInfo: String? = null
        get() = reportInfo
        set(value) {
            field = reportInfo
        }


    var pltEventId: String? = null
        get() = pltEventId
        set(value) {
            field = pltEventId
        }

    var orderId: String? = null
        get() = orderId
        set(value) {
            field = orderId
        }

    var basket: String? = null
        get() = basket
        set(value) {
            field = orderId
        }

    var appInstallEventId:String ? = null
        get() = appInstallEventId
        set(value) {
            field = appInstallEventId
        }


    fun callTrackingInstallation(
        appInstallEventId: String,
        leadNumber: String,
        tduid: String?
    ) {
        val organizationId = settings.organizationId
        val userEmail = settings.userEmail
        val googleAdvertisingId =
            settings.googleAdvertisingId

        if (userEmail != null && userEmail.isNotEmpty()) {
            val url = HttpRequest.trackingInstallation(
                organizationId,
                appInstallEventId = appInstallEventId,
                leadNumber = leadNumber,
                tduid = tduid,
                extId = userEmail
            )
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

        val url = HttpRequest.trackingInstallation(
            organizationId,
            appInstallEventId = appInstallEventId,
            leadNumber = leadNumber,
            tduid = tduid,
            extId = googleAdvertisingId
        )
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


    fun callTrackingOpenURl() {

        val organizationId = settings.organizationId
        val tudid = settings.tduidValue
        val userEmail = settings.userEmail
        val googleAdvertisingId =
            settings.googleAdvertisingId
        if (userEmail != null && userEmail.isNotEmpty()) {
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

    fun generateTUDID() {
        val url = HttpRequest.clickLinkAffiliate()
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


    fun callTrackingLead() {

        val organizationId = settings.organizationId
        val tudid = settings.tduidValue
        val userEmail = settings.userEmail
        val googleAdvertisingId =
            settings.googleAdvertisingId

        val url = HttpRequest.trackingLead(
            organizationId = organizationId,
            leadEventId = "403765",
            leadId = "5",
            tduid = tudid,
            extId = googleAdvertisingId
        )
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

        if (userEmail != null && userEmail.isNotEmpty()) {
            val url = HttpRequest.trackingLead(
                organizationId = organizationId,
                leadEventId = "403765",
                leadId = "5",
                tduid = tudid,
                extId = userEmail
            )

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


    fun callTrackingSale() {
        val organizationId = settings.organizationId
        val tudid = settings.tduidValue
        val userEmail = settings.userEmail
        val googleAdvertisingId =
            settings.googleAdvertisingId


        val url = HttpRequest.trackingSale(
            organizationId = organizationId,
            saleEventId = "403759",
            orderNumber = "15645654",
            orderValue = "81830",
            currency = "EUR",
            voucherCode = null,
            tduid = tudid,
            extId = googleAdvertisingId,
            reportInfo = null,
            secretCode = "123456789"
        )

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


        if (userEmail != null && userEmail.isNotEmpty()) {
            val url = HttpRequest.trackingSale(
                organizationId = organizationId,
                saleEventId = "",
                orderNumber = "",
                orderValue = "",
                currency = "",
                voucherCode = "",
                tduid = tudid,
                extId = userEmail,
                reportInfo = "",
                secretCode = "123456789"
            )

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


}