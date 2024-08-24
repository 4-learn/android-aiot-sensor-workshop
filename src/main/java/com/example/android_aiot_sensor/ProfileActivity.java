package com.example.android_aiot_sensor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class ProfileActivity extends Activity {

    private EditText editTextName, editTextHeight, editTextWeight;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 初始化 UI 元件
        editTextName = findViewById(R.id.editTextName);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String name = editTextName.getText().toString();
        float height = Float.parseFloat(editTextHeight.getText().toString());
        float weight = Float.parseFloat(editTextWeight.getText().toString());

        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("height", height);
        intent.putExtra("weight", weight);
        setResult(RESULT_OK, intent);
        finish();
    }
}
