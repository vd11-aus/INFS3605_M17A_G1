package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class RedemptionCodeActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private Button mBackButton;
    private TextView mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption_code);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mBackButton = findViewById(R.id.btRedemptionBack);
        mCode = findViewById(R.id.tvRedemptionCode);

        // Assign code
        mCode.setText(codeGenerator());

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRewardsActivity();
            }
        });
    }

    // Generate random code
    private String codeGenerator() {
        String returnValue = "";
        String data = "0123456789abcdefghijklmnopqrstuvwxyz";
        ArrayList<String> characters = new ArrayList<String>();
        for (int counter = 0; counter < data.length(); counter++) {
            characters.add(String.valueOf(data.charAt(counter)));
        }
        for (int counter = 0; counter < 5; counter++) {
            returnValue += characters.get((int) (Math.random() * data.length() + 0));
        }
        returnValue = returnValue.toUpperCase();
        System.out.println(returnValue);
        return returnValue;
    }

    // Go to RewardsActivity
    private void launchRewardsActivity() {
        Intent intent = new Intent(this, RewardsActivity.class);
        startActivity(intent);
    }
}