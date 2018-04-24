package com.example.user.banhangonline.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.screen.login.LoginActivity;
import com.example.user.banhangonline.untils.DialogUntils;
import com.example.user.banhangonline.untils.NetworkUtils;
import com.example.user.banhangonline.widget.dialog.DialogOk;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;
import com.example.user.banhangonline.widget.dialog.DialogProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BaseActivity extends AppCompatActivity {
    protected FirebaseAuth mAuth;
    protected DatabaseReference mDataBase;
    protected FirebaseStorage mStorage;
    protected StorageReference mStorageReferrence;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isTransparentStatusBar()) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().setStatusBarColor(getResources().getColor(R.color.blue_window));
            }
        }

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageReferrence = mStorage.getReferenceFromUrl("gs://banhangonline-7058d.appspot.com");
    }

    public boolean isTransparentStatusBar() {
        return true;
    }

    private DialogProgress mProgressDialog;

    //start progress dialog
    protected void showDialog() {
        if (!isFinishing()) {
            dismissDialog();
            mProgressDialog = DialogUntils.showProgressDialog(this);
        }
    }

    protected void dismissDialog() {
        if (!isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void showDialogMessage(String message) {
        if (!isFinishing()) {
            dismissDialog();
            mProgressDialog = DialogUntils.showProgressDialogMessage(this, message);
        }
    }

    //end progress dialog

    //dialog with one button
    protected void showOkDialog(String title, String message, DialogOk.OkDialogListener listener) {
        if (!isFinishing()) {
            DialogUntils.showOkDialog(this, title, message, listener);
        }
    }

    protected void showErrorDialog(String message) {
        if (!isFinishing()) {
            showOkDialog(getResources().getString(R.string.app_name), message, new DialogOk.OkDialogListener() {
                @Override
                public void onOkDialogAnswerOk(DialogOk dialog) {
                    dialog.dismiss();
                }
            });
        }
    }

    // dialog with positive and negative
    protected void showConfirmDialog(String message, DialogPositiveNegative.IPositiveNegativeDialogListener lisenter) {
        if (!isFinishing()) {
            DialogUntils.showConfirmDialogDefault(this, getResources().getString(R.string.app_name), message, lisenter);
        }
    }

    protected void showConfirmDialog(String tittle, String message, String textPositive, String textNegaTtive, DialogPositiveNegative.IPositiveNegativeDialogListener lisenter) {
        if (!isFinishing()) {
            DialogUntils.showConfirmDialog(this, tittle, message, textPositive, textNegaTtive, lisenter);
        }
    }

    // Keyboard utils
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager ipm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            ipm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    hideKeyboard();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void logoutUser() {
        if (NetworkUtils.isConnected(this)) {
            mAuth.signOut();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            showNoInternet();
        }

    }

    protected void runAnimationStartActivity(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        animation.reset();
        if (animation != null && view != null && view.isShown()) {
            view.startAnimation(animation);
        }
    }

    protected void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                 .show();

    }

    protected void showNoInternet() {
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                 .show();

    }
}
