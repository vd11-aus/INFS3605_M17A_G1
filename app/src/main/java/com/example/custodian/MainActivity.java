package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.sql.Timestamp;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private ImageButton mLoginButton;
    private ImageButton mInformationButton;
    private ImageView mBackgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mLoginButton = findViewById(R.id.ibMainLogin);
        mInformationButton = findViewById(R.id.ibMainInformation);
        mBackgroundImage = findViewById(R.id.ivMainBackground);
        
        // Get permissions
        Context pageContext = this;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            final PermissionsDialog permissionsDialog = new PermissionsDialog(pageContext,this, new Dialog(pageContext));
            permissionsDialog.startLoadingAnimation();
        }

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.splash()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

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
                Dialog dialog = new Dialog(pageContext);
                dialog.setContentView(R.layout.information_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                ImageButton mExitButton = dialog.findViewById(R.id.ibInformationExit);
                TextView mEmailButton = dialog.findViewById(R.id.tvInformationEmail);
                mExitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Back Button Clicked");
                        dialog.dismiss();
                    }
                });
                mEmailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Email Button Clicked");
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_EMAIL, "m17agroup1@unsw.edu.au");
                        intent.setType("message/rfc822");
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });

        // Get time
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Date date = new Date (time.getTime());
        System.out.println(date);
    }

    // Go to LoginActivity
    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}