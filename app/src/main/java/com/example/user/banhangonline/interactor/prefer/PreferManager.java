package com.example.user.banhangonline.interactor.prefer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyEmail;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyEmailID;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyIDBuySell;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyIsLogin;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyNamelID;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyPhoneNumberlID;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyTextHeader;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyTextMota;


public class PreferManager {

    public static boolean getIsLogin(Context context) {
        boolean isLogin = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(keyIsLogin, false);
        return isLogin;
    }

    public static void setIsLogin(Context context, boolean isLogin) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(keyIsLogin, isLogin).commit();
        editor.apply();
    }

    public static String getIDBuySell(Context context) {
        String isLogin = PreferenceManager.getDefaultSharedPreferences(context).getString(keyIDBuySell, null);
        return isLogin;
    }

    public static void setIDBuySell(Context context, String idBuySell) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyIDBuySell, idBuySell).commit();
        editor.apply();
    }

    public static void setEmail(Context context, String emailID) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyEmail, emailID);
        editor.apply();
    }

    public static String getEmail(Context context) {
        String emailID = PreferenceManager.getDefaultSharedPreferences(context).getString(keyEmail, null);
        return emailID;
    }

    public static void setEmailID(Context context, String emailID) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyEmailID, emailID);
        editor.apply();
    }

    public static String getEmailID(Context context) {
        String emailID = PreferenceManager.getDefaultSharedPreferences(context).getString(keyEmailID, null);
        return emailID;
    }

    public static String getNameAccount(Context context) {
        String emailID = PreferenceManager.getDefaultSharedPreferences(context).getString(keyNamelID, null);
        return emailID;
    }

    public static void setNameAccount(Context context, String name) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyNamelID, name);
        editor.apply();
    }

    public static String getPhoneNumber(Context context) {
        String emailID = PreferenceManager.getDefaultSharedPreferences(context).getString(keyPhoneNumberlID, "Chưa có số điện thoại");
        return emailID;
    }

    public static void setPhoneNumber(Context context, String phone) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyPhoneNumberlID, phone);
        editor.apply();
    }

    public static void setTextHeader(Context context, String header) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyTextHeader, header);
        editor.apply();
    }

    public static String getTextHeader(Context context) {
        String header = PreferenceManager.getDefaultSharedPreferences(context).getString(keyTextHeader, "");
        return header;
    }

    public static void setTextMota(Context context, String mota) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyTextMota, mota);
        editor.apply();
    }

    public static String getTextMota(Context context) {
        String header = PreferenceManager.getDefaultSharedPreferences(context).getString(keyTextMota, "");
        return header;
    }

}
