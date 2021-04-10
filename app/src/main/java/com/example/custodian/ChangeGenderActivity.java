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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangeGenderActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    RadioGroup mGroup;
    Button mCancel;
    Button mConfirm;
    ImageButton mHelp;

    String gender;
    Context pageContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_gender);

        // Assigning of variable values
        mGroup = findViewById(R.id.rgChangeGenderGroup);
        mCancel = findViewById(R.id.btChangeGenderCancel);
        mConfirm = findViewById(R.id.btChangeGenderConfirm);
        mHelp = findViewById(R.id.ibChangeGenderHelp);

        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final com.example.custodian.AlertDialog helpDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "Please pick a gender from the radio group below.", "information");
                helpDialog.startLoadingAnimation();
            }
        });
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                switch (documentSnapshot.getString("gender")) {
                    case "Male":
                        mGroup.check(R.id.rbChangeGenderMale);
                        break;
                    case "Female":
                        mGroup.check(R.id.rbChangeGenderFemale);
                        break;
                    case "Other":
                        mGroup.check(R.id.rbChangeGenderOther);
                        break;
                    case "Did Not Disclose":
                        mGroup.check(R.id.rbChangeGenderDoNotDisclose);
                        break;
                }
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
                    case R.id.rbChangeGenderMale:
                        gender = "Male";
                        submitData(gender);
                        break;
                    case R.id.rbChangeGenderFemale:
                        gender = "Female";
                        submitData(gender);
                        break;
                    case R.id.rbChangeGenderOther:
                        gender = "Other";
                        submitData(gender);
                        break;
                    case R.id.rbChangeGenderDoNotDisclose:
                        gender = "Did Not Disclose";
                        submitData(gender);
                        break;
                }
            }
        });
    }

    private void submitData(String genderValue) {
        final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(this));
        loadingDialog.startLoadingAnimation();
        Map<String, Object> map = new HashMap<>();
        map.put("gender", genderValue);
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