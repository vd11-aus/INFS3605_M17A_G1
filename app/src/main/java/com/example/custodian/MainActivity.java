package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private ImageButton mLoginButton;
    private ImageButton mInformationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning of variable values
        mLoginButton = findViewById(R.id.ibMainLogin);
        mInformationButton = findViewById(R.id.ibMainInformation);

        // Log in to account
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Login Button Clicked");
                launchLoginActivity();
            }
        });

        // See more information
        mInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Information Button Clicked");
                launchInformationActivity();
            }
        });
    }

    // Go to LoginActivity
    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // Go to InformationActivity
    private void launchInformationActivity() {
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
    }
}