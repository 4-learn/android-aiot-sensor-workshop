package com.example.android_aiot_sensor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private Location startLocation, endLocation;
    private TextView textViewDistance;
    private Button buttonStart, buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 元素
        textViewDistance = findViewById(R.id.textViewDistance);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);

        // 獲取 LocationManager 實例
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 檢查位置權限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // "開始" 按鈕點擊事件
        buttonStart.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Toast.makeText(this, "開始位置已記錄", Toast.LENGTH_SHORT).show();
            }
        });

        // "停止" 按鈕點擊事件
        buttonStop.setOnClickListener(v -> {
            if (startLocation != null && endLocation != null) {
                float distance = startLocation.distanceTo(endLocation); // 計算兩點間距離
                textViewDistance.setText("Distance: " + distance + " meters");
                locationManager.removeUpdates(locationListener); // 停止位置更新
                Toast.makeText(this, "距離已計算", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "請先記錄起始位置和結束位置", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (startLocation == null) {
                startLocation = location; // 記錄起跑位置
            } else {
                endLocation = location; // 記錄結束位置
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 可在此處處理提供者狀態變更
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Toast.makeText(MainActivity.this, "GPS 已啟用", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Toast.makeText(MainActivity.this, "GPS 已禁用", Toast.LENGTH_SHORT).show();
        }
    };

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
}
