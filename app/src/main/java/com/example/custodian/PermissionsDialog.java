package com.example.custodian;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class PermissionsDialog {

    Context context;
    Activity activity;
    Dialog dialog;

    PermissionsDialog(Context contextValue, Activity activityValue, Dialog dialogValue) {
        context = contextValue;
        activity = activityValue;
        dialog = dialogValue;
    }

    void startLoadingAnimation() {
        dialog.setContentView(R.layout.permission_checker_view);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button mLocation = dialog.findViewById(R.id.btPermissionsLocationServices);
        Button mReadExternalStorage = dialog.findViewById(R.id.btPermissionsReadExternalStorage);
        ImageButton mDismiss = dialog.findViewById(R.id.ibPermissionsClose);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocation.setAlpha((float) 0.15);
            mLocation.setClickable(false);
        } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mReadExternalStorage.setAlpha((float) 0.15);
            mReadExternalStorage.setClickable(false);
        }
        mLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                activity.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 4);
            }
        });
        mReadExternalStorage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                activity.requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
            }
        });
        mDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }
}
