package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class NewPostActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private ImageView mBackgroundImage;
    private Button mCancelButton;
    private Button mTextButton;
    private Button mImageButton;
    private Button mVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mBackgroundImage = findViewById(R.id.ivNewPostBackground);
        mCancelButton = findViewById(R.id.btNewPostCancel);
        mTextButton = findViewById(R.id.btNewPostTextClicker);
        mImageButton = findViewById(R.id.btNewPostImageClicker);
        mVideoButton = findViewById(R.id.btNewPostVideoClicker);

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.splash()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        // Redirect to Home screen
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeActivity();
            }
        });

        // Go to next section
        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchXActivity("text");
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchXActivity("image");
            }
        });
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchXActivity("video");
            }
        });
    }

    // Go to New Post Part 2
    private void launchXActivity(String type) {
        Intent textIntent = new Intent(this, TextPostActivity.class);
        Intent imageIntent = new Intent(this, ImagePostActivity.class);
        Intent videoIntent = new Intent(this, VideoPostActivity.class);

        switch (type) {
            case "text":
                System.out.println("Going to text post.");
                startActivity(textIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case "image":
                System.out.println("Going to image post.");
                startActivity(imageIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case "video":
                System.out.println("Going to video post.");
                startActivity(videoIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    // Go to HomeActivity
    private void launchHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}