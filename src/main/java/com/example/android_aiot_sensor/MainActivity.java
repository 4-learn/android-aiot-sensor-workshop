package com.example.android_aiot_sensor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int PROFILE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDashboard = findViewById(R.id.btn_dashboard);
        Button btnHome = findViewById(R.id.btn_home);
        Button btnSetProfile = findViewById(R.id.btn_set_profile);

        // 設置按鈕事件
        btnDashboard.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "功能尚在開發中", Toast.LENGTH_SHORT).show();
        });

        btnHome.setOnClickListener(v -> {
            // 開啟 RunningActivity 來顯示 GPS 慢跑 APP 的功能
            Intent intent = new Intent(MainActivity.this, RunningActivity.class);
            startActivity(intent);
        });

        btnSetProfile.setOnClickListener(v -> {
            // 開啟 ProfileActivity 來設定使用者資料
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivityForResult(intent, PROFILE_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // 可以處理從 ProfileActivity 返回的數據，例如更新使用者資料
            float userHeight = data.getFloatExtra("height", 0);
            float userWeight = data.getFloatExtra("weight", 0);
            // 此處可以將身高體重保存到 SharedPreferences 或類似的儲存位置
        }
    }
}