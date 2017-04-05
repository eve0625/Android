package com.cindy.androidstudy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

public class SharedPrefsUtils {

    private static final String TAG = "SharedPrefsUtils";

    private static final String PREF_NAME_FCM = "fcm";
    private static final String RREF_KEY_FCM_TOKEN = "fcm_token";

    public static void setFcmToken(Context context, String token) {
        Log.d(TAG, "setFcmToken : " + token);
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_NAME_FCM, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(RREF_KEY_FCM_TOKEN, token).commit();
    }

    @Nullable
    public static String getFcmToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_NAME_FCM, Context.MODE_PRIVATE);
        return sharedPrefs.getString(RREF_KEY_FCM_TOKEN, null);
    }
}
