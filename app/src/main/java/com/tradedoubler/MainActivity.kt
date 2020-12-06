/*
 * Copyright 2020 Tradedoubler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tradedoubler

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.github.vivchar.rendererrecyclerviewadapter.*
import com.tradedoubler.sdk.*
import com.tradedoubler.tradedoublersdk.R
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')


    private val adapter = RendererRecyclerViewAdapter()
    private val listItems: MutableList<ViewModel> = mutableListOf()

    private val saleEventId = "403759"
    private val installEventId = "403761"
    private val sale2EventId = "403763"
    private val leadEventId = "403765"
    private val basketSale = "51"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter.enableDiffUtil()
        adapter.registerRenderer(
            ViewRenderer(R.layout.simple_text_item, TextModel::class.java,
                BaseViewRenderer.Binder<TextModel, ViewFinder> { model, finder, _ ->
                    finder.find<TextView>(R.id.text_view).text = model.text
                })
        )

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        TradeDoublerSdk.create(applicationContext,initClient())
        TradeDoublerSdk.getInstance().isLoggingEnabled = true
        TradeDoublerSdk.getInstance().organizationId = "945630"
        TradeDoublerSdk.getInstance().secretCode = "12345678"

        info.setOnClickListener {
            addItem("tduid: ${TradeDoublerSdk.getInstance().tduid}", "advertisingId: ${TradeDoublerSdk.getInstance().advertisingId}")
        }

//        aaid.setOnClickListener {
//            TradeDoublerSdk.getInstance().useAdvertisingId = true
//        }

        referrer_btn.setOnClickListener {
            TradeDoublerSdk.getInstance().useInstallReferrer = true

            retrieveReferrer()
        }

        open.setOnClickListener {
            TradeDoublerSdk.getInstance().trackOpenApp()
        }

        install.setOnClickListener {
            TradeDoublerSdk.getInstance().trackInstall(installEventId)
        }

        open_app.setOnClickListener {
            addItem("activity intent: ${intent.data}")
            val newTduid = TradeDoublerSdk.getInstance().retrieveAndSetTduidFromIntent(intent)
            addItem("tduid for open: $newTduid")
        }

        //
        // sale lead
        //

        lead.setOnClickListener {
            TradeDoublerSdk.getInstance().trackLead(leadEventId,"myLeadId")
        }

        sale.setOnClickListener {
            val orderNumber = generateId(10)
            val reportInfo = ReportInfo(
                listOf(
                    ReportEntry("25","carrrxdxdóśćżćł", 21.0,3),
                    ReportEntry("453","bikeeexdóśćżćł", 3.5,12)
                )
            )
            TradeDoublerSdk.getInstance().trackSale(saleEventId,orderNumber,23.56, Currency.getInstance("PLN"),"13131313", reportInfo)
        }

        sale_plt.setOnClickListener {
            val orderNumber = generateId(10)
            val reportInfo = BasketInfo(
                listOf(
                    BasketEntry("3408",generateId(3),"plt_cookieóśćżćł_chksm", 23.0,5),
                    BasketEntry("3168",generateId(3),"plt_milkóśćżćł_chksm", 3.0,25)
                )
            )
            TradeDoublerSdk.getInstance().trackSalePlt(basketSale,orderNumber, Currency.getInstance("PLN"),"13131313", reportInfo)
        }

    }

    private fun retrieveReferrer(){
        val referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        val referrerUrl  = response.installReferrer
                        addItem("full referrer $referrerUrl")
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        addItem("Install referrer, feature not supported")
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        addItem("Install referrer, service unavailable")
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> {
                        addItem("Install referrer, service disconnected")
                    }

                    InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {
                        addItem("Install referrer, developer error")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                addItem("Install referrer, developer error")
            }
        })
    }


    private fun addItem(vararg values: String){
        for (item in values) {
            listItems.add(TextModel(item))
        }
        adapter.setItems(listItems.toMutableList())
    }

    private fun generateId(length: Int): String{
        return (1..length)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun initClient(): OkHttpClient {
        val loggingInterceptor = Interceptor{
            window?.decorView?.post {
                addItem(it.request().url.toString())
            }
            it.proceed(it.request())
        }

        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}

data class TextModel(val text: String): ViewModel