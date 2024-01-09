package com.android.circleoflife.auth;

import com.android.circleoflife.communication.protocols.SyncProtocol;
import com.android.circleoflife.database.models.User;

import java.io.IOException;
import java.util.Date;

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
     * Returns authenticated status for user username
     * @param user user to be checked.
     * @return true if the passed userName is currently authenticated
     */
    boolean authenticated(User user);

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
     * After this method is called the no user will be authenticated.
     * No matter if they were beforehand or not.
     */
    void logout();

    /**
     * creates a new account both on server and client and {@link Authentication#login(String, String) logs in} into it.
     * @param userName username
     * @param password password
     * @param localOnly if true syncing to server is disabled. can be turned on later however fails to do so if username already exists.
     * @throws IOException if communication with server fails
     */
    boolean signUp(String userName, String password, boolean localOnly) throws IOException;

    UserSettings getSettings();

}
