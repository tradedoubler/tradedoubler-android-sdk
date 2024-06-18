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

        updateViewValues()

        isLoggingCheckBox.setOnCheckedChangeListener { _, isChecked ->
            TradeDoublerSdk.getInstance().isLoggingEnabled = isChecked
        }

        isTrackingCheckBox.setOnCheckedChangeListener { _, isChecked ->
            TradeDoublerSdk.getInstance().isTrackingEnabled = isChecked
        }


        setting_view.setOnClickListener {
            val intent = Intent(this , SettingsActivity::class.java)
            startActivity(intent)
        }

        TradeDoublerSdk.getInstance().retrieveAndSetTduidFromIntent(intent)?.also {
            Toast.makeText(this@MainViewActivity, "new tduid $it",Toast.LENGTH_LONG).show()
        }
    }

    private fun updateViewValues() {
        tduid_text.text = TradeDoublerSdk.getInstance().tduid
        isLoggingCheckBox.isChecked = TradeDoublerSdk.getInstance().isLoggingEnabled
        isTrackingCheckBox.isChecked = TradeDoublerSdk.getInstance().isTrackingEnabled
    }

    override fun onResume() {
        super.onResume()
        refreshPeriodically()
    }

    private fun refreshPeriodically(){
        window?.decorView?.postDelayed({
            if(window?.decorView != null){
                updateViewValues()
                refreshPeriodically()
            }
        },2000)
    }
}