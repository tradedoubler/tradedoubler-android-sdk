package com.tradedoubler

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tradedoubler.sdk.BasketEntry
import com.tradedoubler.sdk.BasketInfo
import com.tradedoubler.sdk.ReportInfo
import com.tradedoubler.sdk.TradeDoublerSdk
import com.tradedoubler.tradedoublersdk.R
import kotlinx.android.synthetic.main.activity_tracking_sale.*
import kotlinx.android.synthetic.main.activity_tracking_sale_plt.*
import kotlinx.android.synthetic.main.activity_tracking_sale_plt.priceEditText
import kotlinx.android.synthetic.main.activity_tracking_sale_plt.priceInputLayout
import kotlinx.android.synthetic.main.activity_tracking_sale_plt.productNameInputLayout
import kotlinx.android.synthetic.main.activity_tracking_sale_plt.quantityEditText
import kotlinx.android.synthetic.main.activity_tracking_sale_plt.quantityInputLayout
import java.util.*

class TrackingSalePltActivity : AppCompatActivity() {

    private lateinit var basketInfoAdapter: BasketInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_sale_plt)
        setRecycleView()
        setSpinnerCurrency()
        setSpinnerGroup()

        eventIdEditText.setText("51")

        add_basket_info.setOnClickListener {
            val productName = productNameBasketEditText.value
            val price = priceEditText.value
            val quantity = quantityEditText.value

            var hasError = false

            if (productName.isEmpty()) {
                hasError = true
                productNameInputLayout.error = "Provide the parameter product name."
            }

            if (price.isEmpty()) {
                hasError = true
                priceInputLayout.error = "Provide the parameter price."
            }

            if (price.isNotEmpty() && price.toDouble() <= 0.00) {
                hasError = true
                priceInputLayout.error = "parameter price cannot be negative."
            }

            if (quantity.isEmpty()) {
                hasError = true
                quantityInputLayout.error = "Provide the parameter quantity."
            }

            if (quantity.isNotEmpty() && quantity.toInt() <= 0) {
                hasError = true
                quantityInputLayout.error = "parameter price cannot be negative."
            }

            val group = groupPltSpinner.selectedItem.toString()

            if (!hasError) {
                val basketEntry =
                    BasketEntry(group = group, id = randomInt(), productName = productName, price = price.toDouble(), quantity = quantity.toInt())
                basketInfoAdapter.addBasketEntry(basketEntry)
            }
        }

        call_tracking_sale_plt_button.setOnClickListener {
            var hasError = false
            val eventId = eventIdEditText.value
            val voucherCode = voucherCodeSaleEditText.value
            val currency = currencyPltSpinner.selectedItem.toString()


            if (basketInfoAdapter.data.size == 0) {
                hasError = true
                Toast.makeText(this, "Please add anything Basket", Toast.LENGTH_LONG).show()
            }

            if (eventId.isEmpty()) {
                hasError = true
                eventIdInputLayout.error = "Provide the parameter event Id."
            }

            if (!hasError) {
                TradeDoublerSdk.getInstance()
                    .trackSalePlt(
                        saleEventId = eventId,
                        orderNumber = generateId(5),
                        currency = Currency.getInstance(currency)!!,
                        voucherCode = voucherCode,
                        basketInfo = BasketInfo(basketInfoAdapter.data.toList())
                    )
            }
        }

    }


    private fun setRecycleView() {
        basketInfoAdapter = BasketInfoAdapter()
        basketInfoList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = basketInfoAdapter
        }
    }

    private fun setSpinnerCurrency() {
        val currency = resources.getStringArray(R.array.currency)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, currency
        )
        currencyPltSpinner.adapter = adapter
    }

    private fun setSpinnerGroup() {
        val groups = mutableListOf<String>().apply {
            add("3408")
            add("3168")
        }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, groups
        )
        groupPltSpinner.adapter = adapter
    }

}