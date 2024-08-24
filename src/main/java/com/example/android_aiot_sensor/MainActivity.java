package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;

public class MainActivity extends Activity implements SensorEventListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private LocationManager locationManager;
    private Location startLocation, endLocation;
    private TextView mStatusTextView;
    private int stepCount = 0;
    private Button buttonStart, buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 ImageView 和 TextView，用於顯示狀態和圖片
        ImageView runnerImageView = findViewById(R.id.runnerImageView);
        mStatusTextView = findViewById(R.id.statusTextView);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);

        // 設置 ImageView 以顯示 WebP 圖片
        runnerImageView.setImageResource(R.drawable.runner);

        // 獲取 SensorManager 和 LocationManager 實例
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 檢查和請求位置權限
        checkLocationPermission();

        // 按鈕事件
        buttonStart.setOnClickListener(v -> {
            startLocation = null;
            endLocation = null;
            stepCount = 0;
            mStatusTextView.setText("開始慢跑");

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "請授予位置權限", Toast.LENGTH_SHORT).show();
            }
        });

        buttonStop.setOnClickListener(v -> {
            locationManager.removeUpdates(locationListener);
            mSensorManager.unregisterListener(this);
            if (startLocation != null && endLocation != null) {
                float distance = startLocation.distanceTo(endLocation);
                mStatusTextView.setText("距離: " + distance + "米\n步數: " + stepCount);
            } else {
                mStatusTextView.setText("未能計算距離");
            }
        });
    }

    private void checkLocationPermission() {
        // 檢查權限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 權限未授予，請求權限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "權限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "權限被拒絕", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (startLocation == null) {
                startLocation = location; // 記錄起跑位置
            } else {
                endLocation = location; // 記錄結束位置
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float z = event.values[2];
            if (Math.abs(z) > 10) { // 閾值可以根據需求調整
                stepCount++;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
