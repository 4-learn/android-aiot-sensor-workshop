package com.example.android_aiot_sensor;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private static final String TAG = "SensorInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 獲取 SensorManager 實例
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 建立一個 StringBuilder 來存儲感測器信息
        StringBuilder sensorInfo = new StringBuilder();

        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        // 遍歷所有感測器，並將每個感測器的信息添加到 StringBuilder 中
        for (Sensor sensor : sensorList) {
            sensorInfo.append("Name: ").append(sensor.getName())
                    .append("\nType: ").append(sensor.getType())
                    .append("\nVendor: ").append(sensor.getVendor())
                    .append("\nVersion: ").append(sensor.getVersion())
                    .append("\n\n");
        }

        Log.d(TAG, sensorInfo.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
