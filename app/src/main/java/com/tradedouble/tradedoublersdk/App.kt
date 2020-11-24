package com.tradedouble.tradedoublersdk

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures.addCallback
import com.tradedouble.tradedoublerandroid.TraderDoublerSDK
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class App : Application() {

    companion object {
        private val LOG_TAG = App::class.java.simpleName
    }

    private lateinit var referrerClient: InstallReferrerClient

    private var tduidId: String? = ""

    override fun onCreate() {
        super.onCreate()


        TraderDoublerSDK.create(this)


        val advertisingId = AdvertisingId.getAdvertisingId(applicationContext)

        Toast.makeText(
            applicationContext,
            " Google Adverasing Id ${advertisingId}",
            Toast.LENGTH_SHORT
        ).show()

        referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        tduidId = response.installReferrer
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {

                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {

                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> {

                    }

                    InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {

                    }
                }

            }

            override fun onInstallReferrerServiceDisconnected() {
                Toast.makeText(
                    applicationContext,
                    " InstallReferrerServiceDisconnected ",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        TraderDoublerSDK.getInstance()?.setTduid("4e8241cd1b66e8a8d2a55c666129cccc")
        TraderDoublerSDK.getInstance()?.googleAdvertisingId = "38400000-8cf0-11bd-b23e-10b96e40000d"
        TraderDoublerSDK.getInstance()?.organizationId = "945630"
        TraderDoublerSDK.getInstance()?.userEmail = "magdalena.dziesinska@britenet.com.pl"


    }

}