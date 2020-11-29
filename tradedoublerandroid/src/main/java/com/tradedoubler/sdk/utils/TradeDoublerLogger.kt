package com.tradedoubler.sdk.utils

import android.util.Log

class TradeDoublerLogger(var isLoggingEnabled: Boolean){

    companion object {
        private const val logTag = "Tradedoubler"
    }

    fun logEvent(event: String){
        if(isLoggingEnabled){
            Log.d(logTag,event)
        }
    }

    fun logError(event: String){
        Log.e(logTag, event)
    }
}