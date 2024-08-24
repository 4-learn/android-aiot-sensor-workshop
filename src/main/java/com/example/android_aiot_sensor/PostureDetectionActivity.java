package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;

public class PostureDetectionActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor, gravitySensor, gyroscopeSensor;
    private TextView textViewStep, textViewPosture;
    private int stepCount = 0;
    private boolean isPostureCorrect = true;
    private ArrayList<String> eventHistory = new ArrayList<>();  // 用於存儲歷史事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture_detection);

        // 初始化 UI 元件
        textViewStep = findViewById(R.id.textViewStep);
        textViewPosture = findViewById(R.id.textViewPosture);

        // 獲取 SensorManager 實例
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // 註冊感測器監聽器
        startPostureDetection();
    }

    private void startPostureDetection() {
        // 註冊加速度計、重力感測器和陀螺儀監聽器
        if (accelerometerSensor != null && gravitySensor != null && gyroscopeSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this, "姿勢檢測開始", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "感測器不可用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // 簡單步數計算
            if (Math.abs(z) > 5) {
                stepCount++;
                textViewStep.setText("步數: " + stepCount);
                eventHistory.add("步數增加: " + stepCount);  // 添加事件到歷史紀錄
            }
        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float gx = event.values[0];
            float gy = event.values[1];
            float gz = event.values[2];

            // 判定姿勢是否正確
            if (gz < 9.8 && gz > 9.0) { // 設定適當的範圍來判定姿勢
                if (!isPostureCorrect) {
                    isPostureCorrect = true;
                    textViewPosture.setText("姿勢正確");
                    eventHistory.add("姿勢正確");  // 添加事件到歷史紀錄
                }
            } else {
                if (isPostureCorrect) {
                    isPostureCorrect = false;
                    textViewPosture.setText("姿勢不正確，請調整");
                    Toast.makeText(this, "姿勢不正確！", Toast.LENGTH_SHORT).show();
                    eventHistory.add("姿勢不正確");  // 添加事件到歷史紀錄
                }
            }
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // 檢測陀螺儀數據，確保沒有過度旋轉
            if (Math.abs(x) > 2.0 || Math.abs(y) > 2.0) {
                textViewPosture.setText("過度旋轉！請調整姿勢");
                Toast.makeText(this, "過度旋轉！請調整姿勢", Toast.LENGTH_SHORT).show();
                eventHistory.add("過度旋轉");  // 添加事件到歷史紀錄
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 當感測器精度改變時處理（此範例中無需實現）
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometerSensor != null && gravitySensor != null && gyroscopeSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // 返回主頁面時傳遞事件記錄
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("eventHistory", eventHistory);
        setResult(RESULT_OK, intent);
        finish();
    }
}
