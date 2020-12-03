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

