package com.tradedouble.tradedoublerandroid.network;

import okhttp3.HttpUrl;


import static com.tradedouble.tradedoublerandroid.utils.Constant.BASE_URL_SALE;
import static com.tradedouble.tradedoublerandroid.utils.Constant.BASE_URL_TRACKING_OPEN;
import static com.tradedouble.tradedoublerandroid.utils.Constant.CURRENCY;
import static com.tradedouble.tradedoublerandroid.utils.Constant.EVENT;
import static com.tradedouble.tradedoublerandroid.utils.Constant.EXT_ID;
import static com.tradedouble.tradedoublerandroid.utils.Constant.EXT_TYP;
import static com.tradedouble.tradedoublerandroid.utils.Constant.LEAD_NUMBER;
import static com.tradedouble.tradedoublerandroid.utils.Constant.ORDER_NUMBER;
import static com.tradedouble.tradedoublerandroid.utils.Constant.ORDER_VALUE;
import static com.tradedouble.tradedoublerandroid.utils.Constant.ORGANIZATION;
import static com.tradedouble.tradedoublerandroid.utils.Constant.ORGANIZATION_ID;
import static com.tradedouble.tradedoublerandroid.utils.Constant.REPORT_INFO;
import static com.tradedouble.tradedoublerandroid.utils.Constant.TDUID;
import static com.tradedouble.tradedoublerandroid.utils.Constant.VERIFY;
import static com.tradedouble.tradedoublerandroid.utils.Constant.VOUCHER;

public class HttpRequest {



    public HttpRequest() {
    }

    public static String trackingLead(String organizationId, String leadEventId, String leadId, String tduid, String extId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL_SALE).newBuilder();
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId);
        urlBuilder.addQueryParameter(EVENT, leadEventId);
        urlBuilder.addQueryParameter(LEAD_NUMBER, leadId);
        urlBuilder.addQueryParameter(TDUID, tduid);
        urlBuilder.addQueryParameter(EXT_TYP, "1");
        urlBuilder.addQueryParameter(EXT_ID, extId);
        return urlBuilder.toString();
    }

    public static String trackingSale(String organizationId, String saleEventId, String orderNumber, String orderValue, String currency, String voucherCode, String tduid, String extId, String reportInfo) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL_SALE).newBuilder();
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId);
        urlBuilder.addQueryParameter(EVENT, saleEventId);
        urlBuilder.addQueryParameter(ORDER_NUMBER, orderNumber);
        urlBuilder.addQueryParameter(ORDER_VALUE, orderValue);
        urlBuilder.addQueryParameter(CURRENCY, currency);
        urlBuilder.addQueryParameter(VOUCHER, voucherCode);
        urlBuilder.addQueryParameter(TDUID, tduid);
        urlBuilder.addQueryParameter(EXT_TYP, "1");
        urlBuilder.addQueryParameter(EXT_ID, extId);
        urlBuilder.addQueryParameter(REPORT_INFO, reportInfo);
        return urlBuilder.toString();
    }


    public static String trackingSalePLT() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL_SALE).newBuilder();
        return urlBuilder.toString();
    }


    public static String trackingOpen(String organizationId, String extId, String tduid, String exttype) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL_TRACKING_OPEN).newBuilder();
        urlBuilder.addQueryParameter(ORGANIZATION, organizationId);
        urlBuilder.addQueryParameter(EXT_ID, extId);
        urlBuilder.addQueryParameter(EXT_TYP, exttype);
        urlBuilder.addQueryParameter(TDUID, tduid);
        urlBuilder.addQueryParameter(VERIFY, "true");
        return urlBuilder.toString();
    }

}
