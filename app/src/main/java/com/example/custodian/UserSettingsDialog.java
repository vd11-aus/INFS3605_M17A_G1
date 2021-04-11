package com.example.custodian;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class UserSettingsDialog {

    Context context;
    Dialog dialog;

    UserSettingsDialog(Context contextValue, Dialog dialogValue) {
        context = contextValue;
        dialog = dialogValue;
    }

    void startLoadingAnimation() {
        dialog.setContentView(R.layout.user_settings_view);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView mIcon = dialog.findViewById(R.id.ivUserSettingsIcon);
        ImageView mIconBorder = dialog.findViewById(R.id.ivUserSettingsBorder);
        ImageButton mIconEdit = dialog.findViewById(R.id.ibUserSettingsIconEdit);
        ImageButton mEmailEdit = dialog.findViewById(R.id.ibUserSettingsEmailEdit);
        ImageButton mGenderEdit = dialog.findViewById(R.id.ibUserSettingsGenderEdit);
        ImageButton mOriginEdit = dialog.findViewById(R.id.ibUserSettingsOriginEdit);
        ImageButton mPasswordEdit = dialog.findViewById(R.id.ibUserSettingsPasswordEdit);
        ImageButton mUsernameEdit = dialog.findViewById(R.id.ibUserSettingsUsernameEdit);
        ImageButton mCloseButton = dialog.findViewById(R.id.ibUserSettingsClose);
        TextView mUsername = dialog.findViewById(R.id.tvUserSettingsUsername);
        TextView mEmail = dialog.findViewById(R.id.tvUserSettingsEmail);
        TextView mGender = dialog.findViewById(R.id.tvUserSettingsGender);
        TextView mOrigin = dialog.findViewById(R.id.tvUserSettingsOrigin);
        Button mLogOut = dialog.findViewById(R.id.btUserSettingsLogOut);
        FirebaseStorage.getInstance().getReference().child("profileicons/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar).fallback(R.drawable.avatar).into(mIcon);
            }
        });
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mUsername.setText("Username: " + documentSnapshot.getString("username"));
                mEmail.setText("Email: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                mGender.setText("Gender: " + documentSnapshot.getString("gender"));
                mOrigin.setText("Origin: " + documentSnapshot.getString("origin"));
            }
        });
        mIconBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeIconActivity.class);
                context.startActivity(intent);
            }
        });
        mIconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeIconActivity.class);
                context.startActivity(intent);
            }
        });
        mUsernameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeUsernameActivity.class);
                context.startActivity(intent);
            }
        });
        mEmailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeEmailActivity.class);
                context.startActivity(intent);
            }
        });
        mPasswordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                context.startActivity(intent);
            }
        });
        mGenderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeGenderActivity.class);
                context.startActivity(intent);
            }
        });
        mOriginEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeOriginActivity.class);
                context.startActivity(intent);
            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(context));
                loadingDialog.startLoadingAnimation();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }
}
