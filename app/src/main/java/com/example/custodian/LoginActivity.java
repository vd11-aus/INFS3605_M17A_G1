package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private ImageButton mExitButton;
    private ImageButton mEmailHelpButton;
    private ImageButton mPasswordHelpButton;
    private Button mLoginButton;
    private Button mCreateAccountButton;
    private TextView mForgotDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Assigning of variable values
        mExitButton = findViewById(R.id.ibLoginExit);
        mEmailHelpButton = findViewById(R.id.ibLoginEmailHelp);
        mPasswordHelpButton = findViewById(R.id.ibLoginPasswordHelp);
        mLoginButton = findViewById(R.id.btLoginLogin);
        mCreateAccountButton = findViewById(R.id.btLoginCreateAccount);
        mForgotDetailsButton = findViewById(R.id.tvLoginForgotDetails);

        // Cancel log in
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Back Button Clicked");
                launchMainActivity();
            }
        });

        // Develop messages
        Snackbar emailHelpMessage = Snackbar.make(findViewById(R.id.clLoginMainLayout),
                "This is where you enter the email you used to create this account.", Snackbar.LENGTH_SHORT);
        Snackbar passwordHelpMessage = Snackbar.make(findViewById(R.id.clLoginMainLayout),
                "This is where you enter the password associated with the email you used to create this account.", Snackbar.LENGTH_SHORT);

        // Request for help on email input
        mEmailHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Email Help Button Clicked");
                emailHelpMessage.show();
            }
        });

        // Request for help on password input
        mPasswordHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Password Help Button Clicked");
                passwordHelpMessage.show();
            }
        });

        // Confirm log in
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Log In Button Clicked");
            }
        });

        // Navigate to account creation
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Create Account Button Clicked");
                launchCreateAccountActivity();
            }
        });

        // Navigate to account recovery
        mForgotDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Forgot Details Button Clicked");
                launchAccountRecoveryWebsite();
            }
        });
    }

    // Go to MainActivity
    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // Go to CreateAccountActivity
    private void launchCreateAccountActivity() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    // Go to Account Recovery Website
    private void launchAccountRecoveryWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.custodian.com.au/forgot-my-credentials/"));
        startActivity(intent);
    }
}