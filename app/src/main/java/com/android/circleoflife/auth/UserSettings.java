package com.android.circleoflife.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.type_converters.UUIDConverter;

import java.util.UUID;

/**
 * Holds settings for a user. Settings are, if server syncing is enabled, and if automatic syncing is enabled
 */
public class UserSettings {

    public final static String USER_PREFS_SET = "set";
    public final static String SERVER_SYNC_ENABLED = "server_sync_enabled";
    public final static String AUTOMATIC_SYNC_ENABLED = "automatic_sync_enabled";

    /**
     * Retrieves SharedPrefs which the user with passed userID uses to saves the settings
     * @param userID userID
     * @return SharedPrefs of the user
     */
    private static SharedPreferences getSharedPreferences(UUID userID) {
        return App.getApplicationContext().getSharedPreferences(UUIDConverter.uuidToString(userID), Context.MODE_PRIVATE);
    }

    /**
     * Fetches UserSettings from SharedPreferences. If doesn't exist, creates new.
     * @param user user
     * @return user-settings for passed user
     */
    public static UserSettings getSettings(User user) {
        SharedPreferences sp = getSharedPreferences(user.getId());
        UserSettings settings;
        if (sp.contains(USER_PREFS_SET)) {
            settings = new UserSettings(
                    user.getId(),
                    sp.getBoolean(SERVER_SYNC_ENABLED, true),
                    sp.getBoolean(AUTOMATIC_SYNC_ENABLED, true)
            );
        } else {
            settings = new UserSettings(user);
        }
        return settings;
    }

    private final UUID userID;
    private boolean serverSyncEnabled;
    private boolean automaticServerSync;

    /**
     * Constructor for setting all attributes
     * @param userID userID
     * @param serverSyncEnabled if syncing to server is enabled
     * @param automaticServerSync if syncing with server is automatic
     */
    private UserSettings(@NonNull UUID userID, boolean serverSyncEnabled, boolean automaticServerSync) {
        this.userID = userID;
        this.serverSyncEnabled = serverSyncEnabled;
        this.automaticServerSync = automaticServerSync;
        saveToSharedPrefs();
    }

    /**
     * Constructor for new UserSettings
     * @param user user to create the settings for
     */
    private UserSettings(@NonNull User user) {
        this(user.getId(), true, true);
    }

    /**
     * Saves Settings to sharedPrefs.
     */
    private void saveToSharedPrefs() {
        SharedPreferences sp = UserSettings.getSharedPreferences(userID);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(USER_PREFS_SET, true);
        editor.putBoolean(SERVER_SYNC_ENABLED, serverSyncEnabled);
        editor.putBoolean(AUTOMATIC_SYNC_ENABLED, automaticServerSync);
        editor.apply();
    }

    public UUID getUserID() {
        return userID;
    }

    public boolean isServerSyncEnabled() {
        return serverSyncEnabled;
    }

    /**
     * Sets serverSyncEnabled. This also affects setAutomaticServerSync.
     * @param serverSyncEnabled new value for serverSyncEnabled
     */
    public void setServerSyncEnabled(boolean serverSyncEnabled) {
        if (this.serverSyncEnabled != serverSyncEnabled) {
            this.serverSyncEnabled = serverSyncEnabled;
            setAutomaticServerSync(serverSyncEnabled);
            saveToSharedPrefs();
        }
    }

    public boolean isAutomaticServerSync() {
        return automaticServerSync;
    }

    /**
     * Changes automaticServerSync value. Only works is serverSync is enabled.
     * @param automaticServerSync new value
     */
    public void setAutomaticServerSync(boolean automaticServerSync) {
        if (this.serverSyncEnabled) {
            this.automaticServerSync = automaticServerSync;
            saveToSharedPrefs();
        }
    }
}
