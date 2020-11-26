package com.tradedouble.tradedoublersdk


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tradedouble.tradedoublerandroid.TraderDoublerSdk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        TraderDoublerSdk.getInstance()?.callTrackingOpenURl()
    }

}