package com.tradedouble.tradedoublerandroid

import android.content.Context
import android.util.Patterns
import com.tradedouble.tradedoublerandroid.network.HttpRequest
import com.tradedouble.tradedoublerandroid.network.NetworkConnection
import okhttp3.*
import java.io.IOException
import java.util.*


class TradeDoublerSdk constructor(private val context: Context, private val client: OkHttpClient) {

    private var settings: TradeDoublerSdkSettings = TradeDoublerSdkSettings(context)
    private var networkConnection: NetworkConnection = NetworkConnection(context)

    companion object {

        @Volatile
        private var instance: TradeDoublerSdk? = null

        fun getInstance(): TradeDoublerSdk {
            return instance!!
        }

        fun create(context: Context): TradeDoublerSdk {
            return create(context, OkHttpClient())
        }

        fun create(context: Context, okHttpClient: OkHttpClient): TradeDoublerSdk {
            instance = TradeDoublerSdk(context, okHttpClient)
            return instance!!
        }
    }

    var organizationId: String?
        get() = settings.organizationId
        set(organizationId) {
            settings.storeOrganizationId(organizationId)
        }

    var tudid: String?
        get() = settings.tduid
        set(value) {
            settings.storeTduid(value)
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

    var secretCode: String?
        get() = settings.secretCode
        set(secretCode) {
            settings.setSecretCode(secretCode)
        }

    fun trackOpenApp() {
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.googleAdvertisingId

        if (tduid.isNullOrBlank()) {
            return
        }

        fun buildTrackOpenUrl(extId: String): String {
            return HttpRequest.trackingOpen(organizationId, extId, tudid, "1")
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildTrackOpenUrl(userEmail))
        }

        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildTrackOpenUrl(googleAdvertisingId))
        }
    }

    fun trackLead(leadEventId: String, leadId: String) {
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.googleAdvertisingId

        if (tduid.isNullOrBlank()) {
            return
        }

        fun buildTrackLead(extId: String): String {
            return HttpRequest.trackingLead(organizationId, leadEventId, leadId, tudid, extId)
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildTrackLead(userEmail))
        }

        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildTrackLead(googleAdvertisingId))
        }
    }

    fun trackSale(saleEventId: String, orderNumber: String, orderValue: String, currency: Currency, voucherCode: String?, reportInfo: ReportInfo?) {
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.googleAdvertisingId
        val secretCode = settings.secretCode

        if (tduid.isNullOrBlank()) {
            return
        }

        fun buildTrackSaleUrl(extId: String): String {
            return HttpRequest.trackingSale(
                organizationId,
                saleEventId,
                orderNumber,
                orderValue,
                currency.currencyCode,
                voucherCode,
                tudid,
                extId,
                reportInfo,
                secretCode!!
            ) //TODO kkalisz validate secret code
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildTrackSaleUrl(userEmail))
        }
        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildTrackSaleUrl(googleAdvertisingId))
        }
    }

    fun trackSalePlt(saleEventId: String, orderNumber: String, currency: Currency, voucherCode: String?, reportInfo: BasketInfo) {
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.googleAdvertisingId

        if (tduid.isNullOrBlank()) {
            return
        }

        fun buildTrackSalPltUrl(extId: String): String {
            return HttpRequest.trackingSalePLT(
                organizationId,
                saleEventId,
                orderNumber,
                currency.currencyCode,
                tudid,
                extId,
                voucherCode,
                reportInfo
            )
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildTrackSalPltUrl(userEmail))
        }
        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildTrackSalPltUrl(googleAdvertisingId))
        }
    }

    fun trackInstall(appInstallEventId: String) {
        val tduid = settings.tduid
        val organizationId = settings.organizationId
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.googleAdvertisingId
        val leadNumber = TraderDoublerSdkUtils.getRandomString() + TraderDoublerSdkUtils.getInstallDate(context)

        if (tduid.isNullOrBlank()) {
            return
        }

        fun buildInstallUrl(extId: String): String {
            return HttpRequest.trackingInstallation(organizationId, appInstallEventId, leadNumber, tduid, extId)
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildInstallUrl(userEmail))
        }

        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildInstallUrl(googleAdvertisingId))
        }
    }

    private fun appendRequest(requestUrl: String) {

        val request = Request.Builder()
            .url(requestUrl)
            .get()
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                //resultRequest.onFailure(-1)
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                if (response.isSuccessful) {
                    if (response.code in 200..300) {
                        //resultRequest.onResponseSuccess(response.code, responseBody = response.body.toString())
                    } else {
                        //resultRequest.onFailure(response.code)
                    }
                }
            }
        })

    }
}