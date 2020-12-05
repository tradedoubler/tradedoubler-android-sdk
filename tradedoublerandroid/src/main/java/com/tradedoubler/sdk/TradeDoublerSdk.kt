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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Patterns
import com.tradedoubler.sdk.network.HttpRequest
import com.tradedoubler.sdk.network.NetworkConnection
import com.tradedoubler.sdk.utils.OfflineDatabase
import com.tradedoubler.sdk.utils.TradeDoublerLogger
import okhttp3.*
import java.io.IOException
import java.util.*


class TradeDoublerSdk constructor(private val context: Context, private val client: OkHttpClient) {

    private val settings: TradeDoublerSdkSettings = TradeDoublerSdkSettings(context)
    private val logger: TradeDoublerLogger = TradeDoublerLogger(false)
    private val offlineDatabase = OfflineDatabase(context)
    private var networkConnection: NetworkConnection = NetworkConnection(context)

    companion object {

        const val DEFAULT_SALE_EVENT = "51"

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

    var tduid: String?
        get() = settings.tduid
        set(value) {
            settings.storeTduid(value)
        }

    var userEmail: String?
        get() = settings.userEmail
        set(userEmail) {
            if (userEmail != null && userEmail.isNotEmpty()) {
                val generateSHA56HashEmail = TradeDoublerSdkUtils.generateSHA56Hash(userEmail)
                settings.storeUserEmail(generateSHA56HashEmail)
            }else{
                settings.storeUserEmail(null)
            }
        }

    var advertisingId: String?
        get() = settings.advertisingIdentifier
        set(googleAdvertisingId) {
            val generateSHA56Hash = TradeDoublerSdkUtils.generateSHA56Hash(googleAdvertisingId)
            settings.storeAdvertisingIdentifier(generateSHA56Hash)
        }

    var secretCode: String?
        get() = settings.secretCode
        set(secretCode) {
            settings.setSecretCode(secretCode)
        }

    var isLoggingEnabled: Boolean
        get() = logger.isLoggingEnabled
        set(value) {
            logger.isLoggingEnabled = value
        }

    var isTrackingEnabled: Boolean = true

    var automaticAdvertisingIdRetrieval: Boolean = false
        set(value){
            field = value
            if(automaticAdvertisingIdRetrieval){
                AdvertisingIdHelper.retrieveAdvertisingId(context,
                    { aaId ->
                        logger.logEvent("Android advertising id retrieved")
                        advertisingId = aaId
                    },
                    { errorMessage ->
                        logger.logEvent("Android advertising not retrieved, performing fallback to android id")
                        logger.logError(errorMessage)
                        advertisingId = TradeDoublerSdkUtils.getAndroidId(context)
                    })
            }
        }

    var automaticInstallReferrerRetrieval: Boolean = false
        set(value){
            field = value
            if(automaticInstallReferrerRetrieval){
                InstallReferrerHelper.retrieveReferrer(context,
                    { tduid ->
                        if(tduid != null){
                            logger.logEvent("tduid form referrer retrieved")
                            if(BuildConfig.DEBUG){
                                logger.logEvent("tduid $tduid")
                            }
                            this.tduid = tduid
                        }else{
                            logger.logEvent("tduid not present in referrer")
                        }
                    },
                    { errorMessage ->
                        logger.logEvent("error during referrer retrival")
                        logger.logError(errorMessage)
                    })
            }
        }

    fun trackOpenApp() {
        if(!isTrackingEnabled){
            return
        }
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.advertisingIdentifier

        if(!validateOrganizationId(organizationId)){
            return
        }

        fun buildTrackOpenUrl(extId: String): String {
            return HttpRequest.trackingOpen(organizationId, extId, tduid, "1")
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildTrackOpenUrl(userEmail))
        }

        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildTrackOpenUrl(googleAdvertisingId))
        }
    }

    fun trackLead(leadEventId: String, leadId: String) {
        if(!isTrackingEnabled){
            return
        }
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.advertisingIdentifier

        if(!validateOrganizationId(organizationId)){
            return
        }

        fun buildTrackLead(extId: String): String {
            return HttpRequest.trackingLead(organizationId, leadEventId, leadId, tduid, extId)
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildTrackLead(userEmail))
        }

        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildTrackLead(googleAdvertisingId))
        }
    }

    fun retrieveAndSetTduidFromIntent(intent: Intent?): String?{
        return retrieveAndSetTduidFromUri(intent?.data)
    }

    fun retrieveAndSetTduidFromUri(uri: Uri?): String?{
        return TradeDoublerSdkUtils.extractTduidFromUri(uri)?.also { newTdUid ->
            tduid = newTdUid
        }
    }

    fun retrieveAndSetTduidFromReferrer(referrer: String?): String?{
        return TradeDoublerSdkUtils.extractTduidFromQuery(referrer)?.also { newTdUid ->
            tduid = newTdUid
        }
    }

    fun trackSale(saleEventId: String, orderNumber: String, orderValue: String, currency: Currency, voucherCode: String?, reportInfo: ReportInfo?) {
        if(!isTrackingEnabled){
            return
        }
        val organizationId = settings.organizationId
        val secretCode = settings.secretCode

        if(!validateOrganizationId(organizationId)){
            return
        }
        if(!validateSecretCode(secretCode)){
            return
        }

        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.advertisingIdentifier

        fun buildTrackSaleUrl(extId: String): String {
            return HttpRequest.trackingSale(
                organizationId,
                saleEventId,
                orderNumber,
                orderValue,
                currency.currencyCode,
                voucherCode,
                tduid,
                extId,
                reportInfo,
                secretCode!!
            )
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildTrackSaleUrl(userEmail))
        }
        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildTrackSaleUrl(googleAdvertisingId))
        }
    }

    fun trackSalePlt(orderNumber: String, currency: Currency, voucherCode: String?, reportInfo: BasketInfo){
        trackSalePlt(DEFAULT_SALE_EVENT,orderNumber,currency,voucherCode,reportInfo)
    }

    fun trackSalePlt(saleEventId: String, orderNumber: String, currency: Currency, voucherCode: String?, reportInfo: BasketInfo) {
        if(!isTrackingEnabled){
            return
        }
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.advertisingIdentifier

        if(!validateOrganizationId(organizationId)){
            return
        }

        fun buildTrackSalPltUrl(extId: String): String {
            return HttpRequest.trackingSalePLT(
                organizationId,
                saleEventId,
                orderNumber,
                currency.currencyCode,
                tduid,
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
        if(!isTrackingEnabled){
            return
        }
        val tduid = settings.tduid
        val organizationId = settings.organizationId
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.advertisingIdentifier
        val leadNumber = TradeDoublerSdkUtils.getRandomString() + TradeDoublerSdkUtils.getInstallDate(context)

        if(!validateOrganizationId(organizationId)){
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

    private fun validateSecretCode(secretCode: String?) = validateAndPrintError(secretCode, "secretCode")

    private fun validateOrganizationId(organizationId: String?) = validateAndPrintError(organizationId, "organizationId")

    private fun validateAndPrintError(value: String?, fieldName: String): Boolean {
        return if( value.isNullOrBlank() ){
            logger.logError("Property $fieldName must be set and not be empty")
            false
        }else{
            true
        }
    }

    private fun appendRequest(requestUrl: String) {
        val request = offlineDatabase.insertUrl(requestUrl)
        if(networkConnection.isNetworkAvailable()) {
            offlineDatabase.peekUrlById(request.id)?.also {
                performRequest(it)
            }
        }
    }

    private fun checkPendingRequests() {
        offlineDatabase.peekOldestUrl()?.also { offlineUrl ->
            performRequest(offlineUrl)
        }
    }

    private fun performRequest(offlineUrl: OfflineDatabase.OfflineUrl) {
        val url = offlineUrl.url
        val request = Request.Builder().url(url).get().build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (logger.isLoggingEnabled) {
                    logger.logEvent(Log.getStackTraceString(e))
                }
                appendRequest(url)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    if (response.code in 200..300) {
                        offlineDatabase.deleteUrl(offlineUrl.id)
                        checkPendingRequests()
                    } else {
                        logger.logEvent("Request failed with code: ${response.code}")
                    }
                }
            }
        })
    }
}