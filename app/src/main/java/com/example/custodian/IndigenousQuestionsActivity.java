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
    SmileRating mRatingOne;
    SmileRating mRatingTwo;
    SmileRating mRatingThree;
    EditText mComments;
    Button mSubmitButton;

    String document;
    String type;
    Context pageContext = this;

    // Brought to you by: https://github.com/sujithkanna/SmileyRating

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indigenous_questions);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mBackgroundImage = findViewById(R.id.ivIndigenousQuestionsBackground);
        mRatingOne = (SmileRating) findViewById(R.id.srIndigenousQuestionsRatingOne);;
        mRatingTwo = (SmileRating) findViewById(R.id.srIndigenousQuestionsRatingTwo);
        mRatingThree = (SmileRating) findViewById(R.id.srIndigenousQuestionsRatingThree);
        mComments = findViewById(R.id.etIndigenousQuestionsComments);
        mSubmitButton = findViewById(R.id.btIndigenousQuestionsSubmit);

        mRatingOne.setSelectedSmile(SmileRating.OKAY);
        mRatingTwo.setSelectedSmile(SmileRating.OKAY);
        mRatingThree.setSelectedSmile(SmileRating.OKAY);

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
                showSubmitWarningDialog(getCurrentFocus());
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
                submit();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    // Initialise after performing field check
    private void submit() {
        if (mRatingOne.getRating() == SmileRating.NONE || mRatingTwo.getRating() == SmileRating.NONE || mRatingThree.getRating() == SmileRating.NONE) {
            final com.example.custodian.AlertDialog incompleteDialog = new com.example.custodian.AlertDialog(new Dialog(pageContext), "You haven't filled out the necessary fields yet.", "warning");
            incompleteDialog.startLoadingAnimation();
        } else {
            insertData();
        }
    }

    // Insert data into existing file
    private void insertData() {
        final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(pageContext));
        loadingDialog.startLoadingAnimation();
        String indigenousQuestionOne = "";
        switch (mRatingOne.getRating()) {
            case SmileRating.TERRIBLE:
                indigenousQuestionOne = "terrible";
                break;
            case SmileRating.BAD:
                indigenousQuestionOne = "bad";
                break;
            case SmileRating.OKAY:
                indigenousQuestionOne = "okay";
                break;
            case SmileRating.GOOD:
                indigenousQuestionOne = "good";
                break;
            case SmileRating.GREAT:
                indigenousQuestionOne = "great";
                break;
            default:
                indigenousQuestionOne = "okay";
                break;
        }
        String indigenousQuestionTwo = "";
        switch (mRatingTwo.getRating()) {
            case SmileRating.TERRIBLE:
                indigenousQuestionTwo = "terrible";
                break;
            case SmileRating.BAD:
                indigenousQuestionTwo = "bad";
                break;
            case SmileRating.OKAY:
                indigenousQuestionTwo = "okay";
                break;
            case SmileRating.GOOD:
                indigenousQuestionTwo = "good";
                break;
            case SmileRating.GREAT:
                indigenousQuestionTwo = "great";
                break;
            default:
                indigenousQuestionTwo = "okay";
                break;
        }
        String indigenousQuestionThree = "";
        switch (mRatingOne.getRating()) {
            case SmileRating.TERRIBLE:
                indigenousQuestionThree = "terrible";
                break;
            case SmileRating.BAD:
                indigenousQuestionThree = "bad";
                break;
            case SmileRating.OKAY:
                indigenousQuestionThree = "okay";
                break;
            case SmileRating.GOOD:
                indigenousQuestionThree = "good";
                break;
            case SmileRating.GREAT:
                indigenousQuestionThree = "great";
                break;
            default:
                indigenousQuestionThree = "okay";
                break;
        }
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