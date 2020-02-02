package com.example.pillboxesapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillboxesapp.Database.CallbackResult;
import com.example.pillboxesapp.Database.DatabaseService;
import com.example.pillboxesapp.Database.PillBoxEntity;
import com.example.pillboxesapp.MyApplication;
import com.example.pillboxesapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    private final int btEnableCode = 1;
    private ViewPager viewPager;
    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;
    private static final UUID arduinoID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupTabs();
        removeRedundantAlarms();
        enableBluetooth();
    }

    private void setupTabs() {
        viewPager = findViewById(R.id.view_pager);
        setupViewPager();
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScheduleFragment(), "Schedule");
        adapter.addFragment(new PillsFragment(), "Pills");
        adapter.addFragment(new AccountFragment(), "Account");
        viewPager.setAdapter(adapter);
    }

    protected void logoutProcess() {
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseAuthentication.signOut(); // TODO: Add completion and fail listeners
        Intent logoutIntent = new Intent(this, MainActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    private void removeRedundantAlarms() {
        DatabaseService dbService = new DatabaseService();
        dbService.removeRedundantAlarms(new CallbackResult() {
            @Override
            public void onCallback(Boolean result) {
                super.onCallback(result);
            }
        });
    }

    protected void getCurrentPillBox() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseService dbService = new DatabaseService();
        dbService.loadUsersPillBoxes(userID, new CallbackResult() {
            @Override
            public void onCallback(ArrayList results) {
                for (Object result : results) {
                    if (result instanceof PillBoxEntity) {
                        PillBoxEntity pillBox = (PillBoxEntity) result;
                        String pillBoxID = pillBox.getID();
                        int boxNum = pillBox.getBoxNum();
                        String boxTitle = "Pillbox " + Integer.toString(boxNum);
                        TextView pillBoxTitle = (TextView) findViewById(R.id.txtPillBoxNumber);
                        pillBoxTitle.setText(boxTitle);
                        Button pillBoxEdit = (Button) findViewById(R.id.pillBoxEditButton);
                        pillBoxEdit.setVisibility(View.VISIBLE);
                        MyApplication appState = ((MyApplication) getApplicationContext());
                        appState.setCurrentBoxID(pillBoxID);
                    }
                }
            }
        });
    }

    protected void openNewMedicineActivity() {
        Intent intentNewMedicine = new Intent(this, NewMedicineInfoActivity.class);
        startActivity(intentNewMedicine);
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
        String ledOnCodeWord = "a";
        try {
            if (btSocket != null) {
                btSocket.getOutputStream().write(ledOnCodeWord.getBytes());
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == btEnableCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(HomeActivity.this, "Bluetooth enabled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Bluetooth not enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}