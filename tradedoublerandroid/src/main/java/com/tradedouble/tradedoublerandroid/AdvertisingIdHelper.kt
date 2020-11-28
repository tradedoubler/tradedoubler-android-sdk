package com.tradedouble.tradedoublerandroid

import android.content.Context
import androidx.ads.identifier.AdvertisingIdClient
import java.util.concurrent.Executor
import java.util.concurrent.Executors

internal object AdvertisingIdHelper {

    fun retrieveAdvertisingId(context: Context, successCallback: (String) -> Unit, errorCallback: (String) -> Unit) {
        if (AdvertisingIdClient.isAdvertisingIdProviderAvailable(context)) {
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val advertisingIdInfoListenableFuture = AdvertisingIdClient.getAdvertisingIdInfo(context)
                try {
                    val info = advertisingIdInfoListenableFuture.get()
                    successCallback.invoke(info.id)
                }
                catch (e: java.lang.Exception){
                    errorCallback.invoke("Exception during retrieving aaid")
                }
            }
        } else {
            return errorCallback.invoke("The Advertising ID client library is unavailable. Use a different")
        }
    }
}