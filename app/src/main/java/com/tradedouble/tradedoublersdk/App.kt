package com.tradedouble.tradedoublersdk

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.tradedouble.tradedoublerandroid.TraderDoublerSDK

class App : Application() {

    companion object {
        private val LOG_TAG = App::class.java.simpleName
    }

    private lateinit var referrerClient: InstallReferrerClient

    private var tduidId: String = ""

    override fun onCreate() {
        super.onCreate()


        TraderDoublerSDK.createLibrary(this)

        referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        tduidId = response.installReferrer
                        TraderDoublerSDK.getInstance().setTduid(tduidId)
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


        TraderDoublerSDK.getInstance().setGoogleAdvertisingId("38400000-8cf0-11bd-b23e-10b96e40000d")
        TraderDoublerSDK.getInstance().setOrganizationId("945630")
        TraderDoublerSDK.getInstance().setUserEmail("magdalena.dziesinska@britenet.com.pl")


    }
}