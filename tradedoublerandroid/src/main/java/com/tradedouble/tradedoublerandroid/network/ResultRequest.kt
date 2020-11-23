package com.tradedouble.tradedoublerandroid.network

interface ResultRequest {
    fun onFailure(code: Int)
    fun onResponseSuccess(code: Int, responseBody:String?)
}