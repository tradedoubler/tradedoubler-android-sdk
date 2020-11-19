package com.tradedouble.tradedoublerandroid.network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class NetClient {

    public final OkHttpClient client;
    private static NetClient netClient;

    public NetClient() {
        client = initOkHttpClient();
    }

    private OkHttpClient initOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
        return okHttpClient;
    }

    public static NetClient getNetClient() {
        if (netClient == null) {
            netClient = new NetClient();
        }
        return netClient;
    }


    public void callResponse(String url, ResultRequest resultRequest) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = getNetClient().initOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                resultRequest.onFailure(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    if (response.code() >= 200 && response.code() <=300) {
                        resultRequest.onResponseSuccess(response.code() );
                    }else{
                        resultRequest.onFailure(response.code());
                    }
                }
            }
        });

    }


}
