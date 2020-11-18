package com.tradedouble.tradedoublerandroid;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class NetClient {

    public NetClient() {
    }

    private OkHttpClient initOkHttpClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
        return okHttpClient;
    }



 public String callResponse(String url, OkHttpClient client) throws IOException {
     Request request = new Request.Builder()
             .url(url)
             .get()
             .build();
     Response response = client.newCall(request).execute();

     return Objects.requireNonNull(request.body()).toString();

 }


}
