package com.example.custodian;

import android.content.Intent;
import android.widget.ImageButton;

public class NavigationBar {

    // Class variables
    ImageButton mHome;
    ImageButton mHistory;
    ImageButton mNewPost;
    ImageButton mRewards;
    ImageButton mProfile;

    String vLocation;

    // Create class
    public void create(ImageButton home, ImageButton history, ImageButton newPost, ImageButton rewards, ImageButton profile) {
        mHome = home;
        mHistory = history;
        mNewPost = newPost;
        mRewards = rewards;
        mProfile = profile;
    }

    // Get current page location
    public void getLocation(String location) {
        vLocation = location;
    }

    // Colour buttons
    public void adjustToPage() {
        switch (vLocation) {
            case "home":
                System.out.println("You're in section: Home");
                mHome.setAlpha((float) 1.0);
                mHistory.setAlpha((float) 0.5);
                mRewards.setAlpha((float) 0.5);
                mProfile.setAlpha((float) 0.5);
                break;
            case "history":
                System.out.println("You're in section: History");
                mHome.setAlpha((float) 0.5);
                mHistory.setAlpha((float) 1.0);
                mRewards.setAlpha((float) 0.5);
                mProfile.setAlpha((float) 0.5);
                break;
            case "rewards":
                System.out.println("You're in section: Rewards");
                mHome.setAlpha((float) 0.5);
                mHistory.setAlpha((float) 0.5);
                mRewards.setAlpha((float) 1.0);
                mProfile.setAlpha((float) 0.5);
                break;
            case "profile":
                System.out.println("You're in section: Profile");
                mHome.setAlpha((float) 0.5);
                mHistory.setAlpha((float) 0.5);
                mRewards.setAlpha((float) 0.5);
                mProfile.setAlpha((float) 1.0);
                break;
        }
    }
}
