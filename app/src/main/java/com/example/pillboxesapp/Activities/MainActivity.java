package com.example.pillboxesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillboxesapp.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuthentication;
    private ProgressBar progressRing;
    private Button loginButton;
    private TextView newUserLink;
    private TextView forgotPasswordLink;
    private EditText emailBox;
    private EditText passwordBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLoginStatus();
        assignWidgets();
        createButtonListeners();
    }

    private void checkLoginStatus() {
        firebaseAuthentication = FirebaseAuth.getInstance();
        if (firebaseAuthentication.getCurrentUser() != null) {
            openHomeActivity();
        }
    }

    private void openHomeActivity() {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
        finish(); // stops the app from going back
    }

    private void assignWidgets() {
        progressRing = findViewById(R.id.main_progressBar);
        loginButton = findViewById(R.id.main_btnSignIn);
        forgotPasswordLink = findViewById(R.id.main_txtForgotPassword);
        newUserLink = findViewById(R.id.main_txtNewUser);
        emailBox = findViewById(R.id.main_txtEmail);
        passwordBox = findViewById(R.id.main_txtPassword);
    }

    private void createButtonListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                showProgressRing();
                attemptUserLogin();
            }
        });
        newUserLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });
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

    private void attemptUserLogin() {
        String email = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();
        if (!validFields(email, password)) {
            hideProgressRing();
            return;
        }
        if (!isDeviceConnectedToServer()) {
            hideProgressRing();
            Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuthentication = FirebaseAuth.getInstance();
        final Task<AuthResult> loginTask = firebaseAuthentication.signInWithEmailAndPassword(email, password);
        loginTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    hideProgressRing();
                    Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    openHomeActivity();
                } else {
                    displayErrorMessage(loginTask);
                }
            }
        });
        loginTask.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                displayErrorMessage(loginTask);
            }
        });
    }

    private boolean validFields(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailBox.setError(EmailChecker.EMAIL_EMPTY_ERROR);
            return false;
        }
        EmailChecker emailCheck = new EmailChecker(email);
        if (!emailCheck.isEmailAddressValid()) {
            emailBox.setError(EmailChecker.EMAIL_FORMAT_ERROR);
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
        return true;
    }

    private boolean isDeviceConnectedToServer() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return null != activeNetwork;
    }

    private void displayErrorMessage(Task<AuthResult> task) {
        hideProgressRing();
        Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void openSignUpActivity() {
        Intent intentNewUser = new Intent(this, SignUpActivity.class);
        startActivity(intentNewUser);
    }

    private void openForgotPasswordActivity() {
        Intent intentForgotPassword = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intentForgotPassword);
    }
}