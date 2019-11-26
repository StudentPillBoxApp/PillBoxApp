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

public class SignUpActivity extends AppCompatActivity {
    private final String Title = "Create Account";
    private ProgressBar progressRing;
    private EditText emailBox;
    private EditText passwordBox;
    private EditText confirmPasswordBox;
    private Switch isCarer;
    private Switch hasCarer;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle(Title);
        assignWidgets();
        createWidgetListeners();
    }

    private void assignWidgets() {
        progressRing = findViewById(R.id.progressBar);
        emailBox = findViewById(R.id.txtEmail);
        passwordBox = findViewById(R.id.txtPassword);
        confirmPasswordBox = findViewById(R.id.txtRePassword);
        isCarer = findViewById(R.id.swtIsACarer);
        hasCarer = findViewById(R.id.swtHasACarer);
        signUpButton = findViewById(R.id.btnSignUp);
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
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        String email = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();
        String confirmPassword = confirmPasswordBox.getText().toString().trim();
        if (!validFields(email, password, confirmPassword)) {
            return;
        }
        firebaseAuthentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    hideProgressRing();
                    Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                    openHomeActivity();
                } else {
                    hideProgressRing();
                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    // TODO: Fix null-pointer exception.
                }
            }
        });
    }

    private boolean validFields(String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(email)) {
            emailBox.setError(emailChecker.EMAIL_EMPTY_ERROR);
            return false;
        }
        emailChecker emailCheck = new emailChecker(email);
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

    private void openHomeActivity() {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
        finish();
    }
}
