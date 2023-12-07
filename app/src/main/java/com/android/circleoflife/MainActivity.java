package com.android.circleoflife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.circleoflife.application.App;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_getting_started);
        button.setOnClickListener(v -> Toast.makeText(this, "under construction", Toast.LENGTH_SHORT).show());

        /**
        App.setApplicationContext(this);

        Log.d(TAG, "onCreate: " + "Test");
        Log.d(TAG, "onCreate: " + Arrays.stream(App.getResources().getStringArray(R.array.days_of_week)).reduce("", (a, b) -> a + b + "; "));
         */
    }
}