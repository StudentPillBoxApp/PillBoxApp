package com.example.pillboxesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private final String Title = "Create Account";
    private FirebaseAuth firebaseAuthentication;
    private ProgressBar progressRing;
    private EditText emailBox;
    private EditText passwordBox;
    private EditText confirmPasswordBox;
    private Switch isCarer;
    private Switch hasCarer;
    private Button signUpButton;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle(Title);
        firebaseAuthentication = FirebaseAuth.getInstance();
        assignWidgets();
        createWidgetListeners();
    }

    private void assignWidgets() {
        progressRing = findViewById(R.id.su_progressBar);
        emailBox = findViewById(R.id.su_txtEmail);
        passwordBox = findViewById(R.id.su_txtPassword);
        confirmPasswordBox = findViewById(R.id.su_txtRePassword);
        isCarer = findViewById(R.id.su_swtIsACarer);
        hasCarer = findViewById(R.id.su_swtHasACarer);
        signUpButton = findViewById(R.id.su_btnSignUp);
    }

    private void createWidgetListeners() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                showProgressRing();
                createUser();
            }
        });
        isCarer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && hasCarer.isChecked()) {
                    hasCarer.setChecked(false); // Makes sure 'is a carer' and 'has a carer' options can't both be TRUE
                }
            }
        });
        hasCarer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && isCarer.isChecked()) {
                    isCarer.setChecked(false); // Makes sure 'is a carer' and 'has a carer' options can't both be TRUE
                }
            }
        });
    }

    private void createUser() {
        userEmail = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();
        String confirmPassword = confirmPasswordBox.getText().toString().trim();
        if (!validFields(password, confirmPassword)) {
            return;
        }
        firebaseAuthentication.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    addUserCollectionToDatabase();
                } else {
                    hideProgressRing();
                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    // TODO: Fix null-pointer exception.
                }
            }
        });
    }

    private boolean validFields(String password, String confirmPassword) {
        if (TextUtils.isEmpty(userEmail)) {
            emailBox.setError(emailChecker.EMAIL_EMPTY_ERROR);
            return false;
        }
        emailChecker emailCheck = new emailChecker(userEmail);
        if (!emailCheck.isEmailAddressValid()) {
            emailBox.setError(emailChecker.EMAIL_FORMAT_ERROR);
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordBox.setError("Password required");
            return false;
        }
        if (password.length() < 6) {
            passwordBox.setError("Password must have at least 6 characters");
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            confirmPasswordBox.setError("Password must match");
            return false;
        }
        return true;
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void showProgressRing() {
        progressRing.setVisibility(View.VISIBLE);
    }

    private void hideProgressRing() {
        progressRing.setVisibility(View.INVISIBLE);
    }

    private void addUserCollectionToDatabase() {
        final DatabaseService dbService = new DatabaseService();
        FirebaseUser newUser = firebaseAuthentication.getCurrentUser();
        if (newUser != null) {
            String userId = firebaseAuthentication.getCurrentUser().getUid();
            UserEntity userEntity = new UserEntity(userId, userEmail, isCarer.isChecked(), hasCarer.isChecked());
            dbService.addUser(userEntity, new CallbackResult() {
                @Override
                public void onCallback(Boolean result) {
                    hideProgressRing();
                    if (result) {
                        Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                        openHomeActivity();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Error: User details could not be added to the database.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void openHomeActivity() {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
        finish();
    }
}