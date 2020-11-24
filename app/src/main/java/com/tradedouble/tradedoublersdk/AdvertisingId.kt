package com.tradedouble.tradedoublersdk

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors

//Todo: This class witch extras AdvertisingId

object AdvertisingId {

    var TAG = "AdvertisingId"
    var AAID: String? = null

    fun getAdvertisingId(context: Context?): String? {
        if (AAID == null) {
            val executor: Executor =
                Executors.newSingleThreadExecutor()
            executor.execute {
                var adInfo: AdvertisingIdClient.Info? = null

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
                        id = adInfo.id
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