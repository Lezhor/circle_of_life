package com.android.circleoflife.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.ui.activities.categories.RootCategoriesActivity;

import java.util.Arrays;

public class MainActivity extends SuperActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_getting_started);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootCategoriesActivity.class);
            startActivity(intent);
        });

        Log.d(TAG, "temp: " + "Test");
        new Thread(() -> Log.d(TAG, "temp: " + Arrays.stream(App.getResources().getStringArray(R.array.days_of_week)).reduce("", (a, b) -> a + b + "; ")))
                .start();

    }
}