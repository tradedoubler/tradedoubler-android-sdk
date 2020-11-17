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

    var tduidId: String = ""

    override fun onCreate() {
        super.onCreate()

        referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {

                        try {
                            val response: ReferrerDetails = referrerClient.installReferrer
                            tduidId = response.installReferrer

                            Toast.makeText(
                                applicationContext,
                                "ReferrerUrl $tduidId",
                                Toast.LENGTH_SHORT
                            ).show()

                            referrerClient.endConnection()

                        } catch (e: RemoteException) {
                            val errorMessage = e.message ?: ""
                            Log.e(LOG_TAG, "Error Message ${errorMessage}")
                        }
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

        TraderDoublerSDK.create(this)
    }
}