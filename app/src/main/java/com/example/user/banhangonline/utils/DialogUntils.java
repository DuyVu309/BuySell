package com.example.user.banhangonline.utils;

import android.content.Context;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.widget.dialog.DialogChangeAccount;
import com.example.user.banhangonline.widget.dialog.DialogChangeAddress;
import com.example.user.banhangonline.widget.dialog.DialogChangePhoneNmber;
import com.example.user.banhangonline.widget.dialog.DialogMethodPay;
import com.example.user.banhangonline.widget.dialog.DialogOk;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;
import com.example.user.banhangonline.widget.dialog.DialogProgress;

public class DialogUntils {

    public static DialogProgress showProgressDialog(Context context) {
        DialogProgress dialogProgress = new DialogProgress(context);
        dialogProgress.show();
        return dialogProgress;
    }

    public static DialogProgress showProgressDialogMessage(Context context, String message) {
        DialogProgress dialogProgress = new DialogProgress(context, message);
        dialogProgress.show();
        return dialogProgress;
    }

    public static DialogOk showOkDialog(Context context, String title, String message, DialogOk.OkDialogListener listener) {
        DialogOk dialogOk = new DialogOk(context, title, message, listener);
        dialogOk.show();
        return dialogOk;
    }

    public static void showConfirmDialogDefault(Context context, String title, String message, DialogPositiveNegative.IPositiveNegativeDialogListener listener) {
        DialogPositiveNegative dialog = new DialogPositiveNegative(context, title, message, context.getString(R.string.xac_nhan), context.getString(R.string.huy));
        dialog.setOnIPositiveNegativeDialogListener(listener);
        dialog.show();
    }

    public static void showConfirmDialog(Context context, String title, String message, String textPositive, String textNegative, DialogPositiveNegative.IPositiveNegativeDialogListener listener) {
        DialogPositiveNegative dialog = new DialogPositiveNegative(context, title, message, textPositive, textNegative);
        dialog.setOnIPositiveNegativeDialogListener(listener);
        dialog.show();
    }

    public static void showChangeInfoDialog(Context context, String name, String phone, String address, DialogChangeAccount.IOnClickDoneChange lisenter) {
        DialogChangeAccount dialogChangeAccount = new DialogChangeAccount(context, name, phone, address);
        dialogChangeAccount.setmLisenter(lisenter);
        dialogChangeAccount.show();
    }

    public static void showMethodPay(Context context, DialogMethodPay.IOnClickChooseMethodPay listener) {
        DialogMethodPay dialogMethodPay = new DialogMethodPay(context, listener);
        dialogMethodPay.show();
    }

    public static void showChangeBuyAddress(Context context, String address, DialogChangeAddress.IOnDoneChangeAddress listener) {
        DialogChangeAddress dialogChangeAddress = new DialogChangeAddress(context, address, listener);
        dialogChangeAddress.show();
    }

    public static void showChangeBuyPhoneNumber(Context context, String phoneNumber, DialogChangePhoneNmber.IOnDoneChangeBuyPhoneNumber listener) {
        DialogChangePhoneNmber dialogChangePhoneNmber = new DialogChangePhoneNmber(context, phoneNumber, listener);
        dialogChangePhoneNmber.show();
    }

}
