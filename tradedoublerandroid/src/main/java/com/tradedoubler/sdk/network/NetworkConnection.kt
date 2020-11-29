package com.tradedoubler.sdk.network

import android.content.Context
import android.net.ConnectivityManager

class NetworkConnection(private val context: Context) {
    val isOnline: Boolean
        get() {
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
}