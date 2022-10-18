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
import android.os.DeadObjectException
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails


internal object InstallReferrerHelper {

    fun retrieveReferrer(context: Context, successCallback: (String?) -> Unit, errorCallback: (String) -> Unit, retryCount: Int = 1) {

        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        try {
                            val response: ReferrerDetails = referrerClient.installReferrer
                            val referrerUrl = response.installReferrer
                            successCallback.invoke(TradeDoublerSdkUtils.extractTduidFromQuery(referrerUrl))
                        } catch (e: DeadObjectException){
                            if(retryCount > 0){
                                retrieveReferrer(context, successCallback, errorCallback, retryCount - 1)
                            }else{
                                errorCallback.invoke("Install referrer, dead object exception")
                            }
                        }
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