package com.QonchAssets.Common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.zebra.qonchAssets.R;

public class CommonFunctions {


    //public static String fontPath = "fonts/font_regular.ttf";

    private static final CommonFunctions ourInstance = new CommonFunctions();

    public static CommonFunctions getInstance() {
        return ourInstance;
    }

    public CommonFunctions() {
    }

    public void newIntent(Context context, Class destinationClass, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(context, destinationClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (isFinish) {
            ((Activity) context).finish();
        }
    }

    public boolean CheckInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void HideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

}
