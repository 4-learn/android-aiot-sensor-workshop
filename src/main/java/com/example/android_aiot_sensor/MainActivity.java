package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private TextView textViewSpeed, textViewFeedback;
    private ImageView imageViewExercise;
    private ProgressBar progressBar;
    private float maxSpeed = 0f; // 記錄最大開合跳速度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 元件
        textViewSpeed = findViewById(R.id.textViewSpeed);
        textViewFeedback = findViewById(R.id.textViewFeedback);
        imageViewExercise = findViewById(R.id.imageViewExercise);
        progressBar = findViewById(R.id.progressBar);

        // 設置運動圖片
        imageViewExercise.setImageResource(R.drawable.jumping_jack_image);

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

            // 計算當前開合跳的旋轉速度
            float currentSpeed = Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));

            // 更新最大速度
            if (currentSpeed > maxSpeed) {
                maxSpeed = currentSpeed;
                progressBar.setProgress((int) maxSpeed * 10); // 進度條顯示速度
            }

            // 更新 UI 顯示開合跳速度
            textViewSpeed.setText("當前速度: " + currentSpeed + " rad/s");

            // 根據速度給出反饋
            if (currentSpeed > 4.0f) {
                textViewFeedback.setText("開合跳速度很快！保持下去！");
            } else if (currentSpeed > 2.0f) {
                textViewFeedback.setText("速度不錯，可以再快些！");
            } else {
                textViewFeedback.setText("速度太慢，快點跳起來！");
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

