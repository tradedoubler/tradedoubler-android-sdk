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
import com.tradedoubler.sdk.TradeDoublerSdkUtils.format
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
    private val queuedItems: MutableList<()->Unit> = mutableListOf<()->Unit>()
    private var isReferrerInProgress: Boolean = false

    companion object {

        const val DEFAULT_SALE_EVENT = "51"

        @Volatile
        private var instance: TradeDoublerSdk? = null

        /**
         * Return sdk instance.
         */
        fun getInstance(): TradeDoublerSdk {
            return instance!!
        }

        /**
         *  Create sdk instance with default okhhtp client.
         */
        fun create(context: Context): TradeDoublerSdk {
            return create(context, OkHttpClient())
        }

        /**
         *  Create sdk instance with custom okhhtp client.
         */
        fun create(context: Context, okHttpClient: OkHttpClient): TradeDoublerSdk {
            instance = TradeDoublerSdk(context, okHttpClient)
            return instance!!
        }

        /**
         * Checks that sdk was initialized.
         */
        fun wasInitialized(): Boolean{
            return instance != null
        }
    }

    /**
     *  Id of your organization, should be provided by Tradedoubler.
     */
    var organizationId: String?
        get() = settings.organizationId
        set(organizationId) {
            settings.storeOrganizationId(organizationId)
        }

    /**
     *  TDUID, unique tracking identifier, would be provided in installation url on in open app url.
     *  TDUID is valid for 365 days.
     */
    var tduid: String?
        get() = settings.tduid
        set(value) {
            settings.storeTduid(value)
        }

    /**
     * The user's email address. This value is hashed (sha256) before usage.
     */
    var userEmail: String?
        get() = settings.userEmail
        set(userEmail) {
            if (userEmail != null && userEmail.isNotEmpty()) {
                val generateSHA56HashEmail = TradeDoublerSdkUtils.generateSHA56Hash(userEmail)
                settings.storeUserEmail(generateSHA56HashEmail)
            } else {
                settings.storeUserEmail(null)
            }
        }

    /**
     * Advertising identifier of user, default implementations use Google Advertising Id.
     */
    var advertisingId: String?
        get() = settings.advertisingIdentifier
        set(googleAdvertisingId) {
            val generateSHA56Hash = TradeDoublerSdkUtils.generateSHA56Hash(googleAdvertisingId)
            settings.storeAdvertisingIdentifier(generateSHA56Hash)
        }

    /**
     *  Secret code, should be provided by Tradedoubler.
     */
    var secretCode: String?
        get() = settings.secretCode
        set(secretCode) {
            settings.setSecretCode(secretCode)
        }
    /**
     *  Flag to enable extended logs in logcat, very useful during development and diagnosing problems.
     */
    var isLoggingEnabled: Boolean
        get() = logger.isLoggingEnabled
        set(value) {
            logger.isLoggingEnabled = value
        }

    /**
     *  Flag to enable tracking, very useful during development. If set to false none of tracking events will be sent to Tradedoubler server.
     */
    var isTrackingEnabled: Boolean = true

    /**
     *  Flag to enable automatic tduid retrieval from installation url,
     */
    var useInstallReferrer: Boolean = true
        set(value) {
            field = value
            if (useInstallReferrer) {
                retrieveInstallTduid()
            }
        }

    init {
        retrieveGoogleAdvertisingId()
    }

    /**
     * track opening app event
     */
    fun trackOpenApp() {
        if (!isTrackingEnabled) {
            return
        }
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.advertisingIdentifier

        if (!validateOrganizationId(organizationId)) {
            return
        }

        if(tduid.isNullOrEmpty() && !settings.wasInstallTduidInvoked && useInstallReferrer){
            retrieveInstallTduid()
            queuedItems.add{ trackOpenApp() }
            return
        }

        if(googleAdvertisingId.isNullOrEmpty()){
            queuedItems.add{ trackOpenApp() }
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

    /**
     *  Track lead for given lead event.
     */
    fun trackLead(leadEventId: String, leadId: String) {
        if (!isTrackingEnabled) {
            return
        }
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.advertisingIdentifier

        if (!validateOrganizationId(organizationId)) {
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

    /**
     * Retrieves tduid value from given intent, if tduid is present inside intent its values is also stored in SDK settings for further usage.
     */
    fun retrieveAndSetTduidFromIntent(intent: Intent?): String? {
        return retrieveAndSetTduidFromUri(intent?.data)
    }

    /**
     * Retrieves tduid value from given uri, if tduid is present inside intent its values is also stored in SDK settings for further usage.
     */
    fun retrieveAndSetTduidFromUri(uri: Uri?): String? {
        return TradeDoublerSdkUtils.extractTduidFromUri(uri)?.also { newTdUid ->
            tduid = newTdUid
        }
    }

    /**
     * Retrieves tduid value from given referrer string, if tduid is present inside intent its values is also stored in SDK settings for further usage.
     */
    fun retrieveAndSetTduidFromReferrer(referrer: String?): String? {
        return TradeDoublerSdkUtils.extractTduidFromQuery(referrer)?.also { newTdUid ->
            tduid = newTdUid
        }
    }

    /**
     * track sale for given parameters.
     */
    fun trackSale(saleEventId: String, orderNumber: String, orderValue: Double, currency: Currency?, voucherCode: String?, reportInfo: ReportInfo?) {
        trackSale(saleEventId,orderNumber,orderValue,currency?.currencyCode,voucherCode,reportInfo)
    }

    /**
     * track sale for given parameters.
     */
    fun trackSale(saleEventId: String, orderNumber: String, orderValue: Double, currency: String?, voucherCode: String?, reportInfo: ReportInfo?) {
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
                orderValue.format(2),
                currency,
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

    /**
     * track sale PLT for given parameters.
     */
    fun trackSalePlt(orderNumber: String, currency: Currency?, voucherCode: String?, basketInfo: BasketInfo){
        trackSalePlt(orderNumber,currency?.currencyCode,voucherCode,basketInfo)
    }
    /**
     * track sale PLT for given parameters.
     */
    fun trackSalePlt(orderNumber: String, currency: String?, voucherCode: String?, basketInfo: BasketInfo){
        trackSalePlt(DEFAULT_SALE_EVENT,orderNumber,currency,voucherCode,basketInfo)
    }

    /**
     * track sale PLT for given parameters.
     */
    fun trackSalePlt(saleEventId: String, orderNumber: String, currency: Currency?, voucherCode: String?, basketInfo: BasketInfo) {
        trackSalePlt(saleEventId, orderNumber, currency?.currencyCode, voucherCode, basketInfo)
    }

    /**
     * track sale PLT for given parameters.
     */
    fun trackSalePlt(saleEventId: String, orderNumber: String, currency: String?, voucherCode: String?, basketInfo: BasketInfo) {
        if(!isTrackingEnabled){
            return
        }
        val organizationId = settings.organizationId
        val tduid = settings.tduid
        val userEmail = settings.userEmail
        val googleAdvertisingId = settings.advertisingIdentifier
        val secretCode = settings.secretCode

        if(!validateOrganizationId(organizationId)){
            return
        }

        if(!validateSecretCode(secretCode)){
            return
        }

        fun buildTrackSalPltUrl(extId: String): String {
            return HttpRequest.trackingSalePLT(
                organizationId,
                saleEventId,
                orderNumber,
                currency,
                tduid,
                extId,
                voucherCode,
                basketInfo,
                secretCode!!
            )
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildTrackSalPltUrl(userEmail))
        }
        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildTrackSalPltUrl(googleAdvertisingId))
        }
    }

    /**
     * Track application install. SDK takes care about sending this request only once, so no additional checks are needed.
     */
    fun trackInstall(appInstallEventId: String) {
        if(!isTrackingEnabled){
            return
        }
        if (settings.wasInstallTracked){
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

        if(tduid.isNullOrEmpty() && !settings.wasInstallTduidInvoked && useInstallReferrer){
            retrieveInstallTduid()
            queuedItems.add{ trackInstall( appInstallEventId) }
            return
        }

        if(googleAdvertisingId.isNullOrEmpty()){
            queuedItems.add{ trackInstall( appInstallEventId) }
            return
        }

        fun buildInstallUrl(extId: String): String {
            return HttpRequest.trackingInstallation(organizationId, appInstallEventId, leadNumber, tduid, extId)
        }

        if (!userEmail.isNullOrEmpty()) {
            appendRequest(buildInstallUrl(userEmail))
            settings.setInstallTracked(true)
        }

        if (!googleAdvertisingId.isNullOrEmpty()) {
            appendRequest(buildInstallUrl(googleAdvertisingId))
            settings.setInstallTracked(true)
        }
    }
    private fun retrieveInstallTduid() {
        if(!networkConnection.isNetworkAvailable()){
            return
        }
        if(settings.wasInstallTduidInvoked){
            return
        }
        if(isReferrerInProgress){
            return
        }
        isReferrerInProgress = true
        InstallReferrerHelper.retrieveReferrer(context,
            { tduid ->
                if (tduid != null) {
                    logger.logEvent("tduid form referrer retrieved")
                    if (BuildConfig.DEBUG) {
                        logger.logEvent("tduid $tduid")
                    }
                    this.tduid = tduid
                } else {
                    logger.logEvent("tduid not present in referrer")
                }
                settings.setInstallReferrerChecked(true)
                isReferrerInProgress = false
                invokeQueuedItems()

            },
            { errorMessage ->
                logger.logEvent("error during referrer retrieval")
                logger.logError(errorMessage)
                settings.setInstallReferrerChecked(true)
                isReferrerInProgress = false
                invokeQueuedItems()
            })
    }

    private fun retrieveGoogleAdvertisingId() {
        AdvertisingIdHelper.retrieveAdvertisingId(context,
            { aaId ->
                logger.logEvent("Android advertising id retrieved")
                advertisingId = aaId
                invokeQueuedItems()
            },
            { errorMessage ->
                logger.logEvent("Android advertising not retrieved, performing fallback to android id")
                logger.logError(errorMessage)
                advertisingId = TradeDoublerSdkUtils.getAndroidId(context)
                invokeQueuedItems()
            })
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
        if(isLoggingEnabled){
            logger.logEvent("track URL: $url")
        }
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

    internal fun onInternetConnected(){
        if(!settings.wasInstallTduidInvoked && useInstallReferrer && tduid.isNullOrEmpty()){
            retrieveInstallTduid()
        } else{
            invokeQueuedItems()
            checkPendingRequests()
        }
    }

    private fun invokeQueuedItems() {
        val copy = queuedItems.toList()
        queuedItems.clear()
        copy.forEach {
            it.invoke()
        }
    }
}