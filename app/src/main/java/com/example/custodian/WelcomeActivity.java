package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirebaseDB;
    String username;
    Integer origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Assigning of variable values
        mUsernameText = findViewById(R.id.tvWelcomeUsername);
        mNextPageButton = findViewById(R.id.btWelcomeNextPage);
        mNextPageButton.setEnabled(false);

        // Verify user found
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            System.out.println(currentUser.getUid());
        }

        // Connect to Firestore DB
        String uniqueIdentifier = currentUser.getUid();
        mFirebaseDB = FirebaseFirestore.getInstance();
        DocumentReference referencePathOne = mFirebaseDB.document("users/"+uniqueIdentifier);
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
            }
        });
    }
}