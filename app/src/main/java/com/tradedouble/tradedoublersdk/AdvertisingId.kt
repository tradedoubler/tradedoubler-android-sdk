package com.tradedouble.tradedoublersdk

import android.content.Context
import android.util.Log
import androidx.ads.identifier.AdvertisingIdClient
import androidx.ads.identifier.AdvertisingIdInfo
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AdvertisingId {

    var TAG = "AdvertisingId"
    var AAID: String? = null

    fun getAdvertisingId(context: Context?): String? {
        if (AAID == null) {
            val executor: Executor =
                Executors.newSingleThreadExecutor()
            executor.execute {
                var adInfo: ListenableFuture<AdvertisingIdInfo>? =
                    null
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context!!)
                } catch (e: Exception) {
                    Log.e(TAG, "Could not fetch advertising id", e)
                } catch (e: GooglePlayServicesNotAvailableException) {
                    Log.e(
                        TAG,
                        "Could not fetch advertising id, Google Play Service not available",
                        e
                    )
                } catch (e: GooglePlayServicesRepairableException) {
                    Log.e(
                        TAG,
                        "Could not fetch advertising id, Google Play Service need repairing",
                        e
                    )
                }
                if (adInfo != null) {
                    var id: String? = null
                    try {
                        id = adInfo.get().id
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    try {
                        val isLAT = adInfo.get().isLimitAdTrackingEnabled
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    AAID = id
                }
            }
        }
        return AAID
    }
}