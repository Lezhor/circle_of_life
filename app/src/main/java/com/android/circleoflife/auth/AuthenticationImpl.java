package com.android.circleoflife.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.control.DatabaseController;
import com.android.circleoflife.database.models.User;

import java.io.IOException;

public class AuthenticationImpl implements Authentication {

    public static String LAST_LOGIN_DATA_PREFS = "lastLoginPrefs";
    public static String PREFS_USERNAME = "username";
    public static String PREFS_PASSWORD = "password";

    private static volatile AuthenticationImpl instance;

    /**
     * AuthenticationImpl follows the singleton-pattern
     * @return only existing instance of this class
     */
    public static AuthenticationImpl getInstance() {
        if (instance == null) {
            synchronized (AuthenticationImpl.class) {
                if (instance == null) {
                    instance = new AuthenticationImpl();
                }
            }
        }
        return instance;
    }

    // TODO: 16.12.2023 Implement all methods!!

    private volatile User user;
    private UserSettings settings;

    private AuthenticationImpl() {
    }

    @Override
    public User getUser() {
        return user;
    }

    /**
     * Setter for user. Also saves (or removes if user null) loginData from Prefs, so that on restart the user would be logged in automatically.
     * Also sets settings for the user
     * @param user user
     */
    private void setUser(User user) {
        this.user = user;
        if (this.user != null) {
            this.settings = UserSettings.getSettings(this.user);
            saveLoginDataToPrefs(this.user);
        } else {
            this.settings = null;
            removeLoginDataFromPrefs();
        }
        synchronized (this) {
            this.notifyAll();
        }
    }

    @Override
    public User waitForUser() throws InterruptedException {
        if (user == null) {
            synchronized (this) {
                if (user == null) {
                    this.wait();
                }
            }
        }
        return user;
    }

    @Override
    public boolean authenticated(User user) {
        return user != null;
    }

    @Override
    public boolean login(String username, String password) throws IOException {
        DatabaseController db = App.getDatabaseController();
        User user = db.getUserByUsername(username);
        if (user != null) {
            // User in local db
            if (!user.getPassword().equals(password)) {
                user = null;
            }
        } else {
            // Send request to server
            user = App.getLoginProtocol().login(username, password);
        }
        if (user != null) {
            setUser(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets lastLoginData from SharedPreferences and tries to log in with those.
     * @return true if login was successful
     * @see #login(String, String)
     */
    private boolean loginWithSavedLoginData() {
        SharedPreferences sp = App.getApplicationContext().getSharedPreferences(LAST_LOGIN_DATA_PREFS, Context.MODE_PRIVATE);
        String username = sp.getString(PREFS_USERNAME, null);
        String password = sp.getString(PREFS_PASSWORD, null);
        if (username == null || password == null) {
            return false;
        }
        try {
            return login(username, password);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void logout() {
        setUser(null);
    }

    @Override
    public boolean signUp(String userName, String password, boolean localOnly) throws IOException {
        // TODO: 09.01.2024 signUp
        return false;
    }

    @Override
    public UserSettings getSettings() {
        return settings;
    }

    /**
     * Saves username and password to SharedPreferences so that on restart the user could login automatically
     * @param user user to be saved
     */
    private void saveLoginDataToPrefs(User user) {
        SharedPreferences sp = App.getApplicationContext().getSharedPreferences(LAST_LOGIN_DATA_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_USERNAME, user.getUsername());
        editor.putString(PREFS_PASSWORD, user.getPassword());
        editor.apply();
    }

    /**
     * Removes login data from prefs
     */
    private void removeLoginDataFromPrefs() {
        SharedPreferences sp = App.getApplicationContext().getSharedPreferences(LAST_LOGIN_DATA_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(PREFS_USERNAME);
        editor.remove(PREFS_PASSWORD);
        editor.apply();
    }
}
