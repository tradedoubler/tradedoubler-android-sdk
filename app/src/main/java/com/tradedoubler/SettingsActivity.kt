package com.tradedoubler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tradedoubler.sdk.TradeDoublerSdk
import com.tradedoubler.tradedoublersdk.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val organizationId = organizationIdInput.value
        val userEmail = userEmailInput.value
        var secretCode = secretCodeInput.value

        tracking_open_button.setOnClickListener {

            var hasError = false


            if (organizationId.isEmpty()) {
                hasError = true
                organizationIdInputLayout.error = "Provide the paramter organization Id"
            } else {
                TradeDoublerSdk.getInstance().organizationId = organizationId
            }

            if (!userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                userEmailInputLayout.error = "Incorrect email"
            }

            if (userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                TradeDoublerSdk.getInstance().userEmail = userEmail
            }

            if (!hasError){
                TradeDoublerSdk.getInstance().trackOpenApp()
            }

        }


        tracking_lead_button.setOnClickListener {

            var hasError = false

            if (organizationId.isEmpty()) {
                hasError = true
                organizationIdInputLayout.error = "Provide the paramter organization Id"
            } else {
                TradeDoublerSdk.getInstance().organizationId = organizationId
            }

            if (!userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                userEmailInputLayout.error = "Incorrect email"
            }

            if (userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                TradeDoublerSdk.getInstance().userEmail = userEmail
            }

            if (!hasError){

                val intent = Intent(this , SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        tracking_sale_button.setOnClickListener {

            var hasError = false

            if (organizationId.isEmpty()) {
                hasError = true
                organizationIdInputLayout.error = "Provide the paramter organization Id"
            } else {
                TradeDoublerSdk.getInstance().organizationId = organizationId
            }

            if (!userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                userEmailInputLayout.error = "Incorrect email"
            }

            if (userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                TradeDoublerSdk.getInstance().userEmail = userEmail
            }
        }
    }
}