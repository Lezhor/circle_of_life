package com.android.circleoflife.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.validators.StringValidator;
import com.android.circleoflife.ui.activities.categories.root.RootCategoriesActivity;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends SuperActivity {

    private static final String TAG = "MainActivity";

    private EditText usernameEdit;
    private EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.login_button);
        button.setOnClickListener(v -> login());

        usernameEdit = findViewById(R.id.edit_username);
        passwordEdit = findViewById(R.id.edit_password);

        Log.d(TAG, "temp: " + "Test");
        new Thread(() -> Log.d(TAG, "temp: " + Arrays.stream(App.getResources().getStringArray(R.array.days_of_week)).reduce("", (a, b) -> a + b + "; ")))
                .start();

        tryLoginWithLastDate();

        executeInBackground(() -> App.getDatabaseController().getUserByUsername("john_doe"),
                user -> Log.d(TAG, "username: '" + user.getUsername() + "', password: '" + user.getPassword() + "'"));

    }

    /**
     * Tries to login with last login data, if succeeds, switches to next activity immediately
     */
    private void tryLoginWithLastDate() {
        executeInBackground(
                App.getAuthentication()::loginWithSavedLoginData,
                succeeded -> {
                    if (succeeded) {
                        goToNextActivity();
                    }
                }
        );
    }

    private void goToNextActivity() {
        // TODO: 09.01.2024 main menu should be here
        Intent intent = new Intent(this, RootCategoriesActivity.class);
        startActivity(intent);
    }

    private void login() {
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        Log.d(TAG, "login: tries to login with username: " + username + ", password: " + password);
        try {
            StringValidator.validateUsername(username);
            StringValidator.validatePassword(password);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        executeInBackground(
                () -> {
                    try {
                        if (App.getAuthentication().login(username, password)) {
                            return null;
                        } else {
                            return "Login failed";
                        }
                    } catch (IOException e) {
                        return "Login failed: " + e.getMessage();
                    }
                },
                str -> {
                    if (str == null) {
                        // login succeeded
                        if (App.getAuthentication().getUser() != null) {
                            goToNextActivity();
                        }
                    } else {
                        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

}