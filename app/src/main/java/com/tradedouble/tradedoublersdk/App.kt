package com.tradedouble.tradedoublersdk

import android.app.Application
import android.widget.Toast
import com.tradedouble.tradedoublerandroid.TraderDoublerSDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        TraderDoublerSDK.create(this);

        TraderDoublerSDK.getInstance().setTduid("Magdalena")
        TraderDoublerSDK.getInstance().googleAdvertisingId = "Klasa123"

    }
}