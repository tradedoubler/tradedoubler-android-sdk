package com.tradedouble.tradedoublerandroid.network

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.HttpRetryException
import java.util.concurrent.TimeUnit

class NetClient {
    val client: OkHttpClient
    private fun initOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Throws(IOException::class)
    fun callResponse(url: String, resultRequest: ResultRequest) {

        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call =
            netClient
                ?.initOkHttpClient()?.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                resultRequest.onFailure( call.execute().code, e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                if (response.isSuccessful) {
                    resultRequest.onResponseSuccess(
                        response.code,
                        responseBody = response.body.toString()
                    )
                }
            }
        })
    }

    companion object {
        var netClient: NetClient? = null
            get() {
                if (field == null) {
                    field =
                        NetClient()
                }
                return field
            }
            private set
    }

    init {
        client = initOkHttpClient()
    }
}