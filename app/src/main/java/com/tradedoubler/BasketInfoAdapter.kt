package com.tradedoubler

import android.text.SpannableStringBuilder
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tradedoubler.baseAdapter.BaseAdapter
import com.tradedoubler.sdk.BasketEntry
import com.tradedoubler.sdk.ReportEntry
import com.tradedoubler.tradedoublersdk.R
import kotlinx.android.synthetic.main.simple_text_item.view.*

class BasketInfoAdapter : BaseAdapter<BasketEntry, BasketInfoAdapter.BasketInfoViewHolder>() {

    class BasketInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: BasketInfoViewHolder, item: BasketEntry, position: Int) {
        holder.itemView.apply {
            val spanText = SpannableStringBuilder()
            spanText.append("Id : ${item.id}")
            spanText.append("\n")
            spanText.append("Product :${item.productName}")
            spanText.append("\n")
            spanText.append("Price: ${item.price} Quantity: ${item.quantity}")
            text_view.text = spanText.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketInfoViewHolder = BasketInfoViewHolder(inflate(parent, R.layout.simple_text_item))


    fun addBasketEntry(item: BasketEntry) {
        data.add(item)
        notifyDataSetChanged()
    }
}