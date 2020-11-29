package com.tradedoubler

import android.app.Application
import com.tradedoubler.sdk.TradeDoublerSdk

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        TradeDoublerSdk.create(this)
        TradeDoublerSdk.getInstance().isLoggingEnabled = true
        TradeDoublerSdk.getInstance().organizationId = "945630"
        //TradeDoublerSdk.getInstance().userEmail = "magdalena.dziesinska@britenet.com.pl"
    }
}