package com.QonchAssets.Api;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.QonchAssets.Activity.HomeActivity;
import com.QonchAssets.ApiResponse.ForgotPasswordApiResponse;
import com.QonchAssets.ApiResponse.LoginApiResponse;
import com.QonchAssets.ApiResponse.SyncAssetResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.Constants;
import com.QonchAssets.Common.CustomProgressDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonApiCalls {
    private static CommonApiCalls ourInstance;

    public static CommonApiCalls getInstance() {
        ourInstance = new CommonApiCalls();
        return ourInstance;
    }

    public void login(Context context, final String body, final APICommonCallback.Listener listener) {
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (body));
        Call<LoginApiResponse> call = apiInterface.login(mRequestBody);
        call.enqueue(new Callback<LoginApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginApiResponse> call, @NonNull Response<LoginApiResponse> response) {
                if (response.isSuccessful()) {

                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(Constants.checkUserNameAndPassword);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginApiResponse> call, @NonNull Throwable t) {
                CustomProgressDialog.getInstance().dismiss();
                t.printStackTrace();
            }
        });
    }


    public void ForgotPassword (Context context, final String body , final APICommonCallback.Listener listener){
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (body));
        Call<ForgotPasswordApiResponse> call = apiInterface.forgotPassword(mRequestBody);

        call.enqueue(new Callback<ForgotPasswordApiResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordApiResponse> call, Response<ForgotPasswordApiResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(Constants.checkUserName);
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordApiResponse> call, Throwable t) {
                CustomProgressDialog.getInstance().dismiss();
                t.printStackTrace();
                FancyToast.makeText(context, "Link sent to your mail successfully...",
                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            }
        });
    }

    public void SyncAssets(Context context, final String body, final APICommonCallback.Listener listener) {
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (body));
        Call<SyncAssetResponse> call = apiInterface.SyncWorkOrders(mRequestBody);
        call.enqueue(new Callback<SyncAssetResponse>() {
            @Override
            public void onResponse(@NonNull Call<SyncAssetResponse> call, @NonNull Response<SyncAssetResponse> response) {
                if (response.isSuccessful()) {

                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure("No data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SyncAssetResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(context,
                        Constants.DataSavedSuccessfully,
                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                Handler handler = new Handler();
                long SPLASH_TIME_OUT = 2000;
                handler.postDelayed(() -> {
                    CommonFunctions.getInstance().newIntent(context,
                            HomeActivity.class, Bundle.EMPTY, true);
                    CustomProgressDialog.getInstance().dismiss();
                }, SPLASH_TIME_OUT);
            }
        });
    }

}