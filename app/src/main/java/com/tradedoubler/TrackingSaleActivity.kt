package com.tradedoubler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tradedoubler.tradedoublersdk.R

class TrackingSaleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_sale)

        val extras = intent.extras
        if (extras != null) {
            val locationId = extras.getString(SECRET_CODE)

        }

    }
}