package com.tradedouble.tradedoublersdk

//Todo: This class witch extras AdvertisingId

object AdvertisingId {

    var TAG = "AdvertisingId"
    var AAID: String? = null

//    fun getAdvertisingId(context: Context?): String? {
//        if (AAID == null) {
//            val executor: Executor =
//                Executors.newSingleThreadExecutor()
//            executor.execute {
//                var adInfo: ListenableFuture<AdvertisingIdInfo>? =
//                    null
//                try {
//                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context!!)
//                } catch (e: Exception) {
//                    Log.e(TAG, "Could not fetch advertising id", e)
//                } catch (e: GooglePlayServicesNotAvailableException) {
//                    Log.e(
//                        TAG,
//                        "Could not fetch advertising id, Google Play Service not available",
//                        e
//                    )
//                } catch (e: GooglePlayServicesRepairableException) {
//                    Log.e(
//                        TAG,
//                        "Could not fetch advertising id, Google Play Service need repairing",
//                        e
//                    )
//                }
//                if (adInfo != null) {
//                    var id: String? = null
//                    try {
//                        id = adInfo.get().id
//                    } catch (e: ExecutionException) {
//                        e.printStackTrace()
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//                    }
//                    try {
//                        val isLAT = adInfo.get().isLimitAdTrackingEnabled
//                    } catch (e: ExecutionException) {
//                        e.printStackTrace()
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//                    }
//                    AAID = id
//                }
//            }
//        }
//        return AAID
//    }
}