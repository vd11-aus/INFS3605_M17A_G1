package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangeOriginActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    RadioGroup mGroup;
    Button mCancel;
    Button mConfirm;
    ImageButton mHelp;

    String origin;
    Context pageContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_origin);

        // Assigning of variable values
        mGroup = findViewById(R.id.rgChangeOriginGroup);
        mCancel = findViewById(R.id.btChangeOriginCancel);
        mConfirm = findViewById(R.id.btChangeOriginConfirm);
        mHelp = findViewById(R.id.ibChangeOriginHelp);

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                switch (documentSnapshot.getString("origin")) {
                    case "True":
                        mGroup.check(R.id.rbChangeOriginTrue);
                        break;
                    case "False":
                        mGroup.check(R.id.rbChangeOriginFalse);
                        break;
                    case "Did Not Disclose":
                        mGroup.check(R.id.rbChangeOriginDoNotDisclose);
                        break;
                }
            }
        });

        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final com.example.custodian.AlertDialog helpDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Select an origin from the radio group below.", "information");
                helpDialog.startLoadingAnimation();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pageContext, ProfileActivity.class);
                intent.putExtra("OPEN_SETTINGS", true);
                startActivity(intent);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mGroup.getCheckedRadioButtonId()) {
                    case R.id.rbChangeOriginTrue:
                        origin = "True";
                        submitData(origin);
                        break;
                    case R.id.rbChangeOriginFalse:
                        origin = "False";
                        submitData(origin);
                        break;
                    case R.id.rbChangeOriginDoNotDisclose:
                        origin = "Did Not Disclose";
                        submitData(origin);
                        break;
                }
            }
        });
    }

    private void submitData(String originValue) {
        final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(this));
        loadingDialog.startLoadingAnimation();
        Map<String, Object> map = new HashMap<>();
        map.put("origin", originValue);
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