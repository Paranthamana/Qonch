package com.QonchAssets.Common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private Locale locale = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();


        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Constants.DBName.toLowerCase() + ".realm")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.getInstance(realmConfiguration);
        Configuration config = getBaseContext().getResources().getConfiguration();
        locale = new Locale("en");
        Locale.setDefault(locale);
        setSystemLocale(config, locale);
        updateConfiguration(config);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            setSystemLocale(newConfig, locale);
            Locale.setDefault(locale);
            updateConfiguration(newConfig);
        }
    }

    private static void setSystemLocale(Configuration config, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void updateConfiguration(Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getBaseContext().createConfigurationContext(config);
        } else {
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
