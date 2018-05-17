package com.example.user.banhangonline.interactor.prefer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.banhangonline.R;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyEmail;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyTextGia;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyUserID;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyIDBuySell;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyIsLogin;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyNamelID;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyPhoneNumberlID;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyTextAddress;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyTextHeader;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyTextMota;


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

    public static void setUserID(Context context, String userID) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyUserID, userID);
        editor.apply();
    }

    public static String getUserID(Context context) {
        String emailID = PreferenceManager.getDefaultSharedPreferences(context).getString(keyUserID, null);
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
        String emailID = PreferenceManager.getDefaultSharedPreferences(context).getString(keyPhoneNumberlID, context.getString(R.string.dont_phone_number));
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

    public static void setTextGia(Context context, String gia) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyTextGia, gia);
        editor.apply();
    }

    public static String getTextGia(Context context) {
        String header = PreferenceManager.getDefaultSharedPreferences(context).getString(keyTextGia, "");
        return header;
    }

    public static void setMyAddress(Context context, String address) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyTextAddress, address);
        editor.apply();
    }

    public static String getMyAddress(Context context) {
        String header = PreferenceManager.getDefaultSharedPreferences(context).getString(keyTextAddress, context.getString(R.string.dont_address));
        return header;
    }

}
