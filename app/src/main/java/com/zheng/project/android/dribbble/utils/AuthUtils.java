package com.zheng.project.android.dribbble.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthUtils {
    private static String PREF_NAME = "auth";

    public static String loadAccessToken(Context context, String key) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static void saveAccessToken(Context context, String key, String value) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        sp.edit().putString(key, value).apply();
    }
}
