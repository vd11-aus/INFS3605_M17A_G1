package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

public class WelcomeActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private TextView mUsernameText;
    private Button mNextPageButton;
    private ImageView mBackgroundImage;

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirebaseFS;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mUsernameText = findViewById(R.id.tvWelcomeUsername);
        mNextPageButton = findViewById(R.id.btWelcomeNextPage);
        mNextPageButton.setEnabled(false);
        mBackgroundImage = findViewById(R.id.ivWelcomeBackground);

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.welcome()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        // Verify user found
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            System.out.println(currentUser.getUid());
        }

        // Connect to Firestore FS
        String uniqueIdentifier = currentUser.getUid();
        mFirebaseFS = FirebaseFirestore.getInstance();
        DocumentReference referencePathOne = mFirebaseFS.document("users/"+uniqueIdentifier);
        referencePathOne.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                username = value.getString("username");
                mUsernameText.setText(username);
                mNextPageButton.setEnabled(true);
            }
        });

        // Navigate to user home
        mNextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Next page button clicked.");
                launchHomeActivity();
            }
        });
    }

    // Go to HomeActivity
    private void launchHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}