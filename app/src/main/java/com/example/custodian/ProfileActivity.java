package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private ImageButton mHomeButton;
    private ImageButton mHistoryButton;
    private ImageButton mNewPostButton;
    private ImageButton mRewardsButton;
    private ImageButton mProfileButton;
    private ImageView mBackgroundImage;
    private ImageView mProfileIcon;

    FirebaseAuth firebaseAuth;

    private String category = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mHomeButton = findViewById(R.id.ibNavigationHome);
        mHistoryButton = findViewById(R.id.ibNavigationHistory);
        mNewPostButton = findViewById(R.id.ibNavigationNewPost);
        mRewardsButton = findViewById(R.id.ibNavigationRewards);
        mProfileButton = findViewById(R.id.ibNavigationProfile);
        mBackgroundImage = findViewById(R.id.ivProfileBackground);
        mProfileIcon = findViewById(R.id.ivProfileSample);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
            Glide.with(this).load(firebaseAuth.getCurrentUser().getPhotoUrl()).into(mProfileIcon);
        }

        NavigationBar navigationBar = new NavigationBar();
        navigationBar.create(mHomeButton, mHistoryButton, mNewPostButton, mRewardsButton, mProfileButton);
        navigationBar.getLocation(category);
        navigationBar.adjustToPage();

        // Go to Home
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchXActivity("home");
            }
        });

        // Go to History
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchXActivity("history");
            }
        });

        // Create a New Post
        mNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchXActivity("new-post");
            }
        });

        // Go to Rewards
        mRewardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchXActivity("rewards");
            }
        });

        // Go to Profile
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchXActivity("profile");
            }
        });
    }

    // Go to XActivity
    public void launchXActivity(String activity) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        Intent historyIntent = new Intent(this, HistoryActivity.class);
        Intent newPostIntent = new Intent(this, NewPostActivity.class);
        Intent rewardsIntent = new Intent(this, RewardsActivity.class);
        Intent profileIntent = new Intent(this, ProfileActivity.class);

        switch (activity) {
            case "home":
                System.out.println("Section clicked: Home");
                startActivity(homeIntent);
                break;
            case "history":
                System.out.println("Section clicked: History");
                startActivity(historyIntent);
                break;
            case "new-post":
                System.out.println("Section clicked: New Post");
                startActivity(newPostIntent);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                break;
            case "rewards":
                System.out.println("Section clicked: Rewards");
                startActivity(rewardsIntent);
                break;
            case "profile":
                System.out.println("Section clicked: Profile");
                startActivity(profileIntent);
                break;
        }
    }
}