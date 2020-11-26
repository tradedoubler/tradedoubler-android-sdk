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

class TraderDoublerSdk constructor(context: Context?) {

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

    var organizationId: String?
        get() = settings.organizationId
        set(organizationId) {
            settings.storeOrganizationId(organizationId)
        }

    var tudid: String?
        get() = settings.tduidValue
        set(value) {
            settings.storeTduid(tudid)
        }

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

    var appInstallEventId: String? = null
        get() = appInstallEventId
        set(value) {
            field = appInstallEventId
        }


    fun callTrackingInstallation(
        appInstallEventId: String,
        appDateInstall: String,
        tduid: String?
    ) {
        val organizationId = settings.organizationId
        val userEmail = settings.userEmail
        val googleAdvertisingId =
            settings.googleAdvertisingId

        val leadNumber = TraderDoublerSdkUtils.getRandomString() + appDateInstall


        val isAllParametersComplete = (!organizationId.isNullOrBlank() && !tudid.isNullOrEmpty() && !userEmail.isNullOrEmpty() && !googleAdvertisingId.isNullOrEmpty() && !leadNumber.isNullOrEmpty() && !appInstallEventId.isNullOrEmpty() && !appDateInstall.isNullOrEmpty() && !tduid.isNullOrEmpty())

        if (isAllParametersComplete ){

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
                            override fun onFailure(code: Int, errorMessage: String?) {
                                Log.e(
                                    "Response Error",
                                    "Problem with request url $url  response  code $code and response error message $errorMessage"
                                )
                            }

                            override fun onResponseSuccess(code: Int, responseBody: String?) {
                                Log.e(
                                    "Response Success",
                                    "Request is done. Url :$url status code  $code and response body $responseBody"
                                )
                            }
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
                        override fun onFailure(code: Int, errorMessage: String?) {
                            Log.e(
                                "Response Error",
                                "Problem with request url $url  response  code $code and response error message $errorMessage"
                            )
                        }

                        override fun onResponseSuccess(code: Int, responseBody: String?) {
                            Log.e(
                                "Response Success",
                                "Request is done. Url :$url status code  $code and response body $responseBody"
                            )
                        }
                    })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Log.e(
                "Missing Parameters ",
                " Please complete parameters because problem with call response"
            )
        }

    }


    fun callTrackingOpenURl() {

        val organizationId = settings.organizationId
        val tudid = settings.tduidValue
        val userEmail = settings.userEmail
        val googleAdvertisingId =
            settings.googleAdvertisingId


        val isAllParametersComplete =
            (!organizationId.isNullOrBlank() && !tudid.isNullOrEmpty() && !userEmail.isNullOrEmpty() && !googleAdvertisingId.isNullOrEmpty())

        if (isAllParametersComplete) {

            if (userEmail != null && userEmail.isNotEmpty()) {
                val url = HttpRequest.trackingOpen(organizationId, userEmail, tudid)
                try {
                    NetClient.netClient
                        ?.callResponse(url, object : ResultRequest {
                            override fun onFailure(code: Int, errorMessage: String?) {
                                Log.e(
                                    "Response Error",
                                    "Problem with request url $url  response  code $code and response error message $errorMessage"
                                )
                            }

                            override fun onResponseSuccess(code: Int, responseBody: String?) {
                                Log.e(
                                    "Response Success",
                                    "Request is done. Url :$url status code  $code and response body $responseBody"
                                )
                            }
                        })
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(
                        "Problem with call url ",
                        " Problem with call response url $url  error ${e.message}"
                    )
                }
            }

            val url =
                HttpRequest.trackingOpen(organizationId, googleAdvertisingId, tudid)

            try {
                NetClient.netClient
                    ?.callResponse(url, object : ResultRequest {
                        override fun onFailure(code: Int, errorMessage: String?) {
                            Log.e(
                                "Response Error",
                                "Problem with request url $url  response  code $code and response error message $errorMessage"
                            )
                        }

                        override fun onResponseSuccess(code: Int, responseBody: String?) {
                            Log.e(
                                "Response Success",
                                "Request is done. Url :$url staus code  $code and response body $responseBody"
                            )
                        }
                    })
            } catch (e: IOException) {
                e.printStackTrace()

                Log.e(
                    "Problem with call url ",
                    " Problem with call response url $url  error ${e.message}"
                )
            }
        }else {
            Log.e(
                "Missing Parameters ",
                " Please complete parameters because problem with call response"
            )
        }


    }

    fun generateTUDID() {
        val url = HttpRequest.clickLinkAffiliate()
        try {
            NetClient.netClient
                ?.callResponse(url, object : ResultRequest {
                    override fun onFailure(code: Int, errorMessage: String?) {
                        Log.e(
                            "Response Error",
                            "Problem with request url $url  response  code $code and response error message $errorMessage"
                        )
                    }

                    override fun onResponseSuccess(code: Int, responseBody: String?) {
                        Log.e(
                            "Response Success",
                            "Request is done. Url :$url status code  $code and response body $responseBody"
                        )
                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(
                "Problem with call url ",
                " Problem with call response url $url  error ${e.message}"
            )
        }
    }


    fun callTrackingLead(leadEventId: String, leadId: String) {

        val organizationId = settings.organizationId
        val tudid = settings.tduidValue
        val userEmail = settings.userEmail
        val googleAdvertisingId =
            settings.googleAdvertisingId


        val isAllParametersComplete =
            (!organizationId.isNullOrBlank() && !tudid.isNullOrEmpty() && !userEmail.isNullOrEmpty() && !googleAdvertisingId.isNullOrEmpty() && !leadEventId.isNullOrEmpty() && !leadId.isNullOrEmpty())


        if (isAllParametersComplete){
            val url = HttpRequest.trackingLead(
                organizationId = organizationId,
                leadEventId = leadEventId,
                leadId = leadId,
                tduid = tudid,
                extId = googleAdvertisingId
            )
            try {
                NetClient.netClient
                    ?.callResponse(url, object : ResultRequest {
                        override fun onFailure(code: Int, errorMessage: String?) {
                            Log.e(
                                "Response Error",
                                "Problem with request url $url  response  code $code and response error message $errorMessage"
                            )
                        }

                        override fun onResponseSuccess(code: Int, responseBody: String?) {
                            Log.e(
                                "Response Success",
                                "Request is done. Url :$url status code  $code and response body $responseBody"
                            )
                        }
                    })
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(
                    "Problem with call url ",
                    " Problem with call response url $url  error ${e.message}"
                )
            }


            if (userEmail != null && userEmail.isNotEmpty()) {
                val url = HttpRequest.trackingLead(
                    organizationId = organizationId,
                    leadEventId = leadEventId,
                    leadId = leadId,
                    tduid = tudid,
                    extId = userEmail
                )

                try {
                    NetClient.netClient
                        ?.callResponse(url, object : ResultRequest {
                            override fun onFailure(code: Int, errorMessage: String?) {
                                Log.e(
                                    "Response Error",
                                    "Problem with request url $url  response  code $code and response error message $errorMessage"
                                )
                            }

                            override fun onResponseSuccess(code: Int, responseBody: String?) {
                                Log.e(
                                    "Response Success",
                                    "Request is done. Url :$url status code  $code and response body $responseBody"
                                )
                            }
                        })
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(
                        "Problem with call url ",
                        " Problem with call response url $url  error ${e.message}"
                    )
                }
            }
        }else {
            Log.e(
                "Missing Parameters ",
                " Please complete parameters because problem with call response"
            )
        }

    }


    fun callTrackingSale(
        saleEventId: String,
        orderNumber: String,
        orderValue: String,
        currency: String,
        voucherCode: String?,
        reportInfo: String?,
        secretCode: String
    ) {
        val organizationId = settings.organizationId
        val tudid = settings.tduidValue
        val userEmail = settings.userEmail
        val googleAdvertisingId =
            settings.googleAdvertisingId


        val url = HttpRequest.trackingSale(
            organizationId = organizationId,
            saleEventId = saleEventId,
            orderNumber = orderNumber,
            orderValue = orderValue,
            currency = currency,
            voucherCode = voucherCode,
            tduid = tudid,
            extId = googleAdvertisingId,
            reportInfo = reportInfo,
            secretCode = secretCode
        )

        val isAllParametersComplete = (!organizationId.isNullOrBlank() &&)

        try {
            NetClient.netClient
                ?.callResponse(url, object : ResultRequest {
                    override fun onFailure(code: Int, errorMessage: String?) {
                        Log.e(
                            "Response Error",
                            "Problem with request url $url  response  code $code and response error message $errorMessage"
                        )
                    }

                    override fun onResponseSuccess(code: Int, responseBody: String?) {
                        Log.e(
                            "Response Success",
                            "Request is done. Url :$url status code  $code and response body $responseBody"
                        )
                    }
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
                        override fun onFailure(code: Int, errorMessage: String?) {
                            Log.e(
                                "Response Error",
                                "Problem with request url $url  response  code $code and response error message $errorMessage"
                            )
                        }

                        override fun onResponseSuccess(code: Int, responseBody: String?) {
                            Log.e(
                                "Response Success",
                                "Request is done. Url :$url status code  $code and response body $responseBody"
                            )
                        }
                    })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}