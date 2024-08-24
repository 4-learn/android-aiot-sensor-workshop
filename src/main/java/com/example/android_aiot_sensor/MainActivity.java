package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private TextView textViewX, textViewY, textViewZ;
    private ImageView postureImageView;
    private boolean isInCorrectPosture = false;

    // 設置初始姿勢的範圍值
    private static final float INITIAL_X_MIN = -0.1f;
    private static final float INITIAL_X_MAX = 0.3f;
    private static final float INITIAL_Y_MIN = 9.7f;
    private static final float INITIAL_Y_MAX = 12f;
    private static final float INITIAL_Z_MIN = -0.1f;
    private static final float INITIAL_Z_MAX = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 元件
        textViewX = findViewById(R.id.textViewX);
        textViewY = findViewById(R.id.textViewY);
        textViewZ = findViewById(R.id.textViewZ);
        postureImageView = findViewById(R.id.postureImageView);

        // 設置起始姿勢圖片
        postureImageView.setImageResource(R.drawable.starting_pose_image);

        // 獲取 SensorManager 實例
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        if (gravitySensor != null) {
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "重力感測器不可用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // 更新 UI 顯示重力感測器數據
            textViewX.setText("X: " + x);
            textViewY.setText("Y: " + y);
            textViewZ.setText("Z: " + z);

            // 寬鬆的姿勢判斷邏輯
            if (x >= INITIAL_X_MIN && x <= INITIAL_X_MAX &&
                    y >= INITIAL_Y_MIN && y <= INITIAL_Y_MAX &&
                    z >= INITIAL_Z_MIN && z <= INITIAL_Z_MAX) {
                if (!isInCorrectPosture) {
                    isInCorrectPosture = true;
                    Toast.makeText(this, "姿勢正確，開始偵測動作！", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (isInCorrectPosture) {
                    isInCorrectPosture = false;
                    Toast.makeText(this, "請調整到正確的起始姿勢", Toast.LENGTH_SHORT).show();
                }
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
        if (gravitySensor != null) {
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
