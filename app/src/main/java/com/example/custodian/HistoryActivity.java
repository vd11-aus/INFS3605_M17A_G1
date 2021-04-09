package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

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
    private TextView mNoHistory;
    private RecyclerView mHistoryList;

    private String category = "history";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mHomeButton = findViewById(R.id.ibNavigationHome);
        mHistoryButton = findViewById(R.id.ibNavigationHistory);
        mNewPostButton = findViewById(R.id.ibNavigationNewPost);
        mRewardsButton = findViewById(R.id.ibNavigationRewards);
        mProfileButton = findViewById(R.id.ibNavigationProfile);
        mBackgroundImage = findViewById(R.id.ivHistoryBackground);
        mNoHistory = findViewById(R.id.tvHistoryNoHistory);
        mHistoryList = findViewById(R.id.rvHistoryList);

        NavigationBar navigationBar = new NavigationBar();
        navigationBar.create(mHomeButton, mHistoryButton, mNewPostButton, mRewardsButton, mProfileButton);
        navigationBar.getLocation(category);
        navigationBar.adjustToPage();

        mNoHistory.setVisibility(View.INVISIBLE);

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.login()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        // Making the List
        Context pageContext = this;
        FirebaseFirestore.getInstance().collection("entries").orderBy("time", Query.Direction.DESCENDING).whereEqualTo("user", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                ArrayList<Date> timeData = new ArrayList<Date>();
                ArrayList<String> titleData = new ArrayList<String>();
                ArrayList<String> overviewData = new ArrayList<String>();
                ArrayList<String> typeData = new ArrayList<String>();
                ArrayList<String> categoryData = new ArrayList<String>();
                ArrayList<Integer> postcodeData = new ArrayList<Integer>();
                ArrayList<String> contentData = new ArrayList<String>();
                ArrayList<Boolean> analysedData = new ArrayList<Boolean>();
                ArrayList<String> indigenousHeritageData = new ArrayList<String>();
                ArrayList<String> indigenousPreservationData = new ArrayList<String>();
                ArrayList<String> indigenousExposureData = new ArrayList<String>();
                for (DocumentSnapshot snapshot: snapshotList) {
                    Date value = snapshot.getTimestamp("time").toDate();
                    timeData.add(value);
                    titleData.add(snapshot.getString("title"));
                    overviewData.add(snapshot.getString("overview"));
                    typeData.add(snapshot.getString("type"));
                    categoryData.add(snapshot.getString("category"));
                    postcodeData.add(snapshot.getLong("postcode").intValue());
                    contentData.add(snapshot.getString("content"));
                    analysedData.add(snapshot.getBoolean("analysed"));
                    indigenousHeritageData.add(snapshot.getString("indigenousheritage"));
                    indigenousPreservationData.add(snapshot.getString("indigenouspreservation"));
                    indigenousExposureData.add(snapshot.getString("indigenousexposure"));
                }
                HistoryListAdapter listAdapter = new HistoryListAdapter(timeData, titleData, overviewData, typeData, categoryData, postcodeData, analysedData, indigenousHeritageData, indigenousPreservationData, indigenousExposureData, FirebaseAuth.getInstance().getCurrentUser().getUid(), pageContext);
                mHistoryList.setAdapter(listAdapter);
                mHistoryList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                if (snapshotList.size() == 0) {
                    mNoHistory.setVisibility(View.VISIBLE);
                }
            }
        });

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