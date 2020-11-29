package com.tradedoubler.sdk.network

import com.tradedoubler.sdk.BasketInfo
import com.tradedoubler.sdk.ReportInfo
import com.tradedoubler.sdk.TradeDoublerSdkUtils
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
        currency: String,
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
        urlBuilder.addQueryParameter(CURRENCY, currency)
        urlBuilder.addQueryParameter(CHECK_SUM, checksum)
        if (voucherCode != null) {
            urlBuilder.addQueryParameter(VOUCHER, voucherCode)
        }
        urlBuilder.addQueryParameter(TDUID, tduid)
        urlBuilder.addQueryParameter(EXT_TYP, "1")
        urlBuilder.addQueryParameter(EXT_ID, extId)
        if (reportInfo != null) {
            urlBuilder.addQueryParameter(REPORT_INFO, reportInfo.toEncodedString())
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
        basket: BasketInfo
    ): String {
        val queryParam = "?o($organizationId)" +
                "event(${saleEventId})" +
                "ordnum($orderId)" +
                "curr($currency)" +
                //"chksum(v0477007e101263751dba5148752ac9eb9d)" + TODO checksum
                "tduid(${tduid ?: ""})" +
                "extid($extId)" +
                "exttype(1)" +
                // "type(iframe)" +
                //"voucher(${voucherCode ?: ""})" + TODO VOUCHER
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