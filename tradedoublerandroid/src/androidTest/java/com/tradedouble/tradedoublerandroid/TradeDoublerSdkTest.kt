package com.tradedouble.tradedoublerandroid

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TradeDoublerSdkTest {

    private val saleEventId = "403759"
    private val installEventId = "403761"
    private val sale2EventId = "403763"
    private val leadEventId = "403765"
    private val basketSale = "51"

    private val orgId = "YOUR_VALUE"
    private val tdUid = "YOUR_VALUE"
    private val email = "YOUR_VALUE"
    private val advertisingId = "YOUR_VALUE"

    @Test
    fun trackAppOpen() {
        val tradeDoublerSdk = createSkdClient()

        tradeDoublerSdk.tduid = tdUid
        tradeDoublerSdk.organizationId = orgId
        tradeDoublerSdk.userEmail = email
        tradeDoublerSdk.googleAdvertisingId = advertisingId

        tradeDoublerSdk.trackOpenApp()
    }

    @Test
    fun trackAppInstall() {
        val tradeDoublerSdk = createSkdClient()

        tradeDoublerSdk.tduid = tdUid
        tradeDoublerSdk.organizationId = orgId
        tradeDoublerSdk.userEmail = email
        tradeDoublerSdk.googleAdvertisingId = advertisingId

        tradeDoublerSdk.trackInstall(installEventId)

        Thread.sleep(3000)
    }

    @Test
    fun trackLead() {
        val tradeDoublerSdk = createSkdClient()

        tradeDoublerSdk.tduid = tdUid
        tradeDoublerSdk.organizationId = orgId
        tradeDoublerSdk.userEmail = email
        tradeDoublerSdk.googleAdvertisingId = advertisingId

        tradeDoublerSdk.trackLead(leadEventId,"734")

        Thread.sleep(3000)

    }

    @Test
    fun trackSale() {
        val tradeDoublerSdk = createSkdClient()

        tradeDoublerSdk.tduid = tdUid
        tradeDoublerSdk.organizationId = orgId
        tradeDoublerSdk.userEmail = email
        tradeDoublerSdk.googleAdvertisingId = advertisingId
        tradeDoublerSdk.secretCode = "12345678"

        val reportInfo = ReportInfo(
            listOf(
                ReportEntry("25","car", 23.0,5),
                ReportEntry("453","bike", 3.0,25)
            )
        )

        tradeDoublerSdk.trackSale(saleEventId,"12553","231", Currency.getInstance("EUR"),null, reportInfo)

        Thread.sleep(3000)
    }

    @Test
    fun trackSalePlt() {
        val tradeDoublerSdk =
            createSkdClient()

        tradeDoublerSdk.tduid = tdUid
        tradeDoublerSdk.organizationId = orgId
        tradeDoublerSdk.userEmail = email
        tradeDoublerSdk.googleAdvertisingId = advertisingId
        tradeDoublerSdk.secretCode = "12345678"

        val reportInfo = BasketInfo(
            listOf(
                BasketEntry("3408","1243","plt_cookie", 23.0,5),
                BasketEntry("3168","3221","plt_milk", 3.0,25)
            )
        )

        //1411
        tradeDoublerSdk.trackSalePlt(basketSale,"141121", Currency.getInstance("EUR"),null, reportInfo)
        tradeDoublerSdk.trackSalePlt(sale2EventId,"141121", Currency.getInstance("EUR"),null, reportInfo)

        Thread.sleep(3000)
    }

    private fun createSkdClient(): TradeDoublerSdk {
        val okHttpClient = initClient()
        val okHttp3IdlingResource = OkHttp3IdlingResource.create("TD", okHttpClient)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)

        return TradeDoublerSdk.create(
            InstrumentationRegistry.getInstrumentation().targetContext,
            okHttpClient
        )
    }

    private fun initClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}