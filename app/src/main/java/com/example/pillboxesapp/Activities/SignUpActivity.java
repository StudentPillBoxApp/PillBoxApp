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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;


import com.example.pillboxesapp.Database.CallbackResult;
import com.example.pillboxesapp.Database.DatabaseService;
import com.example.pillboxesapp.R;
import com.example.pillboxesapp.Database.UserEntity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private final String Title = "Create Account";
    private FirebaseAuth firebaseAuthentication;
    private ProgressBar progressRing;
    private EditText nameBox;
    private EditText emailBox;
    private EditText passwordBox;
    private EditText confirmPasswordBox;
    private Switch isCarer;
    private Button signUpButton;
    private String userName;
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
        nameBox = findViewById(R.id.su_txtName);
        progressRing = findViewById(R.id.su_progressBar);
        emailBox = findViewById(R.id.su_txtEmail);
        passwordBox = findViewById(R.id.su_txtPassword);
        confirmPasswordBox = findViewById(R.id.su_txtRePassword);
        isCarer = findViewById(R.id.su_swtIsACarer);
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
    }

    private void createUser() {
        userName = nameBox.getText().toString();
        userEmail = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();
        String confirmPassword = confirmPasswordBox.getText().toString().trim();
        if (!validFields(password, confirmPassword)) {
            hideProgressRing();
            return;
        }
        if (!isDeviceConnectedToServer()) {
            hideProgressRing();
            Toast.makeText(SignUpActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
            return;
        }
        final Task<AuthResult> signUpTask = firebaseAuthentication.createUserWithEmailAndPassword(userEmail, password);
        signUpTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    addUserCollectionToDatabase();
                } else {
                    displayErrorMessage(signUpTask);
                }
            }
        });
        signUpTask.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                displayErrorMessage(signUpTask);
            }
        });
    }

    private boolean validFields(String password, String confirmPassword) {
        if (TextUtils.isEmpty(userName)) {
            nameBox.setError("Your name is required");
            return false;
        }
        if (TextUtils.isEmpty(userEmail)) {
            emailBox.setError(EmailChecker.EMAIL_EMPTY_ERROR);
            return false;
        }
        EmailChecker emailCheck = new EmailChecker(userEmail);
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
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            confirmPasswordBox.setError("Password must match");
            return false;
        }
        return true;
    }

    private boolean isDeviceConnectedToServer() {
        ConnectivityManager cm = (ConnectivityManager) SignUpActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return null != activeNetwork;
    }

    private void displayErrorMessage(Task<AuthResult> task) {
        hideProgressRing();
        Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
            UserEntity userEntity = new UserEntity(userId, userName, userEmail, isCarer.isChecked());
            dbService.addUser(userEntity, new CallbackResult() {
                @Override
                public void onCallback(Boolean result) {
                    hideProgressRing();
                    if (result) {
                        Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
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