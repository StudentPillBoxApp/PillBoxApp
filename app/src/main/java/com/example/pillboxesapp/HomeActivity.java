package com.example.pillboxesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private final int btEnableCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createButtonListeners();
        enableBluetooth();
    }

    private void createButtonListeners() {
        Button logout = findViewById(R.id.home_btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(btEnableIntent, btEnableCode);
            }
        } else {
            Toast.makeText(HomeActivity.this, "Bluetooth is not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == btEnableCode){
            if (resultCode == RESULT_OK){
                Toast.makeText(HomeActivity.this, "Bluetooth enabled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Bluetooth not enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logout() {
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseAuthentication.signOut(); // TODO: Add completion and fail listeners
        Intent logoutIntent = new Intent(this, MainActivity.class);
        startActivity(logoutIntent);
        finish();
    }

}
