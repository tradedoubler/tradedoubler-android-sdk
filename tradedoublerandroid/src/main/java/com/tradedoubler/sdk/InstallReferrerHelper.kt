package com.tradedoubler.sdk

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails


internal object InstallReferrerHelper {

    fun retrieveReferrer(context: Context, successCallback: (String) -> Unit, errorCallback: (String) -> Unit) {

        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        val referrerUrl  = response.installReferrer //TODO parsing
                        successCallback.invoke(referrerUrl)
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        errorCallback.invoke("Install referrer, feature not supported")
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        errorCallback.invoke("Install referrer, service unavailable")
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> {
                        errorCallback.invoke("Install referrer, service disconnected")
                    }

                    InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {
                        errorCallback.invoke("Install referrer, developer error")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                errorCallback.invoke("Install referrer, developer error")
            }
        })
    }
}