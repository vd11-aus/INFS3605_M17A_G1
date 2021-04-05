package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private static final int PICKER_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private EditText mUsernameInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mConfirmPasswordInput;
    private RadioGroup mOriginInput;
    private RadioGroup mGenderInput;
    private ImageButton mCreateAccountButton;
    private ImageButton mCancelButton;
    private ImageView mProfileIcon;
    private ImageView mBackgroundImage;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFS;
    FirebaseStorage firebaseStorage;

    Uri iconImage;
    String username = "";
    String email = "";
    String password = "";
    String passwordConfirm = "";
    String origin = "";
    String gender = "";
    Boolean imageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Assigning of variable values
        mCreateAccountButton = findViewById(R.id.ibCreateAccountContinue);
        mCancelButton = findViewById(R.id.ibCreateAccountCancel);
        mUsernameInput = findViewById(R.id.etCreateAccountUsername);
        mEmailInput = findViewById(R.id.etCreateAccountEmail);
        mPasswordInput = findViewById(R.id.etCreateAccountPassword);
        mConfirmPasswordInput = findViewById(R.id.etCreateAccountConfirmPassword);
        mOriginInput = findViewById(R.id.rgCreateAccountOrigin);
        mGenderInput = findViewById(R.id.rgCreateAccountGender);
        mProfileIcon = findViewById(R.id.ivCreateAccountIcon);
        mBackgroundImage = findViewById(R.id.ivCreateAccountBackground);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFS = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // Get background
        BackgroundGenerator background = new BackgroundGenerator();
        Glide.with(mBackgroundImage).load(background.login()).centerCrop().placeholder(R.drawable.custom_background_2)
                .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);

        // Confirm create account
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Create Account Button Clicked");
                switch (entriesComplete()) {
                    case "incomplete-entry":
                        Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                                "One or more text fields are blank, please fill them and try again.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "invalid-username":
                        Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                                "Invalid username, it has to be a minimum of 8 characters with no spaces and at least one upper & lower case letter.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "invalid-email":
                        Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                                "Invalid email, it has to contain the '@' symbol.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "invalid-password":
                        Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                                "Invalid password, it has to be a minimum of 8 characters with no spaces and at least one upper & lower case letter.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "passwords-dont-match":
                        Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                                "The passwords you've provided don't match each other. Please correct them and try again.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "no-icon":
                        Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                                "You haven't added an icon yet!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "entries-validated":
                        genderQuestionCheck();
                        originQuestionCheck();
                        System.out.println("Username: " + username);
                        System.out.println("Email: " + email);
                        System.out.println("Password: " + password);
                        System.out.println("Gender: " + gender);
                        System.out.println("Origin: " + origin);
                        Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                                "Confirming user credentials. You will be logged in shortly.", Snackbar.LENGTH_INDEFINITE).show();
                        createUser();
                        break;
                }
            }
        });

        // Cancel account creation
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Cancel Button Clicked");
                showWarningDialog(getCurrentFocus());
            }
        });

        // Select profile picture
        mProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        selectImage();
                    }
                } else {
                    selectImage();
                }
            }
        });
    }

    // Pick image
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICKER_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                            "Permission denied.", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICKER_CODE) {
            iconImage = data.getData();
            mProfileIcon.setImageURI(iconImage);
            imageSelected = true;
        }
    }

    // Check gender entry
    private void genderQuestionCheck() {
        switch (mGenderInput.getCheckedRadioButtonId()) {
            case R.id.rbCreateAccountGenderMale:
                gender = "Male";
                break;
            case R.id.rbCreateAccountGenderFemale:
                gender = "Female";
                break;
            case R.id.rbCreateAccountGenderOther:
                gender = "Other";
                break;
            case R.id.rbCreateAccountGenderDoNotDisclose:
                gender = "Did Not Disclose";
        }
    }

    // Check origin entry
    private void originQuestionCheck() {
        switch (mOriginInput.getCheckedRadioButtonId()) {
            case R.id.rbCreateAccountOriginTrue:
                origin = "True";
                break;
            case R.id.rbCreateAccountOriginFalse:
                origin = "False";
                break;
            case R.id.rbCreateAccountOriginDoNotDisclose:
                origin = "Did Not Disclose";
                break;
        }
    }

    // Check form entries
    private String entriesComplete() {
        String returnValue = "loading ...";
        username = mUsernameInput.getText().toString();
        email = mEmailInput.getText().toString();
        password = mPasswordInput.getText().toString();
        passwordConfirm = mConfirmPasswordInput.getText().toString();
        if (username.equals("") || email.equals("") || password.equals("") || passwordConfirm.equals("")) {
            returnValue = "incomplete-entry";
        } else if (username.length() < 8 || username.contains(" ") || username.toLowerCase().equals(username) || username.toUpperCase().equals(username)) {
            returnValue = "invalid-username";
        } else if (!email.contains("@")) {
            returnValue = "invalid-email";
        } else if (password.length() < 8 || password.contains(" ") || password.toLowerCase().equals(password) || password.toUpperCase().equals(password)) {
            returnValue = "invalid-password";
        } else if (!passwordConfirm.equals(password)) {
            returnValue = "passwords-dont-match";
        } else if (imageSelected == false ){
            returnValue = "no-icon";
        } else {
            returnValue = "entries-validated";
        }
        System.out.println("Validation Outcome: " + returnValue);
        return returnValue;
    }

    // Adds user in Firebase Authentication
    public void createUser() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                addUserDetails();
            }
        });
    }

    // Adds user document in Firebase Cloud Firestore
    public void addUserDetails() {
        firebaseStorage.getReference().child("profileicons/"+firebaseAuth.getCurrentUser().getUid()).putFile(iconImage);
        firebaseStorage.getReference().child("profileicons/"+firebaseAuth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                firebaseAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build());
                launchWelcomeActivity();
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("gender", gender);
        map.put("origin", origin);
        map.put("currentpoints", (int) 0);
        map.put("alltimepoints", (int) 0);
        map.put("alltimeposts", (int) 0);
        map.put("uniqueid", firebaseAuth.getCurrentUser().getUid());
        firebaseFS.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    // Warn user that any entered data will be wiped
    public void showWarningDialog(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you sure?");
        alert.setMessage("If you quit, any form entries you have made thus far will be deleted. If you decide against this later, " +
                "you will have to re-enter your details.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchLoginActivity();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    // Go to WelcomeActivity
    private void launchWelcomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    // Go to LoginActivity
    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}