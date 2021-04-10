package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangeUsernameActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    EditText mUsername;
    EditText mConfirmUsername;
    Button mCancel;
    Button mConfirm;
    ImageButton mHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        // Assigning of variable values
        mCancel = findViewById(R.id.btChangeUsernameCancel);
        mConfirm = findViewById(R.id.btChangeUsernameConfirm);
        mUsername = findViewById(R.id.etChangeUsernameUsername);
        mConfirmUsername = findViewById(R.id.etChangeUsernameConfirmUsername);
        mHelp = findViewById(R.id.ibChangeUsernameHelp);

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mUsername.setText(documentSnapshot.getString("username"));
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
                final com.example.custodian.AlertDialog helpDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Usernames must be a minimum of 8 characters with no spaces and at least one upper & lower case letter.", "information");
                helpDialog.startLoadingAnimation();
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsername.getText().toString().isEmpty() || mConfirmUsername.getText().toString().isEmpty()) {
                    final com.example.custodian.AlertDialog incompleteDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "You haven't filled out the necessary fields yet.", "warning");
                    incompleteDialog.startLoadingAnimation();
                } else if (mUsername.getText().toString().length() < 8 || mUsername.getText().toString().contains(" ") || mUsername.getText().toString().toLowerCase().equals(mUsername.getText().toString()) || mUsername.getText().toString().toUpperCase().equals(mUsername.getText().toString())) {
                    final com.example.custodian.AlertDialog invalidUsernameDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Invalid username, it has to be a minimum of 8 characters with no spaces and at least one upper & lower case letter.", "warning");
                    invalidUsernameDialog.startLoadingAnimation();
                } else if (!mUsername.getText().toString().equals(mConfirmUsername.getText().toString())) {
                    final com.example.custodian.AlertDialog mismatchDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Usernames are not the same!", "warning");
                    mismatchDialog.startLoadingAnimation();
                } else {
                    final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(pageContext));
                    loadingDialog.startLoadingAnimation();
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", mUsername.getText().toString());
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(pageContext, ProfileActivity.class);
                            intent.putExtra("OPEN_SETTINGS", true);
                            startActivity(intent);
                        }
                    });
                }

            }
        });
    }
}