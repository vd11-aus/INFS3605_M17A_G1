package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    EditText mPassword;
    EditText mConfirmPassword;
    Button mCancel;
    Button mConfirm;
    ImageButton mHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Assigning of variable values
        mCancel = findViewById(R.id.btChangePasswordCancel);
        mConfirm = findViewById(R.id.btChangePasswordConfirm);
        mPassword = findViewById(R.id.etChangePasswordPassword);
        mConfirmPassword = findViewById(R.id.etChangePasswordPasswordConfirm);
        mHelp = findViewById(R.id.ibChangePasswordHelp);

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
                final com.example.custodian.AlertDialog helpDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Passwords must be a minimum of 8 characters with no spaces and at least one upper & lower case letter.", "information");
                helpDialog.startLoadingAnimation();
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText().toString().isEmpty() || mConfirmPassword.getText().toString().isEmpty()) {
                    final com.example.custodian.AlertDialog incompleteDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "You haven't filled out the necessary fields yet.", "warning");
                    incompleteDialog.startLoadingAnimation();
                } else if (mPassword.length() < 8 || mPassword.getText().toString().contains(" ") || mPassword.getText().toString().toLowerCase().equals(mPassword.getText().toString()) || mPassword.getText().toString().toUpperCase().equals(mPassword.getText().toString())) {
                    final com.example.custodian.AlertDialog invalidPasswordDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Invalid password, it has to be a minimum of 8 characters with no spaces and at least one upper & lower case letter.", "warning");
                    invalidPasswordDialog.startLoadingAnimation();
                } else if (!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                    final com.example.custodian.AlertDialog mismatchDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Passwords are not the same!", "warning");
                    mismatchDialog.startLoadingAnimation();
                } else {
                    final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(pageContext));
                    loadingDialog.startLoadingAnimation();
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(mPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(pageContext, ProfileActivity.class);
                            intent.putExtra("OPEN_SETTINGS", true);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismissDialog();
                            final com.example.custodian.AlertDialog invalidEmailDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Something went wrong while processing your password change.", "warning");
                            invalidEmailDialog.startLoadingAnimation();
                        }
                    });
                }
            }
        });
    }
}