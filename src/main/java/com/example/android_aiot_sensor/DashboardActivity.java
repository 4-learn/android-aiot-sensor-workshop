package com.example.android_aiot_sensor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;

public class DashboardActivity extends Activity {

    private TextView textViewEventLog;
    private ArrayList<String> eventHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 初始化 UI 元件
        textViewEventLog = findViewById(R.id.textViewEventLog);

        // 檢查是否有事件歷史記錄
        eventHistory = getIntent().getStringArrayListExtra("eventHistory");

        if (eventHistory != null && !eventHistory.isEmpty()) {
            StringBuilder logBuilder = new StringBuilder();
            for (String event : eventHistory) {
                logBuilder.append(event).append("\n");
            }
            textViewEventLog.setText(logBuilder.toString());
        } else {
            textViewEventLog.setText("沒有記錄到事件。");
        }
    }
}
