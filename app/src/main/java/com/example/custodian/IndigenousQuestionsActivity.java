package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class IndigenousQuestionsActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    ImageView mBackgroundImage;
    SmileyRating mRatingOne;
    SmileyRating mRatingTwo;
    SmileyRating mRatingThree;
    EditText mComments;
    Button mSubmitButton;

    String document;
    String type;
    Context pageContext = this;
    String indigenousQuestionOne = "okay";
    String indigenousQuestionTwo  = "okay";
    String indigenousQuestionThree  = "okay";
    Boolean questionOneFilled = false;
    Boolean questionTwoFilled = false;
    Boolean questionThreeFilled = false;

    // Brought to you by: https://github.com/sujithkanna/SmileyRating

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indigenous_questions);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mBackgroundImage = findViewById(R.id.ivIndigenousQuestionsBackground);
        mRatingOne = (SmileyRating) findViewById(R.id.srIndigenousQuestionsRatingOne);;
        mRatingTwo = (SmileyRating) findViewById(R.id.srIndigenousQuestionsRatingTwo);
        mRatingThree = (SmileyRating) findViewById(R.id.srIndigenousQuestionsRatingThree);
        mComments = findViewById(R.id.etIndigenousQuestionsComments);
        mSubmitButton = findViewById(R.id.btIndigenousQuestionsSubmit);

        mRatingOne.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                questionOneFilled = true;
                Integer value = type.getRating();
                switch (value) {
                    case 1:
                        indigenousQuestionOne = "terrible";
                        break;
                    case 2:
                        indigenousQuestionOne = "bad";
                        break;
                    case 3:
                        indigenousQuestionOne = "okay";
                        break;
                    case 4:
                        indigenousQuestionOne = "good";
                        break;
                    case 5:
                        indigenousQuestionOne = "great";
                        break;
                }
            }
        });
        mRatingTwo.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                questionTwoFilled = true;
                Integer value = type.getRating();
                switch (value) {
                    case 1:
                        indigenousQuestionTwo = "terrible";
                        break;
                    case 2:
                        indigenousQuestionTwo = "bad";
                        break;
                    case 3:
                        indigenousQuestionTwo = "okay";
                        break;
                    case 4:
                        indigenousQuestionTwo = "good";
                        break;
                    case 5:
                        indigenousQuestionTwo = "great";
                        break;
                }
            }
        });
        mRatingThree.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                questionThreeFilled = true;
                Integer value = type.getRating();
                switch (value) {
                    case 1:
                        indigenousQuestionThree = "terrible";
                        break;
                    case 2:
                        indigenousQuestionThree = "bad";
                        break;
                    case 3:
                        indigenousQuestionThree = "okay";
                        break;
                    case 4:
                        indigenousQuestionThree = "good";
                        break;
                    case 5:
                        indigenousQuestionThree = "great";
                        break;
                }
            }
        });

        Intent intent = getIntent();
        document = intent.getStringExtra("ENTRY_ID");
        type = intent.getStringExtra("ENTRY_TYPE");

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.welcome()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!questionOneFilled || !questionTwoFilled || !questionThreeFilled) {
                    final com.example.custodian.AlertDialog incompleteDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "You haven't filled out the necessary fields yet.", "warning");
                    incompleteDialog.startLoadingAnimation();
                } else {
                    showSubmitWarningDialog(getCurrentFocus());
                }
            }
        });
    }

    // Warn user that you can't go back once you proceed
    public void showSubmitWarningDialog(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you sure?");
        alert.setMessage("If you submit, you will not be able to go back to change your submission.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertData();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    // Insert data into existing file
    private void insertData() {
        final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(pageContext));
        loadingDialog.startLoadingAnimation();
        Map<String, Object> map = new HashMap<>();
        map.put("indigenousheritage", indigenousQuestionOne);
        map.put("indigenouspreservation", indigenousQuestionTwo);
        map.put("indigenousexposure", indigenousQuestionThree);
        if (mComments.getText().toString().isEmpty()) {
            map.put("indigenouscomments", "No additional comments");
        } else {
            map.put("indigenouscomments", mComments.getText().toString());
        }
        FirebaseFirestore.getInstance().collection("entries").document(document).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                launchPointAccumulationActivity();
            }
        });
    }

    // Go to PointAccumulationActivity
    private void launchPointAccumulationActivity() {
        Intent intent = new Intent(this, PointAccumulationActivity.class);
        intent.putExtra("ENTRY_TYPE", type);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}