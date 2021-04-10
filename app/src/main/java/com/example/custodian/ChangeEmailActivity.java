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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangeEmailActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    EditText mEmail;
    EditText mConfirmEmail;
    Button mCancel;
    Button mConfirm;
    ImageButton mHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        // Assigning of variable values
        mCancel = findViewById(R.id.btChangeEmailCancel);
        mConfirm = findViewById(R.id.btChangeEmailConfirm);
        mEmail = findViewById(R.id.etChangeEmailEmail);
        mConfirmEmail = findViewById(R.id.etChangeEmailEmailConfirm);
        mHelp = findViewById(R.id.ibChangeEmailHelp);

        mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

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
                final com.example.custodian.AlertDialog helpDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Emails must contain the '@' symbol.", "information");
                helpDialog.startLoadingAnimation();
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().isEmpty() || mConfirmEmail.getText().toString().isEmpty()) {
                    final com.example.custodian.AlertDialog incompleteDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "You haven't filled out the necessary fields yet.", "warning");
                    incompleteDialog.startLoadingAnimation();
                } else if (!mEmail.getText().toString().contains("@")) {
                    final com.example.custodian.AlertDialog invalidEmailDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Invalid email, it has to contain the '@' symbol.", "warning");
                    invalidEmailDialog.startLoadingAnimation();
                } else if (!mEmail.getText().toString().equals(mConfirmEmail.getText().toString())) {
                    final com.example.custodian.AlertDialog mismatchDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Emails are not the same!", "warning");
                    mismatchDialog.startLoadingAnimation();
                } else {
                    final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(pageContext));
                    loadingDialog.startLoadingAnimation();
                    FirebaseAuth.getInstance().getCurrentUser().updateEmail(mEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            final com.example.custodian.AlertDialog invalidEmailDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Something went wrong while processing your email change.", "warning");
                            invalidEmailDialog.startLoadingAnimation();
                        }
                    });
                }
            }
        });
    }
}