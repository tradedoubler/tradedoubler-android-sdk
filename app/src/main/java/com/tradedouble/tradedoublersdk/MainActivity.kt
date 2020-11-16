package com.tradedouble.tradedoublersdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val googleId = AdvertisingId.getAdvertisingId(this)
        Toast.makeText(this, "Google ${googleId}", Toast.LENGTH_SHORT).show()

    }
}