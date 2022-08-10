package com.QonchAssets.Api;

import com.QonchAssets.ApiResponse.ForgotPasswordApiResponse;
import com.QonchAssets.ApiResponse.LoginApiResponse;
import com.QonchAssets.ApiResponse.SyncAssetResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(Urls.Login)
    Call<LoginApiResponse> login(@Body RequestBody body);

    @POST(Urls.ForgotPassword)
    Call<ForgotPasswordApiResponse> forgotPassword(@Body RequestBody body);

    @POST(Urls.pushAssets)
    Call<SyncAssetResponse> SyncWorkOrders(@Body RequestBody body);

    /*@GET(Urls.Location)
    Call<LocationApiResponse.Master> LocationDetails();*/

}