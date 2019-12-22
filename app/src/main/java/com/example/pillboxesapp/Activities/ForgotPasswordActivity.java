package com.example.pillboxesapp.Activities;

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

import com.example.pillboxesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private final String TITLE = "Forgotten Password";
    private ProgressBar progressRing;
    private EditText emailBox;
    private Button emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.setTitle(TITLE);
        assignWidgets();
        createSendEmailListener();
    }

    private void assignWidgets() {
        progressRing = findViewById(R.id.fp_progressBar);
        emailButton = findViewById(R.id.fp_btnSendEmail);
        emailBox = findViewById(R.id.fp_txtEmail);
    }

    private void createSendEmailListener() {
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                showProgressRing();
                sendResetLinkEmail();
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

    private void sendResetLinkEmail() {
        String email = emailBox.getText().toString().trim();
        if (!validFields(email)) {
            return;
        }
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseAuthentication.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideProgressRing();
                    Toast.makeText(ForgotPasswordActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                } else {
                    hideProgressRing();
                    Toast.makeText(ForgotPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    // TODO: Fix null-pointer exception.
                }
            }
        });
    }

    private boolean validFields(String email) {
        if (TextUtils.isEmpty(email)) {
            emailBox.setError(EmailChecker.EMAIL_EMPTY_ERROR);
            return false;
        }
        EmailChecker emailCheck = new EmailChecker(email);
        if (!emailCheck.isEmailAddressValid()) {
            emailBox.setError(EmailChecker.EMAIL_FORMAT_ERROR);
            return false;
        }
        return true;
    }
}
