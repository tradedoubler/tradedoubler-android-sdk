package com.tradedoubler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tradedoubler.sdk.TradeDoublerSdk
import com.tradedoubler.tradedoublersdk.R
import kotlinx.android.synthetic.main.activity_tracking_lead.*

class TrackingLeadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_lead)

        eventIdEditText.setText("51")

        call_tracking_lead_button.setOnClickListener {

            val eventId = eventIdEditText.value
            val leadId = leadIdEditText.value

            var hasError = false

            if (eventId.isEmpty()){
                hasError = true
                eventIdInputLayout.error  = "Provide the parameter event Id"
            }

            if (leadId.isEmpty()){
                hasError = true
                leadIdInputLayout.error = "Provide the parameter lead Id"
            }

            if (!hasError){
                TradeDoublerSdk.getInstance().trackLead(eventId, leadId)
            }
        }

    }
}