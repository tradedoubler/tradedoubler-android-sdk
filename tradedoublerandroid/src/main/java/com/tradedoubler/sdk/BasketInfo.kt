package com.tradedoubler.sdk

import java.net.URLEncoder

data class BasketInfo(val reportEntries: List<BasketEntry>) {
    fun toEncodedString(): String {
        return "basket(${reportEntries.joinToString("") { it.toEncodedString() }})"
    }
}

data class BasketEntry(val group: String, val id: String, val productName: String, val price: Double, val quantity: Int) {
    fun toEncodedString(): String {
        return "pr(" +
                "gr(${URLEncoder.encode(group, Charsets.UTF_8.name())})" +
                "i(${URLEncoder.encode(id, Charsets.UTF_8.name())})" +
                "n(${URLEncoder.encode(productName, Charsets.UTF_8.name())})" +
                "v(${price})q(${quantity})" +
                ")"
    }
}