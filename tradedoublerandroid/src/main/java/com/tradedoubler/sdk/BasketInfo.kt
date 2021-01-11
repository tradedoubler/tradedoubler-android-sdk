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

package com.tradedoubler.sdk

import android.os.Parcel
import android.os.Parcelable
import com.tradedoubler.sdk.TradeDoublerSdkUtils.format
import java.net.URLEncoder

data class BasketInfo(val basketEntries: List<BasketEntry>): Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(BasketEntry)!!)

    fun toEncodedString(): String {
        return "basket(${basketEntries.joinToString("") { it.toEncodedString() }})"
    }

    fun getOrderValue(): Double{
        return basketEntries.sumByDouble { it.getOverallPrice() }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(basketEntries)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BasketInfo> {
        override fun createFromParcel(parcel: Parcel): BasketInfo {
            return BasketInfo(parcel)
        }

        override fun newArray(size: Int): Array<BasketInfo?> {
            return arrayOfNulls(size)
        }
    }
}

data class BasketEntry(val group: String, val id: String, val productName: String, val price: Double, val quantity: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt()
    )

    fun toEncodedString(): String {
        return "pr(" +
                "gr(${URLEncoder.encode(group, Charsets.UTF_8.name())})" +
                "i(${URLEncoder.encode(id, Charsets.UTF_8.name())})" +
                "n(${URLEncoder.encode(productName, Charsets.UTF_8.name())})" +
                "v(${price.format(2)})q(${quantity})" +
                ")"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(group)
        parcel.writeString(id)
        parcel.writeString(productName)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
    }

    fun getOverallPrice(): Double{
        return price * quantity
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BasketEntry> {
        override fun createFromParcel(parcel: Parcel): BasketEntry {
            return BasketEntry(parcel)
        }

        override fun newArray(size: Int): Array<BasketEntry?> {
            return arrayOfNulls(size)
        }
    }
}