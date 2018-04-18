package com.example.user.banhangonline.interactor.prefer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.example.user.banhangonline.untils.KeyUntils.keyHasAccount;
import static com.example.user.banhangonline.untils.KeyUntils.keyIsLogin;

public class PreferManager {
    static SharedPreferences preferences;

    public static boolean getIsLogin(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isLogin = preferences.getBoolean(keyIsLogin, false);
        return isLogin;
    }

    public static void setIsLogin(Context context, boolean isLogin) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(keyIsLogin, isLogin).commit();
        editor.apply();
    }

    public static boolean getHasAccount(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean hasAccount = preferences.getBoolean(keyIsLogin, false);
        return hasAccount;
    }

    public static void setHasAccount(Context context, boolean hasAccounnt) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(keyHasAccount, hasAccounnt).commit();
        editor.apply();
    }
}
