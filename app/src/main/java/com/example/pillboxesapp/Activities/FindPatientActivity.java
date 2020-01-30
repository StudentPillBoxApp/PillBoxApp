package com.example.pillboxesapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pillboxesapp.Database.CallbackResult;
import com.example.pillboxesapp.Database.DatabaseService;
import com.example.pillboxesapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

public class FindPatientActivity extends AppCompatActivity {

    private final String TITLE = "Find Patient";
    private ProgressBar progressRing;
    private EditText emailBox;
    private Button searchButton;
    private FloatingActionButton addUserButton;
    private TextView resultText;
    private String patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_patient);
        this.setTitle(TITLE);
        patientID = "";
        assignWidgets();
        createButtonListeners();
    }

    private void assignWidgets() {
        progressRing = findViewById(R.id.find_p_progressBar);
        searchButton = findViewById(R.id.find_p_btnFindPatient);
        emailBox = findViewById(R.id.find_p_txtEmail);
        resultText = findViewById(R.id.find_p_txtResult);
        addUserButton = findViewById(R.id.find_p_btnAddUser);
    }

    private void createButtonListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                showProgressRing();
                findPatient();
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                showProgressRing();
                linkPatientToCarer();
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

    private void findPatient() {
        String email = emailBox.getText().toString().trim();
        if (!validFields(email)) {
            return;
        }
        DatabaseService dbService = new DatabaseService();
        dbService.findPatientByEmail(email, new CallbackResult() {
            @Override
            public void onCallback(String[] result) {
                hideProgressRing();
                String[] userDetails = result;
                patientID = userDetails[0];
                String name = userDetails[1];
                resultText.setText(name);
                resultText.setVisibility(View.VISIBLE);
                addUserButton.show();
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

    private void linkPatientToCarer() {
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        String userID = firebaseAuthentication.getCurrentUser().getUid();
        DatabaseService dbService = new DatabaseService();
        dbService.linkPatientAndCarer(patientID, userID, new CallbackResult() {
            @Override
            public void onCallback(String[] result) {
                hideProgressRing();
            }
        });
    }
}
