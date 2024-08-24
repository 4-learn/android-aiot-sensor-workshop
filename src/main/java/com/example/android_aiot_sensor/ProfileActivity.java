package com.example.android_aiot_sensor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileActivity extends Activity {

    private EditText editTextHeight, editTextWeight;
    private Button buttonSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile);

        buttonSaveProfile.setOnClickListener(v -> {
            String heightStr = editTextHeight.getText().toString();
            String weightStr = editTextWeight.getText().toString();

            if (!heightStr.isEmpty() && !weightStr.isEmpty()) {
                float height = Float.parseFloat(heightStr);
                float weight = Float.parseFloat(weightStr);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("height", height);
                resultIntent.putExtra("weight", weight);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(ProfileActivity.this, "請輸入所有資料", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
