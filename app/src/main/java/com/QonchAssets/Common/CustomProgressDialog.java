package com.QonchAssets.Common;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.LinearLayout;

import com.zebra.qonchAssets.R;

public class CustomProgressDialog {
    private static final CustomProgressDialog ourInstance = new CustomProgressDialog();
    Dialog dialog;

    public static CustomProgressDialog getInstance() {
        return ourInstance;
    }

    private CustomProgressDialog() {

    }

    public void show(Context context) {

        try {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            LinearLayout mkLoader = dialog.findViewById(R.id.llView);
            mkLoader.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}
