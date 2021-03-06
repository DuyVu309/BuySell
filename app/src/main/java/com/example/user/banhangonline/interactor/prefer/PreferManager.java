package com.example.user.banhangonline.interactor.prefer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.banhangonline.R;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyEmail;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyShowDistanceCart;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyShowDistanceSell;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyUserID;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyIDBuySell;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyIsLogin;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyNamelID;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyPhoneNumberlID;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyTextAddress;


public class PreferManager {

    //start [account]
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

    public static void setMyAddress(Context context, String address) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(keyTextAddress, address);
        editor.apply();
    }

    public static String getMyAddress(Context context) {
        String header = PreferenceManager.getDefaultSharedPreferences(context).getString(keyTextAddress, context.getString(R.string.dont_address));
        return header;
    }
    // /end [account]

    //start [setting]

    public static void setShowDistanceSell(Context context, boolean isShow) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(keyShowDistanceSell, isShow);
        editor.apply();
    }

    public static boolean getIsShowDistanceSell(Context context) {
        boolean isShow = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(keyShowDistanceSell, false);
        return isShow;
    }

    public static void setShowDistanceCart(Context context, boolean isShow) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(keyShowDistanceCart, isShow);
        editor.apply();
    }

    public static boolean getIsShowDistanceCart(Context context) {
        boolean isShow = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(keyShowDistanceCart, false);
        return isShow;
    }
    //end [setting]
}
