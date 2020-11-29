package com.tradedoubler.sdk

import java.net.URLEncoder

data class ReportInfo(val reportEntries: List<ReportEntry>) {
    fun toEncodedString(): String {
        return reportEntries.joinToString("|") { it.toEncodedString() }
    }
}

data class ReportEntry(val id: String, val productName: String, val price: Double, val quantity: Int) {
    fun toEncodedString(): String {
        return "f1=${URLEncoder.encode(id, Charsets.UTF_8.name())}&f2=${URLEncoder.encode(productName, Charsets.UTF_8.name())}&f3=${price}&f4=${quantity}"
    }
}

