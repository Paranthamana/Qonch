package com.QonchAssets.Api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfiguration {

    public String ProdOrDev = "?env";
    private static ApiConfiguration ourInstance = new ApiConfiguration();
    Retrofit mRetrofit;

    public static ApiConfiguration getInstance() {
        if (ourInstance == null) {
            synchronized (ApiConfiguration.class) {
                if (ourInstance == null)
                    ourInstance = new ApiConfiguration();
            }
        }
        ourInstance.config();
        return ourInstance;
    }

    private ApiConfiguration() {
    }

    private void config() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        String BASE_URL = Urls.COMMON;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Urls.COMMON)
               // .client(client)
                .client(getRequestHeader(SessionManager.getInstance().getAccessToken()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private OkHttpClient getRequestHeader(final String token) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okHttpClientBuild = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                //.addHeader("Accept-Language", SessionManager.getInstance().getAppLanguageCode())
                                .build();
                        if (!CheckInternetConnection()) {
                            CustomProgressDialog.getInstance().dismiss();
                            throw new NoConnectivityException();
                        } else {
                            okhttp3.Response response = chain.proceed(newRequest);
                            return response;
                        }
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        if (ProdOrDev != "") {
            okHttpClientBuild.interceptors().add(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();

                    if (!CheckInternetConnection()) {
                        CustomProgressDialog.getInstance().dismiss();
                        throw new NoConnectivityException();
                    } else {
                        okhttp3.Response response = chain.proceed(request);
                        return response;
                    }
                }
            });
        }
        OkHttpClient okHttpClient = okHttpClientBuild.build();
        return okHttpClient;
    }

    public Retrofit getApiBuilder() {
        return mRetrofit;
    }

    public class NoConnectivityException extends IOException {
        @Override
        public String getMessage() {
            return "";
        }
    }

    public boolean CheckInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
