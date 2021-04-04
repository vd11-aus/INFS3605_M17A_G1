package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoPostActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    ImageButton mBackButton;
    Button mProceedButton;
    EditText mTitle;
    EditText mOverview;
    ImageView mBackgroundImage;
    Spinner mCategorySelect;

    Integer postcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);

        // Assigning of variable values
        mBackButton = findViewById(R.id.ibVideoPostBack);
        mProceedButton = findViewById(R.id.btVideoPostProceed);
        mTitle = findViewById(R.id.etVideoPostTitle);
        mOverview = findViewById(R.id.etVideoPostOverview);
        mBackgroundImage = findViewById(R.id.ivVideoPostBackground);
        mCategorySelect = findViewById(R.id.spVideoPostCategorySelect);

        Intent intent = getIntent();
        postcode = intent.getIntExtra("POSTCODE", 0);

        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySelect.setAdapter(categoryAdapter);
        mCategorySelect.setSelection(0);

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.login()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        // Cancel post
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitWarningDialog(getCurrentFocus());
            }
        });

        // Proceed to next section
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFields()) {
                    Snackbar.make(findViewById(R.id.clImagePostMainLayout),
                            "You haven't filled out all the fields yet.", Snackbar.LENGTH_SHORT).show();
                } else {
                    showProceedWarningDialog(getCurrentFocus());
                }
            }
        });
    }

    // Check fields have been filled
    private boolean checkFields() {
        boolean returnValue = true;
        if (mTitle.getText().toString().isEmpty() || mOverview.getText().toString().isEmpty() || mCategorySelect.getSelectedItemPosition() == 0) {
            returnValue = false;
        }
        return returnValue;
    }

    // Upload data
    private void submitData() {
        String uniqueEntry = System.currentTimeMillis()/1000 + "-" + codeGenerator();
        Map<String, Object> map = new HashMap<>();
        map.put("title", mTitle.getText().toString());
        map.put("overview", mOverview.getText().toString());
        map.put("type", "video");
        map.put("category", mCategorySelect.getSelectedItem().toString());
        map.put("postcode", postcode);
        map.put("content", "");
        map.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseFirestore.getInstance().collection("entries").document(uniqueEntry).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    // Generate random code
    private String codeGenerator() {
        String returnValue = "";
        String data = "0123456789abcdefghijklmnopqrstuvwxyz";
        ArrayList<String> characters = new ArrayList<String>();
        for (int counter = 0; counter < data.length(); counter++) {
            characters.add(String.valueOf(data.charAt(counter)));
        }
        for (int counter = 0; counter < 5; counter++) {
            returnValue += characters.get((int) (Math.random() * data.length() + 0));
        }
        returnValue = returnValue.toUpperCase();
        System.out.println(returnValue);
        return returnValue;
    }

    // Warn user that any entered data will be wiped
    public void showExitWarningDialog(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you sure?");
        alert.setMessage("If you quit, any form entries you have made thus far will be deleted.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchNewPostActivity();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    // Warn user that you can't go back once you proceed
    public void showProceedWarningDialog(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you sure?");
        alert.setMessage("If you proceed, you will not be able to go back to change your post and form entries will be submitted.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(findViewById(R.id.clVideoPostMainLayout),
                        "Submitting data - please wait.", Snackbar.LENGTH_INDEFINITE).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submitData();
                        launchIndigenousQuestionsActivity();
                    }
                }, 5000);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    // Go to NewPostActivity
    private void launchNewPostActivity() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // Go to IndigenousQuestionsActivity
    private void launchIndigenousQuestionsActivity() {
        Intent intent = new Intent(this, IndigenousQuestionsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}