package com.example.custodian;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

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
    private ImageButton mGeolocationButton;
    private ImageView mBackgroundImage;
    private TextView mGeolocation;

    private ImageView mEventOneBackground;
    private TextView mEventOneHeader;
    private TextView mEventOneSubtext;
    private Button mEventOneInteractButton;

    private ImageView mEventTwoBackground;
    private TextView mEventTwoHeader;
    private TextView mEventTwoSubtext;
    private Button mEventTwoInteractButton;

    private ImageView mEventThreeBackground;
    private TextView mEventThreeHeader;
    private TextView mEventThreeSubtext;
    private Button mEventThreeInteractButton;

    private String category = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        Context context = getApplicationContext();

        mHomeButton = findViewById(R.id.ibNavigationHome);
        mHistoryButton = findViewById(R.id.ibNavigationHistory);
        mNewPostButton = findViewById(R.id.ibNavigationNewPost);
        mRewardsButton = findViewById(R.id.ibNavigationRewards);
        mProfileButton = findViewById(R.id.ibNavigationProfile);
        mGeolocationButton = findViewById(R.id.ibHomeGetGeolocation);
        mBackgroundImage = findViewById(R.id.ivHomeBackground);
        mGeolocation = findViewById(R.id.tvHomeGeolocation);

        mEventOneBackground = findViewById(R.id.ivHomeEventsOneBackground);
        mEventOneHeader = findViewById(R.id.tvHomeEventsOneMainText);
        mEventOneSubtext = findViewById(R.id.tvHomeEventsOneSubtext);
        mEventOneInteractButton = findViewById(R.id.btHomeEventsOneInteract);

        mEventTwoBackground = findViewById(R.id.ivHomeEventsTwoBackground);
        mEventTwoHeader = findViewById(R.id.tvHomeEventsTwoMainText);
        mEventTwoSubtext = findViewById(R.id.tvHomeEventsTwoSubtext);
        mEventTwoInteractButton = findViewById(R.id.btHomeEventsTwoInteract);

        mEventThreeBackground = findViewById(R.id.ivHomeEventsThreeBackground);
        mEventThreeHeader = findViewById(R.id.tvHomeEventsThreeMainText);
        mEventThreeSubtext = findViewById(R.id.tvHomeEventsThreeSubtext);
        mEventThreeInteractButton = findViewById(R.id.btHomeEventsThreeInteract);

        NavigationBar navigationBar = new NavigationBar();
        navigationBar.create(mHomeButton, mHistoryButton, mNewPostButton, mRewardsButton, mProfileButton);
        navigationBar.getLocation(category);
        navigationBar.adjustToPage();

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.login()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        // Get Geolocation
        mGeolocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Getting geolocation.");
                mGeolocation.setText(getGeolocation());
                updateEvents(Integer.parseInt(getGeolocation()), context);
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

    // Finds current location
    public String getGeolocation() {
        return "7150";
    }

    // Updates events
    public void updateEvents(Integer postcode, Context context) {
        ArrayList<String> backgroundArray = new ArrayList<String>();
        ArrayList<String> headerArray = new ArrayList<String>();
        ArrayList<String> subtextArray = new ArrayList<String>();
        ArrayList<String> linkArray = new ArrayList<String>();
        FirebaseFirestore.getInstance().collection("events").orderBy("date", Query.Direction.DESCENDING).limit(3).whereEqualTo("postcode", postcode).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document: snapshotList) {
                    backgroundArray.add(document.getString("image"));
                    headerArray.add(document.getString("event"));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM YYYY");
                    Date date = Objects.requireNonNull(document.getTimestamp("date")).toDate();
                    subtextArray.add(formatter.format(date));
                    linkArray.add(document.getString("link"));
                }

                Glide.with(mEventOneBackground).load(backgroundArray.get(0)).centerCrop().placeholder(R.drawable.custom_background_2)
                        .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mEventOneBackground);
                mEventOneHeader.setText(headerArray.get(0));
                mEventOneSubtext.setText(subtextArray.get(0));
                mEventOneInteractButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Redirecting ...");
                        Intent intent = new Intent(context, RedirectActivity.class);
                        intent.putExtra("LINK_LOCATION", linkArray.get(0));
                        startActivity(intent);
                    }
                });

                Glide.with(mEventTwoBackground).load(backgroundArray.get(1)).centerCrop().placeholder(R.drawable.custom_background_2)
                        .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mEventTwoBackground);
                mEventTwoHeader.setText(headerArray.get(1));
                mEventTwoSubtext.setText(subtextArray.get(1));
                mEventTwoInteractButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Redirecting ...");
                        Intent intent = new Intent(context, RedirectActivity.class);
                        intent.putExtra("LINK_LOCATION", linkArray.get(1));
                        startActivity(intent);
                    }
                });

                Glide.with(mEventThreeBackground).load(backgroundArray.get(2)).centerCrop().placeholder(R.drawable.custom_background_2)
                        .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mEventThreeBackground);
                mEventThreeHeader.setText(headerArray.get(2));
                mEventThreeSubtext.setText(subtextArray.get(2));
                mEventThreeInteractButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Redirecting ...");
                        Intent intent = new Intent(context, RedirectActivity.class);
                        intent.putExtra("LINK_LOCATION", linkArray.get(2));
                        startActivity(intent);
                    }
                });
            }
        });
    }
}