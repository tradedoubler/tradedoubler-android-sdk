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

package com.tradedoubler

import android.app.Application
import android.os.Handler
import android.widget.Toast
import com.tradedoubler.sdk.TradeDoublerSdk
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        TradeDoublerSdk.create(applicationContext,initClient())
        TradeDoublerSdk.getInstance().organizationId = "945630"
        TradeDoublerSdk.getInstance().secretCode = "12345678"

        TradeDoublerSdk.getInstance().trackInstall("403761")
        TradeDoublerSdk.getInstance().trackOpenApp()
    }

    private fun initClient(): OkHttpClient {
        val appContext = applicationContext
        val handler = Handler(appContext.mainLooper)
        val loggingInterceptor = Interceptor{
            handler.post {
                Toast.makeText(appContext,it.request().url.toString(), Toast.LENGTH_LONG).show()
            }
            it.proceed(it.request())
        }

        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}