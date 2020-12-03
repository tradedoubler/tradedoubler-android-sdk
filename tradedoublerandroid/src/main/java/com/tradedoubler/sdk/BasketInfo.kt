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