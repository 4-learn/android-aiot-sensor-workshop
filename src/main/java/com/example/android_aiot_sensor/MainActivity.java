package com.example.android_aiot_sensor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int RUNNING_REQUEST_CODE = 1;
    private static final int POSTURE_REQUEST_CODE = 2;

    private Button btnDashboard, btnRunning, btnPostureDetection, btnSetProfile;
    private ArrayList<String> eventHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 元件
        btnDashboard = findViewById(R.id.btn_dashboard);
        btnRunning = findViewById(R.id.btn_running);
        btnSetProfile = findViewById(R.id.btn_set_profile);

        // 設定按鈕的點擊事件
        btnDashboard.setOnClickListener(v -> openDashboard());
        btnRunning.setOnClickListener(v -> openRunningActivity());
        btnSetProfile.setOnClickListener(v -> openProfileSettings());
    }

    private void openDashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.putStringArrayListExtra("eventHistory", eventHistory);
        startActivity(intent);
    }

    private void openRunningActivity() {
        Intent intent = new Intent(MainActivity.this, RunningActivity.class);
        startActivityForResult(intent, RUNNING_REQUEST_CODE);
    }

    private void openProfileSettings() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == RUNNING_REQUEST_CODE || requestCode == POSTURE_REQUEST_CODE) && resultCode == RESULT_OK && data != null) {
            ArrayList<String> newEvents = data.getStringArrayListExtra("eventHistory");
            if (newEvents != null) {
                eventHistory.addAll(newEvents);
            }
        }
    }
}
