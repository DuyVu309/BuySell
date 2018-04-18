package com.example.user.banhangonline.untils;

import android.app.Dialog;
import android.content.Context;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.widget.dialog.DialogOk;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;
import com.example.user.banhangonline.widget.dialog.DialogProgress;

public class DialogUntils {

    public static DialogProgress showProgressDialog(Context context) {
        DialogProgress dialogProgress = new DialogProgress(context);
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

}
