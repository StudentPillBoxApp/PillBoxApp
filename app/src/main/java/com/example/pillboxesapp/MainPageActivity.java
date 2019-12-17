package com.example.pillboxesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainPageActivity extends AppCompatActivity {
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewMedicineInfoActivity();
            }
        });
    }


    public void openNewMedicineInfoActivity() {
        Intent intent = new Intent(this, NewMedicineInfoActivity.class);
        startActivity(intent);
    }
}
