package com.android.circleoflife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_getting_started);
        button.setOnClickListener(v -> Toast.makeText(this, "under construction", Toast.LENGTH_SHORT).show());
    }
}