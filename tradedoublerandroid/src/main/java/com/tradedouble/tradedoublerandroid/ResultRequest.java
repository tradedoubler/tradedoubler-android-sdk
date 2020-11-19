package com.tradedouble.tradedoublerandroid;

public interface ResultRequest {
    void onFailure(int code);
    void onResponseSuccess(int code);
}
