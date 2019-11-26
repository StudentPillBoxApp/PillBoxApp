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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuthentication;
    private ProgressBar progressRing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuthentication = FirebaseAuth.getInstance();
        if (firebaseAuthentication.getCurrentUser() != null) {
            openHomeActivity();
        }
        createButtonListeners();
    }

    private void createButtonListeners() {
        Button loginButton = findViewById(R.id.btnSignIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                attemptUserLogin();
            }
        });
        TextView newUserLink = findViewById(R.id.txtNewUser);
        newUserLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });
        TextView forgotPasswordLink = findViewById(R.id.txtForgotPassword);
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPasswordActivity();
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void attemptUserLogin() {
        EditText emailBox = findViewById(R.id.txtEmail);
        EditText passwordBox = findViewById(R.id.txtPassword);
        String email = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailBox.setError(emailChecker.EMAIL_EMPTY_ERROR);
            return;
        }
        emailChecker emailCheck = new emailChecker(email);
        if (!emailCheck.isEmailAddressValid()) {
            emailBox.setError(emailChecker.EMAIL_FORMAT_ERROR);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordBox.setError("Password required");
            return;
        }
        if (password.length() < 6) {
            passwordBox.setError("Password must have at least 6 characters");
            return;
        }
        progressRing = findViewById(R.id.progressBar);
        showProgressRing();
        firebaseAuthentication.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    openHomeActivity();
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    hideProgressRing();
                }
            }
        });
    }

    private void showProgressRing() {
        progressRing.setVisibility(View.VISIBLE);
    }

    private void hideProgressRing() {
        progressRing.setVisibility(View.INVISIBLE);
    }

    private void openSignUpActivity() {
        Intent intentNewUser = new Intent(this, SignUpActivity.class);
        startActivity(intentNewUser);
    }

    private void openForgotPasswordActivity() {
        Intent intentForgotPassword = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intentForgotPassword);
    }

    private void openHomeActivity() {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
        finish(); // stops the app from going back
    }
}
