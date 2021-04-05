package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PointAccumulationActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    TextView mPoints;

    Context context;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_accumulation);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mPoints = findViewById(R.id.tvPointAccumulationPoints);

        context = this;

        Intent intent = getIntent();
        type = intent.getStringExtra("ENTRY_TYPE");

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Integer currentPoints = documentSnapshot.getLong("currentpoints").intValue();
                Integer allTimePoints = documentSnapshot.getLong("alltimepoints").intValue();
                Integer allTimePosts = documentSnapshot.getLong("alltimeposts").intValue();
                Integer additiveValue = 0;
                switch (type) {
                    case "text":
                        additiveValue = 4;
                        break;
                    case "image":
                        additiveValue = 7;
                        break;
                    case "video":
                        mPoints.setText("+10pts");
                        additiveValue = 10;
                        break;
                }
                mPoints.setText("+" + additiveValue + "pts");
                currentPoints += additiveValue;
                allTimePoints += additiveValue;
                allTimePosts += 1;
                Map<String, Object> map = new HashMap<>();
                map.put("currentpoints", currentPoints);
                map.put("alltimepoints", allTimePoints);
                map.put("alltimeposts", allTimePosts);
                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
            }
        });
    }

    // Go to HomeActivity
    private void launchHomeActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}