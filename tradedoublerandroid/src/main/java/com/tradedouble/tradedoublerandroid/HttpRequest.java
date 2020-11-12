package com.tradedouble.tradedoublerandroid;

import com.tradedouble.tradedoublerandroid.utils.Constans;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static com.tradedouble.tradedoublerandroid.utils.Constans.EVENT;
import static com.tradedouble.tradedoublerandroid.utils.Constans.EXT_ID;
import static com.tradedouble.tradedoublerandroid.utils.Constans.EXT_TYP;
import static com.tradedouble.tradedoublerandroid.utils.Constans.ORGANIZATION_ID;
import static com.tradedouble.tradedoublerandroid.utils.Constans.LEAD_NUMBER;
import static com.tradedouble.tradedoublerandroid.utils.Constans.TDUID;

public class HttpRequest {


    public String trackingLead(String organizationId, String leadEventId, String leadId, String tduid, String extId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constans.BASE_URL_SALE).newBuilder();
        urlBuilder.addQueryParameter(ORGANIZATION_ID, organizationId);
        urlBuilder.addQueryParameter(EVENT, leadEventId);
        urlBuilder.addQueryParameter(LEAD_NUMBER, leadId);
        urlBuilder.addQueryParameter(TDUID, tduid);
        urlBuilder.addQueryParameter(EXT_TYP, "1");
        urlBuilder.addQueryParameter(EXT_ID, extId);
        return urlBuilder.toString();
    }


}
