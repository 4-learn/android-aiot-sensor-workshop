package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView mTextViewX;
    private TextView mTextViewY;
    private TextView mTextViewZ;
    private TextView mStepCountView;
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 ImageView 和 TextView，用於顯示加速度計數據和步數
        ImageView runnerImageView = findViewById(R.id.runnerImageView);
        mTextViewX = findViewById(R.id.textViewX);
        mTextViewY = findViewById(R.id.textViewY);
        mTextViewZ = findViewById(R.id.textViewZ);
        mStepCountView = findViewById(R.id.stepCountView);

        // 設置 ImageView 以顯示 WebP 圖片
        runnerImageView.setImageResource(R.drawable.runner);

        // 初始化顯示的初始值
        mTextViewX.setText("X: 0.0");
        mTextViewY.setText("Y: 0.0");
        mTextViewZ.setText("Z: 0.0");
        mStepCountView.setText("Steps: 0");

        // 獲取 SensorManager 實例
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // 獲取加速度計感測器
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // 註冊感測器監聽器，監聽加速度計數據變化
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 當 Activity 重新啟動時，重新註冊感測器監聽器
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 當 Activity 暫停時，解除感測器監聽器的註冊
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 當加速度計數據變化時調用，處理加速度計數據
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // 更新 TextView 顯示加速度計數據
            mTextViewX.setText("X: " + x);
            mTextViewY.setText("Y: " + y);
            mTextViewZ.setText("Z: " + z);

            // 假設當 Z 軸的變化超過某個閾值時計數為一步
            if (Math.abs(z) > 10) { // 閾值可以根據需求調整
                stepCount++;
                mStepCountView.setText("Steps: " + stepCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 在感測器精度變化時調用（本範例中不需要處理）
    }
}
