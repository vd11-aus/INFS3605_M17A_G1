package com.example.custodian;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private Integer postCode;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        context = this;

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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                System.out.println("Getting geolocation.");
                getGeolocation();
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
    private void getGeolocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 45);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, 43);
        } else {
            Double latitude;
            Double longitude;
            Location gpsLocation = null;
            Location networkLocation = null;
            Location finalLocation = null;
            try {
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (gpsLocation != null) {
                finalLocation = gpsLocation;
                latitude = finalLocation.getLatitude();
                longitude = finalLocation.getLongitude();
            } else if (networkLocation != null) {
                finalLocation = networkLocation;
                latitude = finalLocation.getLatitude();
                longitude = finalLocation.getLongitude();
            } else {
                latitude = 0.0;
                longitude = 0.0;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                FirebaseFirestore.getInstance().collection("lands").whereEqualTo("postcode", Integer.parseInt(postalCode)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document: snapshotList) {
                            mGeolocation.setText(document.getString("name") + " - " + document.getString("suburb") + ", " + state + " " + postalCode);
                        }
                    }
                });
                postCode = Integer.parseInt(postalCode);
                System.out.println("Postcode: " + postalCode);
                updateEvents(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Updates events
    public void updateEvents(Context context) {
        ArrayList<String> backgroundArray = new ArrayList<String>();
        ArrayList<String> headerArray = new ArrayList<String>();
        ArrayList<String> subtextArray = new ArrayList<String>();
        ArrayList<String> linkArray = new ArrayList<String>();
        FirebaseFirestore.getInstance().collection("events").orderBy("date", Query.Direction.DESCENDING).limit(3).whereEqualTo("postcode", postCode).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document: snapshotList) {
                    backgroundArray.add(document.getString("image"));
                    headerArray.add(document.getString("event"));
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd MMM YYYY");
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