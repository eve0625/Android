package com.cindy.androidstudy.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

public class CustomDialog extends AppCompatDialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
