package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    // Disable action bar back button
    @Override
    public void onBackPressed() {
    }

    // Declaration of variables
    private EditText mUsernameInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mConfirmPasswordInput;
    private RadioGroup mOriginInput;
    private RadioGroup mGenderInput;
    private ImageButton mCreateAccountButton;
    private ImageButton mCancelButton;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseDB;

    String username = "";
    String email = "";
    String password = "";
    String passwordConfirm = "";
    String origin = "";
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Assigning of variable values
        mCreateAccountButton = findViewById(R.id.ibCreateAccountContinue);
        mCancelButton = findViewById(R.id.ibCreateAccountCancel);
        mUsernameInput = findViewById(R.id.etCreateAccountUsername);
        mEmailInput = findViewById(R.id.etCreateAccountEmail);
        mPasswordInput = findViewById(R.id.etCreateAccountPassword);
        mConfirmPasswordInput = findViewById(R.id.etCreateAccountConfirmPassword);
        mOriginInput = findViewById(R.id.rgCreateAccountOrigin);
        mGenderInput = findViewById(R.id.rgCreateAccountGender);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();

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
                    case "entries-validated":
                        genderQuestionCheck();
                        originQuestionCheck();
                        System.out.println("Username: " + username);
                        System.out.println("Email: " + email);
                        System.out.println("Password: " + password);
                        System.out.println("Gender: " + gender);
                        System.out.println("Origin: " + origin);
                        Snackbar.make(findViewById(R.id.clCreateAccountMainLayout),
                                "Confirming user credentials. You will be logged in shortly.", Snackbar.LENGTH_SHORT).show();
                        createUser();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addUserDetails();
                                launchWelcomeActivity();
                            }
                        }, 2500);
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
            }
        });
    }

    // Adds user document in Firebase Cloud Firestore
    public void addUserDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("gender", gender);
        map.put("origin", origin);
        map.put("currentpoints", (int) 0);
        map.put("alltimepoints", (int) 0);
        firebaseDB.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(map)
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