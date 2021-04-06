package com.example.custodian;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    private Integer postCode;

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
                showProceedWarningDialog(getCurrentFocus(), "text");
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProceedWarningDialog(getCurrentFocus(), "image");
            }
        });
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProceedWarningDialog(getCurrentFocus(), "video");
            }
        });
    }

    // Warn user that you can't go back once you proceed
    public void showProceedWarningDialog(View v, String type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you sure?");
        alert.setMessage("If you continue, your current location postcode will be recorded.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getGeolocation(type);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    // Finds current location
    private void getGeolocation(String type) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewPostActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 45);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewPostActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewPostActivity.this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, 43);
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

                postCode = Integer.parseInt(postalCode);
                launchXActivity(type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Go to New Post Part 2
    private void launchXActivity(String type) {
        Intent textIntent = new Intent(this, TextPostActivity.class);
        textIntent.putExtra("POSTCODE", postCode);
        Intent imageIntent = new Intent(this, ImagePostActivity.class);
        imageIntent.putExtra("POSTCODE", postCode);
        Intent videoIntent = new Intent(this, VideoPostActivity.class);
        videoIntent.putExtra("POSTCODE", postCode);

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