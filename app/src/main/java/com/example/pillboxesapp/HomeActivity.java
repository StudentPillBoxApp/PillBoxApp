package com.example.pillboxesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    private final int btEnableCode = 1;
    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;
    private static final UUID arduinoID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Arduino Unique Identifier.

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
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null) {
            if (!btAdapter.isEnabled()) {
                Intent btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(btEnableIntent, btEnableCode);
            }
        } else {
            Toast.makeText(HomeActivity.this, "Bluetooth is not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == btEnableCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(HomeActivity.this, "Bluetooth enabled.", Toast.LENGTH_SHORT).show();
                connectToArduino();
            } else {
                Toast.makeText(HomeActivity.this, "Bluetooth not enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void connectToArduino() {
        String btAddress = btAdapter.getAddress();
        BluetoothDevice btDevice = btAdapter.getRemoteDevice(btAddress);
        try {
            btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(arduinoID);
            btSocket.connect();
            turnOnLED();
            Toast.makeText(HomeActivity.this, "Connected to pill box.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(HomeActivity.this, "Bluetooth connection failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void turnOnLED() {
        String ledOnCodeWord = "ON";
        try {
            if (btSocket != null) {
                btSocket.getOutputStream().write(ledOnCodeWord.getBytes());
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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