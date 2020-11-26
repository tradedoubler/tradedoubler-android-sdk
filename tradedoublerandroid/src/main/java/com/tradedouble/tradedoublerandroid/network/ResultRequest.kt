package com.tradedouble.tradedoublerandroid.network

interface ResultRequest {
    fun onFailure(code: Int, errorMessage: String?)
    fun onResponseSuccess(code: Int, responseBody: String?)
}