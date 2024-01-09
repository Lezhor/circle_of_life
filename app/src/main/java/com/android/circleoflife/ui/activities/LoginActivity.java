package com.android.circleoflife.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.validators.StringValidator;
import com.android.circleoflife.ui.activities.categories.root.RootCategoriesActivity;
import com.android.circleoflife.ui.other.TextInputLayoutValidator;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Arrays;

public class LoginActivity extends SuperActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> login());

        TextView signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(v -> goToSignUpActivity());

        usernameInput = findViewById(R.id.edit_username);
        passwordInput = findViewById(R.id.edit_password);

        Log.d(TAG, "temp: " + "Test");
        new Thread(() -> Log.d(TAG, "temp: " + Arrays.stream(App.getResources().getStringArray(R.array.days_of_week)).reduce("", (a, b) -> a + b + "; ")))
                .start();

        //tryLoginWithLastDate(); todo - comment in

        executeInBackground(() -> App.getDatabaseController().getUserByUsername("john_doe"),
                user -> Log.d(TAG, "username: '" + user.getUsername() + "', password: '" + user.getPassword() + "'"));

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (App.getAuthentication().getUser() != null) {
            // someone authenticated
            User user = App.getAuthentication().getUser();
            App.getAuthentication().logout();
            Toast.makeText(this, "Logged out user '" + user.getUsername() + "'", Toast.LENGTH_SHORT).show();
        }

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
        String username = usernameInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        Log.d(TAG, "login: tries to login with username: " + username + ", password: " + password);
        if (
                TextInputLayoutValidator.validate(usernameInput, StringValidator::validateUsername, getString(R.string.username))
                & TextInputLayoutValidator.validate(passwordInput, StringValidator::validatePassword, getString(R.string.password))
        ) {
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


    private void goToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


}