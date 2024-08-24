package com.example.android_aiot_sensor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private Button btnDashboard, btnHome, btnSetProfile;
    private ArrayList<String> eventHistory = new ArrayList<>();
    private static final int REQUEST_CODE_POSTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 元件
        btnDashboard = findViewById(R.id.btn_dashboard);
        btnHome = findViewById(R.id.btn_home);
        btnSetProfile = findViewById(R.id.btn_set_profile);

        // 設定按鈕的點擊事件
        btnDashboard.setOnClickListener(v -> openDashboard());
        btnHome.setOnClickListener(v -> openPostureDetection());
        btnSetProfile.setOnClickListener(v -> openProfileSettings());
    }

    private void openDashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.putStringArrayListExtra("eventHistory", eventHistory);  // 傳遞事件歷史紀錄
        startActivity(intent);
    }

    private void openPostureDetection() {
        Intent intent = new Intent(MainActivity.this, PostureDetectionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_POSTURE);
    }

    private void openProfileSettings() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_POSTURE && resultCode == RESULT_OK) {
            ArrayList<String> newEvents = data.getStringArrayListExtra("eventHistory");
            if (newEvents != null) {
                eventHistory.addAll(newEvents);  // 更新主頁面的事件歷史記錄
            }
        }
    }
}
