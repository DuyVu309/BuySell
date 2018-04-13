package com.example.user.banhangonline.untils;

import android.app.Dialog;
import android.content.Context;

import com.example.user.banhangonline.widget.dialog.DialogOk;
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
}
