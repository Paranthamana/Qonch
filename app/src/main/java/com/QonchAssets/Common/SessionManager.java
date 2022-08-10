package com.QonchAssets.Common;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

public class SessionManager {

    private static final SessionManager ourInstance = new SessionManager();
    private static final String APP_PREFERENCE_NAME = "Assets";
    private static final String APP_PREFS_SETTINGS = "Assets_Settings";
    private static final String USER_ID = "pref_userId";
    private static final String USER_FIRSTNAME = "pref_firstName";
    private static final String ACCESS_TOKEN = "pref_access_token";
    private static final String LANGUAGE_KEY = "pref_language_key";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private static SharedPreferences pref;

    public static SessionManager getInstance() {
        return ourInstance;
    }

    public SessionManager() {
        pref = MyApplication.context.getSharedPreferences(APP_PREFERENCE_NAME, MODE_PRIVATE);
    }

    public void userDetails(String first_name, String accessToken, String userId) {
        pref.edit().putString(USER_FIRSTNAME, first_name).apply();
        pref.edit().putString(ACCESS_TOKEN, accessToken).apply();
        pref.edit().putString(USER_ID, userId).apply();
    }

    public void setAppLanguageCode(String appLanguageCode) {
        pref.edit().putString(LANGUAGE_KEY, appLanguageCode).apply();
    }

    public String getAppLanguageCode() {
        return pref.getString(LANGUAGE_KEY, "en");
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }


    public static void setData(String key, String value) {
        pref.edit().putString(key, value).apply();
    }

    public static void setSelectedData(int key, String value) {
        pref.edit().putInt(value, key).apply();
    }

    public static String getData(String key, String defaultVal) {
        return pref.getString(key, defaultVal);
    }

    public static boolean getSelectedData(String defaultVal,boolean key) {
        //return pref.getBoolean(defaultVal,key);
        return pref.getBoolean(defaultVal,key);
    }


    public void Logout() {
        SharedPreferences prefs = MyApplication.context.
                getSharedPreferences(APP_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public static class AppProperties {
        private static AppProperties ourInstance = new AppProperties();

        public static AppProperties getInstance() {
            return ourInstance;
        }


        public void setDevelopmentURL(String mURL) {
            SharedPreferences pref = MyApplication.context.getSharedPreferences(APP_PREFS_SETTINGS, MODE_PRIVATE);
            pref.edit().putString("DevelopmentURL", mURL).apply();
        }

        public String getDevelopmentURL() {
            SharedPreferences pref = MyApplication.context.getSharedPreferences(APP_PREFS_SETTINGS, MODE_PRIVATE);
            return pref.getString("DevelopmentURL", "http://192.168.1.6:56/");
        }

    }

    public void SavedIntoSession(String key, boolean value) {
        SharedPreferences sp = MyApplication.context.getSharedPreferences("Assets", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean Update(String key) {
        SharedPreferences sp = MyApplication.context.getSharedPreferences("Assets", MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public void SavedIntoEditTextSession(String key ,String value) {
        SharedPreferences sp = MyApplication.context.getSharedPreferences("Assets", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public boolean UpdateEdit(String key) {
        SharedPreferences sp = MyApplication.context.getSharedPreferences("Assets", MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
}
