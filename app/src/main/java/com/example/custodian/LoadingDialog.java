package com.example.custodian;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog {

    private Dialog dialog;

    LoadingDialog(Dialog dialogValue) {
        dialog = dialogValue;
    }

    void startLoadingAnimation() {
        dialog.setContentView(R.layout.loading_view);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }
}
