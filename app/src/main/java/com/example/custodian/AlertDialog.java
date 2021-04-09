package com.example.custodian;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

public class AlertDialog {

    Dialog dialog;
    String message;
    String type;

    AlertDialog(Dialog dialogValue, String messageValue, String typeValue) {
        dialog = dialogValue;
        message = messageValue;
        type = typeValue;
    }

    void startLoadingAnimation() {
        dialog.setContentView(R.layout.alert_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView mMessage = dialog.findViewById(R.id.tvStandardAlertMessage);
        ImageView mIcon = dialog.findViewById(R.id.ivStandardAlertIcon);
        ImageView mInteract = dialog.findViewById(R.id.ivStandardAlertInteract);
        switch (type) {
            case "information":
                mIcon.setImageResource(R.drawable.information_icon);
                break;
            case "warning":
                mIcon.setImageResource(R.drawable.warning_icon);
                break;
        }
        mMessage.setText(message);
        dialog.show();
        mInteract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }

    void dismissDialog() {
        dialog.dismiss();
    }
}
