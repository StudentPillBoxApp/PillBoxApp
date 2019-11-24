package com.example.pillboxesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ForgotPasswordActivity extends AppCompatActivity {
    private final String Title = "Forgotten Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.setTitle(Title);
    }
}
