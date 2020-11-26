package com.tradedouble.tradedoublerandroid.network

import com.tradedouble.tradedoublerandroid.TraderDoublerSdkUtils
import com.tradedouble.tradedoublerandroid.utils.Constant.A
import com.tradedouble.tradedoublerandroid.utils.Constant.BASE_URL_SALE
import com.tradedouble.tradedoublerandroid.utils.Constant.BASE_URL_TRACKING_OPEN
import com.tradedouble.tradedoublerandroid.utils.Constant.BASKET
import com.tradedouble.tradedoublerandroid.utils.Constant.CHECK_SUM
import com.tradedouble.tradedoublerandroid.utils.Constant.CURR
import com.tradedouble.tradedoublerandroid.utils.Constant.CURRENCY
import com.tradedouble.tradedoublerandroid.utils.Constant.ENC
import com.tradedouble.tradedoublerandroid.utils.Constant.EVENT
import com.tradedouble.tradedoublerandroid.utils.Constant.EVENT_ID
import com.tradedouble.tradedoublerandroid.utils.Constant.EXT_ID
import com.tradedouble.tradedoublerandroid.utils.Constant.EXT_TYP
import com.tradedouble.tradedoublerandroid.utils.Constant.G
import com.tradedouble.tradedoublerandroid.utils.Constant.LEAD_NUMBER
import com.tradedouble.tradedoublerandroid.utils.Constant.ORDER_NUMBER
import com.tradedouble.tradedoublerandroid.utils.Constant.ORDER_VALUE
import com.tradedouble.tradedoublerandroid.utils.Constant.ORDNUM
import com.tradedouble.tradedoublerandroid.utils.Constant.ORGANIZATION
import com.tradedouble.tradedoublerandroid.utils.Constant.ORGANIZATION_ID
import com.tradedouble.tradedoublerandroid.utils.Constant.P
import com.tradedouble.tradedoublerandroid.utils.Constant.REPORT_INFO
import com.tradedouble.tradedoublerandroid.utils.Constant.TDUID
import com.tradedouble.tradedoublerandroid.utils.Constant.VERIFY
import com.tradedouble.tradedoublerandroid.utils.Constant.VOUCHER
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object HttpRequest {

    fun clickLinkAffiliate(): String {
        val urlBuilder = BASE_URL_SALE.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(P, "310409")
        urlBuilder.addQueryParameter(A, "982247")
        urlBuilder.addQueryParameter(G, "0")
        return urlBuilder.toString()
    }


    fun trackingInstallation(
        organizationId: String?,
        appInstallEventId: String,
        leadNumber: String,
        tduid: String?,
        extId: String?): String {
        val urlBuilder = BASE_URL_SALE.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId)
        urlBuilder.addQueryParameter(EVENT_ID, appInstallEventId)
        urlBuilder.addQueryParameter(LEAD_NUMBER, leadNumber)
        urlBuilder.addQueryParameter(TDUID,tduid)
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
        val urlBuilder = BASE_URL_SALE.toHttpUrlOrNull()!!.newBuilder()
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
        secretCode: String,
        voucherCode: String?,
        tduid: String?,
        extId: String?,
        reportInfo: String?
    ): String {

        val checksum  = TraderDoublerSdkUtils.generateCheckSum(secretCode, orderNumber, orderValue)

        val urlBuilder = BASE_URL_SALE.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId)
        urlBuilder.addQueryParameter(EVENT, saleEventId)
        urlBuilder.addQueryParameter(ORDER_NUMBER, orderNumber)
        urlBuilder.addQueryParameter(ORDER_VALUE, orderValue)
        urlBuilder.addQueryParameter(CURRENCY, currency)
        urlBuilder.addQueryParameter(CHECK_SUM, checksum)
        if (voucherCode != null){
            urlBuilder.addQueryParameter(VOUCHER, voucherCode)
        }
        urlBuilder.addQueryParameter(TDUID, tduid)
        urlBuilder.addQueryParameter(EXT_TYP, "1")
        urlBuilder.addQueryParameter(EXT_ID, extId)
        if (reportInfo != null){
            urlBuilder.addQueryParameter(REPORT_INFO, reportInfo)
        }
        return urlBuilder.toString()
    }

    fun trackingSalePLT(
        organizationId: String?,
        pltEventId: String,
        orderId: String,
        currency: String,
        tduid: String?,
        extId: String?,
        voucherCode: String?,
        basket: String?
    ): String {
        val urlBuilder = BASE_URL_SALE.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId)
        urlBuilder.addQueryParameter(EVENT, pltEventId)
        urlBuilder.addQueryParameter(ORDNUM, orderId)
        urlBuilder.addQueryParameter(CURR, currency)
        urlBuilder.addQueryParameter(TDUID, tduid)
        urlBuilder.addQueryParameter(EXT_ID, extId)
        urlBuilder.addQueryParameter(EXT_TYP, "1")
        urlBuilder.addQueryParameter(VOUCHER, voucherCode)
        urlBuilder.addQueryParameter(ENC, "3")
        urlBuilder.addQueryParameter(BASKET, basket)
        return urlBuilder.toString()
    }

    fun trackingOpen(
        organizationId: String?,
        extId: String?,
        tduid: String?
    ): String {
        val urlBuilder = BASE_URL_TRACKING_OPEN.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(ORGANIZATION, organizationId)
        urlBuilder.addQueryParameter(EXT_ID, extId)
        urlBuilder.addQueryParameter(EXT_TYP, "1")
        urlBuilder.addQueryParameter(TDUID, tduid)
        urlBuilder.addQueryParameter(VERIFY, "true")
        return urlBuilder.toString()
    }


}