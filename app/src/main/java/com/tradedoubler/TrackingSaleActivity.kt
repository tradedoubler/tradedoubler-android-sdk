package com.tradedoubler

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tradedoubler.sdk.ReportEntry
import com.tradedoubler.sdk.ReportInfo
import com.tradedoubler.sdk.TradeDoublerSdk
import com.tradedoubler.tradedoublersdk.R
import kotlinx.android.synthetic.main.activity_tracking_sale.*
import kotlinx.android.synthetic.main.activity_tracking_sale.priceEditText
import kotlinx.android.synthetic.main.activity_tracking_sale.priceInputLayout
import kotlinx.android.synthetic.main.activity_tracking_sale.productNameInputLayout
import kotlinx.android.synthetic.main.activity_tracking_sale.quantityEditText
import kotlinx.android.synthetic.main.activity_tracking_sale.quantityInputLayout
import kotlinx.android.synthetic.main.activity_tracking_sale_plt.*
import java.util.*

class TrackingSaleActivity : AppCompatActivity() {

    private lateinit var reportInfoAdapter: ReportInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_sale)
        setRecycleView()
        setSpinnerCurrency()

        add_report_info.setOnClickListener {

            val productName = productNameEditText.value
            val price = priceEditText.value
            val quantity = quantityEditText.value

            var hasError = false

            if (productName.isEmpty()) {
                hasError = true
                productNameInputLayout.error = "Provide the paramter product name."
            }

            if (price.isEmpty()) {
                hasError = true
                priceInputLayout.error = "Provide the paramter price."
            }

            if (price.isNotEmpty() && price.toDouble() <= 0.00) {
                hasError = true
                priceInputLayout.error = "Paramter price cannot be negative."
            }

            if (quantity.isEmpty()) {
                hasError = true
                quantityInputLayout.error = "Provide the paramter quantity."
            }

            if (quantity.isNotEmpty() && quantity.toInt() <= 0) {
                hasError = true
                quantityInputLayout.error = "Paramter price cannot be negative."
            }

            if (!hasError) {
                val reportEntry = ReportEntry(id = randomInt(), productName = productName, price = price.toDouble(), quantity = quantity.toInt())
                reportInfoAdapter.addReportEntry(reportEntry)
            }
        }

        call_tracking_sale_button.setOnClickListener {
            var hasError = false
            val eventId = saleEventIdEditText.value
            val orderValue = orderValueEditText.value
            val voucherCode = voucherCodeEditText.value
            val currency = currencySpinner.selectedItem.toString()

                if (reportInfoAdapter.data.size == 0) {
                    hasError = true
                    Toast.makeText(this, "Please add anything Reports", Toast.LENGTH_LONG).show()
                }

            if (eventId.isEmpty()){
                hasError = true
                saleEventIdInputLayout.error = "Provide the paramter event Id."
            }

            if (orderValue.isEmpty()) {
                hasError = true
                orderValueInputLayout.error = "Provide the paramter order value."
            }

            if (orderValue.isNotEmpty() && orderValue.toDouble() <=0.00){
                orderValueInputLayout.error = "Paramter orderValue cannot be negative."
            }

            if (!hasError) {

                TradeDoublerSdk.getInstance()
                    .trackSale(
                        saleEventId = eventId,
                        orderNumber = generateId(5),
                        orderValue = orderValue,
                        voucherCode = voucherCode,
                        currency = Currency.getInstance(currency),
                        reportInfo = ReportInfo(reportEntries = reportInfoAdapter.data.toList())
                    )
            }

        }
    }


    private fun setRecycleView() {
        reportInfoAdapter = ReportInfoAdapter()
        reportInfoList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reportInfoAdapter
        }
    }

    private fun setSpinnerCurrency() {
        val currency = resources.getStringArray(R.array.currency)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, currency
        )
        currencySpinner.adapter = adapter

    }
}