package com.android.circleoflife.auth;

import com.android.circleoflife.communication.protocols.SyncProtocol;
import com.android.circleoflife.database.models.User;

import java.io.IOException;

/**
 * This is where the authentication happens. Every script can get the Username but not the password (only its hash value).<br>
 * At any time any script can check whether a user is currently authenticated (logged in) or not.<br>
 * The {@link SyncProtocol SyncProtocol} also uses the authentication for server-sync
 * @version 1.0
 */
public interface Authentication {

    /**
     * Returns logged in user and null if no user is currently authenticated
     * @return user which is currently logged in
     */
    User getUser();

    /**
     * Waits until User is set! - This method should be called on a background thread
     * @return user
     * @throws InterruptedException if waiting was interrupted
     */
    User waitForUser() throws InterruptedException;

    /**
     * Checks if automatic syncing is enabled and syncs if so
     */
    void autoSync();

    /**
     * Syncs even if autoSync is not enabled
     * @return true if synchronisation succeeds
     */
    boolean manualSync();

    /**
     * Returns true if there is a user authenticated
     * @return true if there si a user authenticated
     */
    boolean authenticated();

    /**
     * If a user is currently authenticated, does nothing.
     * (also returns false if authenticated user is another one than the one passed in the parameters)<br>
     * If no user is authenticated searches through the local room-database for a user with matching username.
     * If found checks the password value. If matches, sets the authenticated user and returns true.<br>
     * If no the username isn't found in the local room-database, sends a login-request to the server
     * @param userName userName for authentication
     * @param password password for authentication
     * @return true or false whether or not the user is authenticated now.
     * @throws IOException if communication with server fails
     * @see com.android.circleoflife.communication.protocols.LoginProtocol
     */
    boolean login(String userName, String password) throws IOException;

    /**
     * retrieves loginData from SharedPrefs and tries to login with this data
     * @return true if login was successful
     * @see #login(String, String)
     */
    boolean loginWithSavedLoginData();

    /**
     * After this method is called the no user will be authenticated.
     * No matter if they were beforehand or not.
     */
    void logout();

    /**
     * creates a new account. If localOnly is set to false, the server creates
     * @param userName username
     * @param password password
     * @param localOnly if true syncing to server is disabled. can be turned on later however fails to do so if username already exists.
     * @throws IOException if communication with server fails
     */
    boolean signUp(String userName, String password, boolean localOnly) throws IOException;

    // TODO: 09.01.2024 signUp(User) for the case that a user which was localOnly wants to sync to server now

    /**
     * Getter for user settings
     * @return user settings
     */
    UserSettings getSettings();

}
