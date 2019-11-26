package com.example.pillboxesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private final String Title = "Forgotten Password";
    private ProgressBar progressRing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.setTitle(Title);
        createSendEmailListener();
        progressRing = findViewById(R.id.progressBar);
    }

    private void createSendEmailListener() {
        Button emailButton = findViewById(R.id.btnSendEmail);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                showProgressRing();
                sendResetLinkEmail();
            }
        });
    }

    private void sendResetLinkEmail() {
        EditText emailBox = findViewById(R.id.txtEmail);
        String email = emailBox.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailBox.setError(emailChecker.EMAIL_EMPTY_ERROR);
            return;
        }
        emailChecker emailCheck = new emailChecker(email);
        if (!emailCheck.isEmailAddressValid()) {
            emailBox.setError(emailChecker.EMAIL_FORMAT_ERROR);
            return;
        }

        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseAuthentication.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                    hideProgressRing();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
