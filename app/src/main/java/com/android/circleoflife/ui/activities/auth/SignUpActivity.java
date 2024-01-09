package com.android.circleoflife.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.validators.StringValidator;
import com.android.circleoflife.ui.activities.SuperActivity;
import com.android.circleoflife.ui.activities.categories.root.RootCategoriesActivity;
import com.android.circleoflife.ui.other.TextInputLayoutValidator;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class SignUpActivity extends SuperActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();

    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;
    private Switch switchServerSync;
    private Switch switchAutoSync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(v -> signUp());

        TextView loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> goToLoginActivity());

        usernameInput = findViewById(R.id.edit_username);
        passwordInput = findViewById(R.id.edit_password);

        switchServerSync = findViewById(R.id.server_sync_switch);
        switchAutoSync = findViewById(R.id.automatic_syncing_switch);

        switchServerSync.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchAutoSync.isChecked() != isChecked) {
                switchAutoSync.setChecked(isChecked);
            }
        });

    }

    private void signUp() {
        String username = usernameInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        boolean serverSync = switchServerSync.isChecked();
        boolean autoSync = switchAutoSync.isChecked();
        Log.d(TAG, "login: tries to signUp username: " + username + ", password: " + password);
        if (
                TextInputLayoutValidator.validate(usernameInput, StringValidator::validateUsername, getString(R.string.username))
                        & TextInputLayoutValidator.validate(passwordInput, StringValidator::validatePassword, getString(R.string.password))
        ) {
            executeInBackground(
                    () -> {
                        try {
                            if (App.getAuthentication().signUp(username, password, !serverSync)) {
                                return null;
                            } else {
                                return "Username already exists";
                            }
                        } catch (IOException e) {
                            return "SignUp failed: " + e.getMessage();
                        }
                    },
                    str -> {
                        if (str == null) {
                            // signUp succeeded
                            if (App.getAuthentication().getUser() != null) {
                                App.getAuthentication().getSettings().setAutomaticServerSync(autoSync);
                                goToNextActivity();
                            } else {
                                Log.i(TAG, "signUp: str is null but user is not authenticated!");
                            }
                        } else if (str.equals("Username already exists")) {
                            usernameInput.setError(str);
                        } else {
                            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void goToNextActivity() {
        Intent intent = new Intent(this, RootCategoriesActivity.class);
        startActivity(intent);
    }
}