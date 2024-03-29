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
import java.net.URLEncoder

data class ReportInfo(val reportEntries: List<ReportEntry>): Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(ReportEntry)!!)

    fun toEncodedString(): String {
        return reportEntries.joinToString("|") { it.toEncodedString() }
    }

    fun getOrderValue(): Double{
        return reportEntries.sumByDouble { it.getOverallPrice() }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(reportEntries)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReportInfo> {
        override fun createFromParcel(parcel: Parcel): ReportInfo {
            return ReportInfo(parcel)
        }

        override fun newArray(size: Int): Array<ReportInfo?> {
            return arrayOfNulls(size)
        }
    }
}

data class ReportEntry(val id: String, val productName: String, val price: Double, val quantity: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt()
    )

    fun toEncodedString(): String {
        return "f1=${URLEncoder.encode(id, Charsets.UTF_8.name())}&f2=${productName}&f3=${price}&f4=${quantity}"
    }

    fun getOverallPrice(): Double{
        return price * quantity
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productName)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReportEntry> {
        override fun createFromParcel(parcel: Parcel): ReportEntry {
            return ReportEntry(parcel)
        }

        override fun newArray(size: Int): Array<ReportEntry?> {
            return arrayOfNulls(size)
        }
    }
}

