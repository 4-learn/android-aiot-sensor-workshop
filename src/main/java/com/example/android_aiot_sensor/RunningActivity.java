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
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import android.content.Intent;

import java.util.ArrayList;

public class RunningActivity extends Activity implements SensorEventListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private LocationManager locationManager;
    private Location startLocation, endLocation;
    private TextView textViewDistance, textViewSteps;
    private Button buttonStartMonitoring;
    private int stepCount = 0;
    private boolean isMonitoring = false;
    private ArrayList<String> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        // 初始化 UI 元件
        textViewDistance = findViewById(R.id.textViewDistance);
        textViewSteps = findViewById(R.id.textViewSteps);
        buttonStartMonitoring = findViewById(R.id.buttonStartMonitoring);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 檢查位置權限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // 按鈕點擊事件
        buttonStartMonitoring.setOnClickListener(v -> {
            if (isMonitoring) {
                stopMonitoring();
                returnToDashboard();  // 在停止監控後返回 Dashboard
            } else {
                startMonitoring();
            }
        });
    }

    private void startMonitoring() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        buttonStartMonitoring.setText("停止監控");
        isMonitoring = true;
        events.add("開始監控");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            Toast.makeText(this, "請授予位置權限", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopMonitoring() {
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(locationListener);
        buttonStartMonitoring.setText("開始監控");
        isMonitoring = false;
        events.add("停止監控");
    }

    private void returnToDashboard() {
        Intent intent = new Intent(RunningActivity.this, DashboardActivity.class);
        intent.putStringArrayListExtra("eventHistory", events); // 傳遞事件記錄
        startActivity(intent);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float z = event.values[2];
            if (Math.abs(z) > 2) {
                stepCount++;
                textViewSteps.setText("步數: " + stepCount);
                events.add("步數增加: " + stepCount);
            }
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (startLocation == null) {
                startLocation = location;
                events.add("GPS: 開始位置記錄");
            } else {
                endLocation = location;
                float distance = startLocation.distanceTo(endLocation);
                textViewDistance.setText("距離: " + distance + " 米");
                events.add("距離更新: " + distance + " 米");
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                Toast.makeText(this, "無法獲取位置服務，請授予權限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
