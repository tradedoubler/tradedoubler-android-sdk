package com.tradedoubler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tradedoubler.sdk.TradeDoublerSdk
import com.tradedoubler.tradedoublersdk.R
import kotlinx.android.synthetic.main.main.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MainViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        TradeDoublerSdk.create(applicationContext,initClient())

        advertising_text.text = TradeDoublerSdk.getInstance().advertisingId
        tduid_text.text = TradeDoublerSdk.getInstance().tduid

        isLoggingCheckBox.isChecked = TradeDoublerSdk.getInstance().isLoggingEnabled
        isLoggingCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            TradeDoublerSdk.getInstance().isLoggingEnabled = isChecked
        }

        isTrackingCheckBox.isChecked = TradeDoublerSdk.getInstance().isTrackingEnabled
        isTrackingCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            TradeDoublerSdk.getInstance().isTrackingEnabled = isChecked
        }


        setting_view.setOnClickListener {
            val intent = Intent(this , SettingsActivity::class.java)
            startActivity(intent)
        }

        clear_view.setOnClickListener {

        }
    }

    private fun initClient(): OkHttpClient {
        val appContext = applicationContext
        val loggingInterceptor = Interceptor{
            window?.decorView?.post {
                Toast.makeText(appContext,it.request().url.toString(),Toast.LENGTH_LONG).show()
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