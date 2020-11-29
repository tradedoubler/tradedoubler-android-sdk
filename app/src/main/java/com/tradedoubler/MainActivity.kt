package com.tradedoubler


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vivchar.rendererrecyclerviewadapter.*
import com.tradedoubler.sdk.TradeDoublerSdk
import com.tradedoubler.tradedoublersdk.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = RendererRecyclerViewAdapter()
    private val listItems: MutableList<ViewModel> = mutableListOf()

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

        info.setOnClickListener {
            addItem("tduid: ${TradeDoublerSdk.getInstance().tduid}", "advertisingId: ${TradeDoublerSdk.getInstance().deviceIdentifier}")
        }

        aaid.setOnClickListener {
            TradeDoublerSdk.getInstance().automaticDeviceIdentifierRetrieval = true
        }

        referrer_btn.setOnClickListener {
            TradeDoublerSdk.getInstance().automaticInstallReferrerRetrieval = true
        }


    }

    private fun addItem(vararg values: String){
        for (item in values) {
            listItems.add(TextModel(item))
        }
        adapter.setItems(listItems.toMutableList())
    }

}

data class TextModel(val text: String): ViewModel{

}