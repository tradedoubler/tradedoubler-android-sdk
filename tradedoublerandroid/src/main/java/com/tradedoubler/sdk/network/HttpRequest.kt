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

package com.tradedoubler.sdk.network

import com.tradedoubler.sdk.BasketInfo
import com.tradedoubler.sdk.ReportInfo
import com.tradedoubler.sdk.TradeDoublerSdkUtils
import com.tradedoubler.sdk.TradeDoublerSdkUtils.format
import com.tradedoubler.sdk.utils.Constant.BASE_TBL_URL
import com.tradedoubler.sdk.utils.Constant.BASE_URL_SALE
import com.tradedoubler.sdk.utils.Constant.BASE_URL_TRACKING_OPEN
import com.tradedoubler.sdk.utils.Constant.CHECK_SUM
import com.tradedoubler.sdk.utils.Constant.CURRENCY
import com.tradedoubler.sdk.utils.Constant.EVENT
import com.tradedoubler.sdk.utils.Constant.EVENT_ID
import com.tradedoubler.sdk.utils.Constant.EXT_ID
import com.tradedoubler.sdk.utils.Constant.EXT_TYP
import com.tradedoubler.sdk.utils.Constant.LEAD_NUMBER
import com.tradedoubler.sdk.utils.Constant.ORDER_NUMBER
import com.tradedoubler.sdk.utils.Constant.ORDER_VALUE
import com.tradedoubler.sdk.utils.Constant.ORGANIZATION
import com.tradedoubler.sdk.utils.Constant.ORGANIZATION_ID
import com.tradedoubler.sdk.utils.Constant.REPORT_INFO
import com.tradedoubler.sdk.utils.Constant.TDUID
import com.tradedoubler.sdk.utils.Constant.VERIFY
import com.tradedoubler.sdk.utils.Constant.VOUCHER
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object HttpRequest {

    fun trackingInstallation(
        organizationId: String?,
        appInstallEventId: String,
        leadNumber: String,
        tduid: String?,
        extId: String?
    ): String {
        val urlBuilder = BASE_TBL_URL.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId)
        urlBuilder.addQueryParameter(EVENT_ID, appInstallEventId)
        urlBuilder.addQueryParameter(LEAD_NUMBER, leadNumber)
        urlBuilder.addQueryParameter(TDUID, tduid)
        urlBuilder.addQueryParameter(EXT_TYP, "1")
        urlBuilder.addQueryParameter(EXT_ID, extId)
        return urlBuilder.toString()
    }

    fun trackingLead(
        organizationId: String?,
        leadEventId: String?,
        leadId: String?,
        tduid: String?,
        extId: String?
    ): String {
        val urlBuilder = BASE_TBL_URL.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId)
        urlBuilder.addQueryParameter(EVENT, leadEventId)
        urlBuilder.addQueryParameter(LEAD_NUMBER, leadId)
        urlBuilder.addQueryParameter(TDUID, tduid)
        urlBuilder.addQueryParameter(EXT_TYP, "1")
        urlBuilder.addQueryParameter(EXT_ID, extId)
        return urlBuilder.toString()
    }

    fun trackingSale(
        organizationId: String?,
        saleEventId: String,
        orderNumber: String,
        orderValue: String,
        currency: String?,
        voucherCode: String?,
        tduid: String?,
        extId: String?,
        reportInfo: ReportInfo?,
        secretCode: String
    ): String {

        val checksum = TradeDoublerSdkUtils.generateCheckSum(secretCode, orderNumber, orderValue)

        val urlBuilder = BASE_TBL_URL.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId)
        urlBuilder.addQueryParameter(EVENT, saleEventId)
        urlBuilder.addQueryParameter(ORDER_NUMBER, orderNumber)
        urlBuilder.addQueryParameter(ORDER_VALUE, orderValue)
        currency?.let {
            urlBuilder.addQueryParameter(CURRENCY, it)
        }
        urlBuilder.addQueryParameter(CHECK_SUM, checksum)
        voucherCode?.let {
            urlBuilder.addQueryParameter(VOUCHER, it)
        }
        urlBuilder.addQueryParameter(TDUID, tduid)
        urlBuilder.addQueryParameter(EXT_TYP, "1")
        urlBuilder.addQueryParameter(EXT_ID, extId)
        reportInfo?.let {
            urlBuilder.addQueryParameter(REPORT_INFO, it.toEncodedString())
        }
        return urlBuilder.toString()
    }

    fun trackingSalePLT(
        organizationId: String?,
        saleEventId: String,
        orderId: String,
        currency: String,
        tduid: String?,
        extId: String?,
        voucherCode: String?,
        basket: BasketInfo,
        secretCode: String
    ): String {
        val checksum = TradeDoublerSdkUtils.generateCheckSum(secretCode, orderId, basket.getOverallPrice().format(2))
        val queryParam = "?o($organizationId)" +
                "event(${saleEventId})" +
                "ordnum($orderId)" +
                "curr($currency)" +
                (if(checksum != null) "chksum(${checksum ?: ""})" else "" ) +
                "tduid(${tduid ?: ""})" +
                "extid($extId)" +
                "exttype(1)" +
                (if(voucherCode != null) "voucher(${voucherCode ?: ""})" else "" ) +
                "enc(3)" +
                basket.toEncodedString()
        return BASE_URL_SALE + queryParam
    }

    fun trackingOpen(
        organizationId: String?,
        extId: String?,
        tduid: String?,
        exttype: String?
    ): String {
        val urlBuilder = BASE_URL_TRACKING_OPEN.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(ORGANIZATION, organizationId)
        urlBuilder.addQueryParameter(EXT_ID, extId)
        urlBuilder.addQueryParameter(EXT_TYP, exttype)
        urlBuilder.addQueryParameter(TDUID, tduid)
        urlBuilder.addQueryParameter(VERIFY, "true")
        return urlBuilder.toString()
    }


}