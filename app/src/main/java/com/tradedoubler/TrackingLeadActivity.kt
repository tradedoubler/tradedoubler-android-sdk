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

        val eventId = eventIdEditText.value
        val leadId = leadIdEditText.value

        call_tracking_lead_button.setOnClickListener {

            var hasError = false

            if (eventId.isEmpty()){
                hasError = true
                eventIdInputLayout.error  = "Provide the paramter event Id"
            }

            if (leadId.isEmpty()){
                hasError = true
                leadIdInputLayout.error = "Provide the paramter lead Id"
            }

            if (!hasError){
                TradeDoublerSdk.getInstance().trackLead(eventId, leadId)
            }
        }

    }
}