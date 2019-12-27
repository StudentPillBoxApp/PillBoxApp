package com.example.pillboxesapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pillboxesapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private final int btEnableCode = 1;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupTabs();
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
        if (requestCode == btEnableCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(HomeActivity.this, "Bluetooth enabled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Bluetooth not enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
