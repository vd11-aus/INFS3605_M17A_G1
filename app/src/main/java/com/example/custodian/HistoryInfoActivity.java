package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryInfoActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    ImageView mBackgroundImage;
    Button mClose;
    TextView mTitle;
    TextView mDate;
    TextView mLocation;
    TextView mOverview;
    TextView mCategory;
    TextView mAnalysed;
    ImageView mAnalysedBackground;
    SmileyRating mRatingOne;
    SmileyRating mRatingTwo;
    SmileyRating mRatingThree;
    EditText mContentText;
    ImageView mContentImage;
    VideoView mContentVideo;

    Context pageContext = this;
    String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_info);

        // Assigning of variable values
        mBackgroundImage = findViewById(R.id.ivHistoryInfoBackground);
        mClose = findViewById(R.id.btHistoryInfoClose);
        mTitle = findViewById(R.id.tvHistoryInfoTitle);
        mDate = findViewById(R.id.tvHistoryInfoDate);
        mLocation = findViewById(R.id.tvHistoryInfoLocation);
        mOverview = findViewById(R.id.tvHistoryInfoOverview);
        mCategory = findViewById(R.id.tvHistoryInfoCategory);
        mAnalysed = findViewById(R.id.tvHistoryInfoAnalysed);
        mAnalysedBackground = findViewById(R.id.ivHistoryInfoAnalysedBorder);
        mRatingOne = (SmileyRating) findViewById(R.id.srHistoryInfoHeritage);;
        mRatingTwo = (SmileyRating) findViewById(R.id.srHistoryInfoPreservation);
        mRatingThree = (SmileyRating) findViewById(R.id.srHistoryInfoExposure);
        mContentText = findViewById(R.id.etHistoryInfoContent);
        mContentImage = findViewById(R.id.ivHistoryInfoContent);
        mContentVideo = findViewById(R.id.vvHistoryInfoContent);

        mContentText.setVisibility(View.INVISIBLE);
        mContentImage.setVisibility(View.INVISIBLE);
        mContentVideo.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        identifier = intent.getStringExtra("IDENTIFIER");

        Context pageContext = this;
        FirebaseFirestore.getInstance().collection("entries").document(identifier).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final LoadingDialog loadingDialog = new LoadingDialog(new Dialog(pageContext));
                loadingDialog.startLoadingAnimation();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                        switch (task.getResult().getString("type")) {
                            case "text":
                                mContentText.setVisibility(View.VISIBLE);
                                mContentText.setText(task.getResult().getString("content"));
                                break;
                            case "image":
                                mContentImage.setVisibility(View.VISIBLE);
                                Glide.with(mContentImage).load(task.getResult().getString("content")).centerCrop().placeholder(R.drawable.custom_background_2)
                                        .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mContentImage);
                                break;
                            case "video":
                                mContentVideo.setVisibility(View.VISIBLE);
                                mContentVideo.setVideoURI(Uri.parse(task.getResult().getString("content")));
                                MediaController mediaController = new MediaController(pageContext);
                                mContentVideo.setMediaController(mediaController);
                                mediaController.setAnchorView(mContentVideo);
                                break;
                        }
                        mTitle.setText(task.getResult().getString("title"));
                        mOverview.setText(task.getResult().getString("overview"));
                        SimpleDateFormat format = new SimpleDateFormat("dd MMM YYYY");
                        String date = format.format(task.getResult().getTimestamp("time").toDate()).toUpperCase();
                        mDate.setText(date);
                        FirebaseFirestore.getInstance().collection("lands").whereEqualTo("postcode", task.getResult().getLong("postcode")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                String land = "";
                                for (DocumentSnapshot document: snapshotList) {
                                    land = String.format("%s" + ", %d", document.getString("suburb"), task.getResult().getLong("postcode").intValue());
                                }
                                mLocation.setText(land);
                            }
                        });
                        if (task.getResult().getBoolean("analysed")) {
                            mAnalysed.setText("Analysed - Check Messages");
                            mAnalysedBackground.setImageResource(R.drawable.custom_button_style_6);
                        } else {
                            mAnalysed.setText("Getting Analysed");
                            mAnalysedBackground.setImageResource(R.drawable.custom_button_style_7);
                        }
                        mCategory.setText(task.getResult().getString("category"));
                        switch (task.getResult().getString("indigenousheritage")) {
                            case "terrible":
                                mRatingOne.setRating(SmileyRating.Type.TERRIBLE);
                                break;
                            case "bad":
                                mRatingOne.setRating(SmileyRating.Type.BAD);
                                break;
                            case "okay":
                                mRatingOne.setRating(SmileyRating.Type.OKAY);
                                break;
                            case "good":
                                mRatingOne.setRating(SmileyRating.Type.GOOD);
                                break;
                            case "great":
                                mRatingOne.setRating(SmileyRating.Type.GREAT);
                                break;
                        }
                        switch (task.getResult().getString("indigenouspreservation")) {
                            case "terrible":
                                mRatingTwo.setRating(SmileyRating.Type.TERRIBLE);
                                break;
                            case "bad":
                                mRatingTwo.setRating(SmileyRating.Type.BAD);
                                break;
                            case "okay":
                                mRatingTwo.setRating(SmileyRating.Type.OKAY);
                                break;
                            case "good":
                                mRatingTwo.setRating(SmileyRating.Type.GOOD);
                                break;
                            case "great":
                                mRatingTwo.setRating(SmileyRating.Type.GREAT);
                                break;
                        }
                        switch (task.getResult().getString("indigenousexposure")) {
                            case "terrible":
                                mRatingThree.setRating(SmileyRating.Type.TERRIBLE);
                                break;
                            case "bad":
                                mRatingThree.setRating(SmileyRating.Type.BAD);
                                break;
                            case "okay":
                                mRatingThree.setRating(SmileyRating.Type.OKAY);
                                break;
                            case "good":
                                mRatingThree.setRating(SmileyRating.Type.GOOD);
                                break;
                            case "great":
                                mRatingThree.setRating(SmileyRating.Type.GREAT);
                                break;
                        }
                    }
                };
                handler.postDelayed(runnable, 1500);
            }
        });

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.welcome()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pageContext, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}