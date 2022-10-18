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
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors

internal object AdvertisingIdHelper {

    fun retrieveAdvertisingId(context: Context, successCallback: (String) -> Unit, errorCallback: (String) -> Unit) {
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            var adInfo: AdvertisingIdClient.Info? = null
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
            } catch (e: Exception) {
                errorCallback.invoke("Could not fetch advertising id")
            } catch (e: GooglePlayServicesNotAvailableException) {
                errorCallback.invoke("Could not fetch advertising id, Google Play Service not available")
            } catch (e: GooglePlayServicesRepairableException) {
                errorCallback.invoke("Could not fetch advertising id, Google Play Service need repairing")
            }
            if (adInfo != null ) {
                try {
                    val id = adInfo.id
                    if(id != null) {
                        successCallback.invoke(id)
                    }else{
                        errorCallback.invoke("Advertising id is null")
                    }
                } catch (e: ExecutionException) {
                    errorCallback.invoke("Could not fetch advertising id, ExecutionException")
                } catch (e: InterruptedException) {
                    errorCallback.invoke("Could not fetch advertising id, InterruptedException")
                }
            }
        }
    }
}