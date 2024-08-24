package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private ImageView compassImageView;
    private ProgressBar progressBarPitch;
    private TextView textViewWarning;
    private Button buttonStartStop;
    private boolean isMonitoring = false;

    private float lastAzimuth = 0;
    private int turnaroundCount = 0;
    private static final int TURNAROUND_THRESHOLD = 170; // 定義180度轉向的閾值
    private static final int TARGET_TURNAROUNDS = 10; // 目標折返跑次數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 元件
        compassImageView = findViewById(R.id.compassImageView);
        progressBarPitch = findViewById(R.id.progressBarPitch);
        textViewWarning = findViewById(R.id.textViewWarning);
        buttonStartStop = findViewById(R.id.buttonStartStop);

        // 獲取 SensorManager 實例
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // 設置按鈕點擊事件
        buttonStartStop.setOnClickListener(v -> {
            if (isMonitoring) {
                stopMonitoring();
            } else {
                startMonitoring();
            }
        });
    }

    private void startMonitoring() {
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        buttonStartStop.setText("停止監控");
        isMonitoring = true;
        turnaroundCount = 0; // 開始監控時重置折返跑次數
    }

    private void stopMonitoring() {
        sensorManager.unregisterListener(this);
        buttonStartStop.setText("開始監控");
        textViewWarning.setVisibility(View.GONE);
        progressBarPitch.setProgress(0);
        isMonitoring = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // 創建旋轉矩陣
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            // 轉換旋轉矩陣為方向數據（俯仰角、滾動角、方位角）
            float[] orientationValues = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientationValues);

            // 方向數據的角度變為度數（從弧度轉換為度數）
            float azimuth = (float) Math.toDegrees(orientationValues[0]); // 方位角
            float pitch = (float) Math.toDegrees(orientationValues[1]);   // 俯仰角

            // 更新指南針圖標的旋轉
            compassImageView.setRotation(-azimuth);

            // 更新進度條來顯示俯仰角
            progressBarPitch.setProgress(Math.abs((int) pitch));

            // 當俯仰角超過 30 度時顯示警告
            if (Math.abs(pitch) > 30) {
                textViewWarning.setVisibility(View.VISIBLE);
            } else {
                textViewWarning.setVisibility(View.GONE);
            }

            // 判斷折返跑
            if (Math.abs(azimuth - lastAzimuth) > TURNAROUND_THRESHOLD) {
                turnaroundCount++;
                Toast.makeText(this, "已完成 " + turnaroundCount + " 次折返", Toast.LENGTH_SHORT).show();
                lastAzimuth = azimuth; // 更新上一次的方位角
            }

            // 檢查是否達到目標折返跑次數
            if (turnaroundCount >= TARGET_TURNAROUNDS) {
                Toast.makeText(this, "折返跑已完成！", Toast.LENGTH_LONG).show();
                stopMonitoring(); // 停止監控
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 當感測器的精度改變時，可以在這裡處理（本示例中不需要處理）
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMonitoring) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isMonitoring) {
            sensorManager.unregisterListener(this);
        }
    }
}
