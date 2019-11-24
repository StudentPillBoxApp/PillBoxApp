package com.example.pillboxesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createButtonListeners();
    }

    private void createButtonListeners() {
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

    private void openSignUpActivity(){
        Intent intentNewUser = new Intent (this, SignUpActivity.class);
        startActivity(intentNewUser);
    }
    private void openForgotPasswordActivity(){
        Intent intentForgotPassword = new Intent (this, ForgotPasswordActivity.class);
        startActivity(intentForgotPassword);
    }

}
