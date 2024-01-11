package com.android.circleoflife.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.control.DatabaseController;
import com.android.circleoflife.database.models.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class AuthenticationImpl implements Authentication {
    private static final String TAG = Authentication.class.getSimpleName();

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
    public void autoSync() {
        if (authenticated() && settings.isAutomaticServerSync()) {
            App.getDatabaseController().syncWithServer(this);
        }
    }

    @Override
    public boolean manualSync() {
        if (authenticated()) {
            return App.getDatabaseController().syncWithServer(this);
        }
        return false;
    }

    @Override
    public boolean authenticated() {
        return user != null;
    }

    @Override
    public boolean login(String username, String password) throws IOException {
        Log.d(TAG, "login: trying to login :'" + username + "'");
        DatabaseController db = App.getDatabaseController();
        User user = db.getUserByUsername(username);
        boolean remoteLogin = false;
        if (user != null) {
            // User in local db
            Log.d(TAG, "login: User found in local db!");
            if (!user.getPassword().equals(password)) {
                Log.d(TAG, "login: password '" + password + "' is not correct");
                user = null;
            }
        } else {
            // Send request to server
            Log.d(TAG, "login: user not found in local db");
            Log.d(TAG, "login: sending login request to server");
            user = App.getLoginProtocol().login(username, password);
            remoteLogin = true;
        }
        if (user != null) {
            Log.d(TAG, "login: successfully logged in: " + user);
            setUser(user);
            if (remoteLogin) {
                db.insertUsers(user);
                getSettings().setLastSyncDate(null);
            }
            return true;
        } else {
            Log.d(TAG, "login: failed for username: '" + username + "'");
            return false;
        }
    }

    /**
     * Gets lastLoginData from SharedPreferences and tries to log in with those.
     * If fails it calls {@link #removeLoginDataFromPrefs()}
     * @return true if login was successful
     * @see #login(String, String)
     */
    @Override
    public boolean loginWithSavedLoginData() {
        boolean loggedIn = false;
        SharedPreferences sp = App.getApplicationContext().getSharedPreferences(LAST_LOGIN_DATA_PREFS, Context.MODE_PRIVATE);
        String username = sp.getString(PREFS_USERNAME, null);
        String password = sp.getString(PREFS_PASSWORD, null);
        if (username != null && password != null) {
            Log.d(TAG, "loginWithSavedLoginData: Trying to log from saved login data. username: " + username);
            try {
                loggedIn = login(username, password);
            } catch (IOException ignored) {
            }
            if (!loggedIn) {
                removeLoginDataFromPrefs();
            }
        }
        return loggedIn;
    }

    @Override
    public void logout() {
        Log.d(TAG, "logout: user: " + user);
        setUser(null);
    }

    @Override
    public boolean signUp(String username, String password, boolean localOnly) throws IOException {
        User user;
        DatabaseController db = App.getDatabaseController();
        Log.d(TAG, "signUp: Trying to signUp username '" + username + "' " + (localOnly ? "on this machine" : "on remote server"));
        if (localOnly) {
            user = db.getUserByUsername(username);
            if (user != null) {
                // username already exists
                user = null;
            } else {
                user = new User(UUID.randomUUID(), username, password, LocalDateTime.now(App.SERVER_TIMEZONE));
            }
        } else {
            user = App.getSignUpProtocol().signUp(username, password);
        }
        if (user != null) {
            setUser(user);
            Log.d(TAG, "signUp: successful, inserting user into database: " + user);
            db.insertUsers(user);
            return true;
        } else {
            // signup failed
            Log.d(TAG, "signUp: failed for username: " + username + (localOnly ? " (local only)" : ""));
            return false;
        }
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
        Log.d(TAG, "saveLoginDataToPrefs: " + user);
        SharedPreferences sp = App.getApplicationContext().getSharedPreferences(LAST_LOGIN_DATA_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_USERNAME, user.getUsername());
        editor.putString(PREFS_PASSWORD, user.getPassword());
        editor.apply();
    }

    /**
     * Removes login data from prefs
     */
    @Override
    public void removeLoginDataFromPrefs() {
        Log.d(TAG, "removeLoginDataFromPrefs: " + user);
        SharedPreferences sp = App.getApplicationContext().getSharedPreferences(LAST_LOGIN_DATA_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(PREFS_USERNAME);
        editor.remove(PREFS_PASSWORD);
        editor.apply();
    }
}
