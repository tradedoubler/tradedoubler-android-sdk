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
            if (adInfo != null) {
                try {
                    successCallback.invoke(adInfo.id)
                } catch (e: ExecutionException) {
                    errorCallback.invoke("Could not fetch advertising id, ExecutionException")
                } catch (e: InterruptedException) {
                    errorCallback.invoke("Could not fetch advertising id, InterruptedException")
                }
            }
        }
    }
}