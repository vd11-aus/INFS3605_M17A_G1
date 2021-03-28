package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RewardsActivity extends AppCompatActivity {

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
    private RecyclerView mRewardsList;
    private TextView mCurrentPoints;

    private String category = "rewards";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        // Assigning of variable values
        mHomeButton = findViewById(R.id.ibNavigationHome);
        mHistoryButton = findViewById(R.id.ibNavigationHistory);
        mNewPostButton = findViewById(R.id.ibNavigationNewPost);
        mRewardsButton = findViewById(R.id.ibNavigationRewards);
        mProfileButton = findViewById(R.id.ibNavigationProfile);
        mBackgroundImage = findViewById(R.id.ivRewardsBackground);
        mRewardsList = findViewById(R.id.rvRewardsList);
        mCurrentPoints = findViewById(R.id.tvRewardsCurrentPoints);

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        NavigationBar navigationBar = new NavigationBar();
        navigationBar.create(mHomeButton, mHistoryButton, mNewPostButton, mRewardsButton, mProfileButton);
        navigationBar.getLocation(category);
        navigationBar.adjustToPage();

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.login()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        // Making the Rewards List
        Context pageContext = this;
        FirebaseFirestore.getInstance().document("users/" + id).get().addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                Integer currentPoints = snapshot.getLong("currentpoints").intValue();
                FirebaseFirestore.getInstance().collection("rewards").orderBy("cost").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> rewardTitles = new ArrayList<String>();
                        ArrayList<String> rewardDescriptions = new ArrayList<String>();
                        ArrayList<String> rewardImages = new ArrayList<String>();
                        ArrayList<String> rewardIds = new ArrayList<String>();
                        ArrayList<Integer> rewardCosts = new ArrayList<Integer>();
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot: snapshotList) {
                            System.out.println("onSuccess: " + snapshot.getData());
                            rewardTitles.add(snapshot.getString("title"));
                            rewardDescriptions.add(snapshot.getString("description"));
                            rewardCosts.add(snapshot.getLong("cost").intValue());
                            rewardImages.add(snapshot.getString("image"));
                            rewardIds.add(snapshot.getString("id"));
                        }
                        RewardsListAdapter listAdapter = new RewardsListAdapter(pageContext, rewardTitles, rewardDescriptions, rewardImages, rewardIds, rewardCosts, currentPoints);
                        mRewardsList.setAdapter(listAdapter);
                        mRewardsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                });
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