package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ChangeIconActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private static final int PICKER_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    ImageView mIcon;
    ImageView mBorder;
    ImageButton mEdit;
    Button mCancel;
    Button mConfirm;
    ImageButton mHelp;

    Uri imageLocation;
    Boolean imageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_icon);

        mIcon = findViewById(R.id.ivChangeIconIcon);
        mBorder = findViewById(R.id.ivChangeIconBorder);
        mEdit = findViewById(R.id.ibChangeIconEdit);
        mCancel = findViewById(R.id.btChangeIconCancel);
        mConfirm = findViewById(R.id.btChangeIconConfirm);
        mHelp = findViewById(R.id.ibChangeIconHelp);

        FirebaseStorage.getInstance().getReference().child("profileicons/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar).fallback(R.drawable.avatar).into(mIcon);
            }
        });
        // Select image media
        mBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        selectImage();
                    }
                } else {
                    selectImage();
                }
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        selectImage();
                    }
                } else {
                    selectImage();
                }
            }
        });

        Context pageContext = this;
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pageContext, ProfileActivity.class);
                intent.putExtra("OPEN_SETTINGS", true);
                startActivity(intent);
            }
        });
        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final com.example.custodian.AlertDialog helpDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Select an icon from your local device storage. Permissions must be enabled.", "information");
                helpDialog.startLoadingAnimation();
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(pageContext));
                loadingDialog.startLoadingAnimation();
                FirebaseStorage.getInstance().getReference().child("profileicons/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).putFile(imageLocation).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        FirebaseStorage.getInstance().getReference().child("profileicons/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(pageContext, ProfileActivity.class);
                                        intent.putExtra("OPEN_SETTINGS", true);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    // Pick image
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICKER_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    final com.example.custodian.AlertDialog permissionDeniedDialog = new com.example.custodian.AlertDialog(new Dialog(this), "Permission denied", "warning");
                    permissionDeniedDialog.startLoadingAnimation();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICKER_CODE) {
            imageLocation = data.getData();
            mIcon.setImageURI(imageLocation);
            imageSelected = true;
        }
    }
}