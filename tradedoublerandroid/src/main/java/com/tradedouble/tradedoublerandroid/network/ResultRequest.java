package com.tradedouble.tradedoublerandroid.network;

public interface ResultRequest {
    void onFailure(int code);
    void onResponseSuccess(int code);
}
