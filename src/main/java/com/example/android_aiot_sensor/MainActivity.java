package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private TextView textViewSpeed, textViewFeedback;
    private float maxSpeed = 0f; // 記錄最大揮拳速度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 元件
        textViewSpeed = findViewById(R.id.textViewSpeed);
        textViewFeedback = findViewById(R.id.textViewFeedback);

        // 獲取 SensorManager 實例
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // 註冊陀螺儀感測器監聽器
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "陀螺儀感測器不可用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // 計算當前揮拳速度的絕對值（忽略方向）
            float currentSpeed = Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));

            // 更新最大速度
            if (currentSpeed > maxSpeed) {
                maxSpeed = currentSpeed;
            }

            // 更新 UI 顯示揮拳速度
            textViewSpeed.setText("當前速度: " + currentSpeed + " rad/s");

            // 根據速度給出反饋
            if (currentSpeed > 5.0f) {
                textViewFeedback.setText("速度非常快！保持這個節奏！");
            } else if (currentSpeed > 2.0f) {
                textViewFeedback.setText("速度中等，加快速度！");
            } else {
                textViewFeedback.setText("速度較慢，加油！");
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
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
