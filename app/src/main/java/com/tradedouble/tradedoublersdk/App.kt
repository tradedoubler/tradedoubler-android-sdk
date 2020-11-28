package com.tradedouble.tradedoublersdk

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.tradedouble.tradedoublerandroid.TradeDoublerSdk
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class App : Application() {

    var TAG = "AdvertisingId"


    companion object {
        private val LOG_TAG = App::class.java.simpleName
    }

    private lateinit var referrerClient: InstallReferrerClient

    override fun onCreate() {
        super.onCreate()


        TradeDoublerSdk.create(this)

        TradeDoublerSdk.getInstance().tduid = "4e8241cd1b66e8a8d2a55c666129cccc"
        TradeDoublerSdk.getInstance().organizationId = "945630"
        TradeDoublerSdk.getInstance().userEmail = "magdalena.dziesinska@britenet.com.pl"

//        referrerClient = InstallReferrerClient.newBuilder(this).build()
//        referrerClient.startConnection(object : InstallReferrerStateListener {
//            override fun onInstallReferrerSetupFinished(responseCode: Int) {
//                when (responseCode) {
//                    InstallReferrerClient.InstallReferrerResponse.OK -> {
//                        val response: ReferrerDetails = referrerClient.installReferrer
//                        val referrerUrl  = response.installReferrer
//                        TradeDoublerSdk.getInstance()?.callTrackingInstallation(appDateInstall = "", appInstallEventId = "403761",tduid = referrerUrl)
//                    }
//
//                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
//
//                    }
//
//                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
//
//                    }
//
//                    InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> {
//
//                    }
//
//                    InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {
//
//                    }
//                }
//
//            }
//
//            override fun onInstallReferrerServiceDisconnected() {
//                Toast.makeText(
//                    applicationContext,
//                    " InstallReferrerServiceDisconnected ",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        })
    }
}