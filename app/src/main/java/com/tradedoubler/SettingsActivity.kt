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

        organizationIdInput.value = TradeDoublerSdk.getInstance().organizationId ?: ""
        userEmailInput.value = TradeDoublerSdk.getInstance().userEmail ?: ""
        secretCodeInput.value = TradeDoublerSdk.getInstance().secretCode ?: ""

        tracking_open_button.setOnClickListener {

            val organizationId = organizationIdInput.value
            val userEmail = userEmailInput.value

            var hasError = false

            if (organizationId.isEmpty()) {
                hasError = true
                organizationIdInputLayout.error = "Provide the parameter organization Id"
            } else {
                TradeDoublerSdk.getInstance().organizationId = organizationId
            }

            if (!userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                userEmailInputLayout.error = "Incorrect email"
            }

            if (userEmail.isValidEmail()) {
                if(TradeDoublerSdk.getInstance().userEmail != userEmail) {
                    TradeDoublerSdk.getInstance().userEmail = userEmail
                }
            }else{
                TradeDoublerSdk.getInstance().userEmail = null
            }
        }


        tracking_lead_button.setOnClickListener {

            val organizationId = organizationIdInput.value
            val userEmail = userEmailInput.value
            val secretCode = secretCodeInput.value

            var hasError = false

            if (organizationId.isEmpty()) {
                hasError = true
                organizationIdInputLayout.error = "Provide the parameter organization Id"
            } else {
                TradeDoublerSdk.getInstance().organizationId = organizationId
            }

            if (!userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                userEmailInputLayout.error = "Incorrect email"
            }

            if (userEmail.isValidEmail()) {
                if(TradeDoublerSdk.getInstance().userEmail != userEmail) {
                    TradeDoublerSdk.getInstance().userEmail = userEmail
                }
            }else{
                TradeDoublerSdk.getInstance().userEmail = null
            }

            if (!hasError){
                val intent = Intent(this , TrackingLeadActivity::class.java)
                startActivity(intent)
            }
        }

        tracking_sale_button.setOnClickListener {

            val organizationId = organizationIdInput.value
            val userEmail = userEmailInput.value
            val secretCode = secretCodeInput.value

            var hasError = false

            if (organizationId.isEmpty()) {
                hasError = true
                organizationIdInputLayout.error = "Provide the parameter organization Id"
            } else {
                TradeDoublerSdk.getInstance().organizationId = organizationId
            }

            if (!userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                userEmailInputLayout.error = "Incorrect email"
            }

            if (userEmail.isValidEmail()) {
                if(TradeDoublerSdk.getInstance().userEmail != userEmail) {
                    TradeDoublerSdk.getInstance().userEmail = userEmail
                }
            }else{
                TradeDoublerSdk.getInstance().userEmail = null
            }

            if (secretCode.isEmpty()){
                hasError = true
                secertCodeInputLayout.error = "Provide the parameter secret Code"
            }else {
                TradeDoublerSdk.getInstance().secretCode = secretCode
            }

            if (!hasError){
                val intent = Intent(this , TrackingSaleActivity::class.java)
                startActivity(intent)
            }
        }

        tracking_sale_plt_button.setOnClickListener {
            val organizationId = organizationIdInput.value
            val userEmail = userEmailInput.value
            val secretCode = secretCodeInput.value

            var hasError = false

            if (organizationId.isEmpty()) {
                hasError = true
                organizationIdInputLayout.error = "Provide the parameter organization Id"
            } else {
                TradeDoublerSdk.getInstance().organizationId = organizationId
            }

            if (!userEmail.isValidEmail() && userEmail.isNotEmpty()) {
                userEmailInputLayout.error = "Incorrect email"
            }

            if (userEmail.isValidEmail()) {
                if(TradeDoublerSdk.getInstance().userEmail != userEmail) {
                    TradeDoublerSdk.getInstance().userEmail = userEmail
                }
            }else{
                TradeDoublerSdk.getInstance().userEmail = null
            }

            if (secretCode.isEmpty()){
                hasError = true
                secertCodeInputLayout.error = "Provide the parameter secret Code"
            }else {
                TradeDoublerSdk.getInstance().secretCode = secretCode
            }

            if (!hasError){
                val intent = Intent(this , TrackingSalePltActivity::class.java)
                startActivity(intent)
            }
        }

    }
}