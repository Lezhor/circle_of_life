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
     * If this is true, there will be no communication with the server! ever!<br>
     * Note that enabling it later could not work in some cases.
     * @return whether or not server-syncing is enabled. true means not.
     * @throws AuthenticationFailedException if user is not logged in
     */
    boolean localOnly() throws AuthenticationFailedException;

    /**
     * Does nothing is {@link Authentication#localOnly()} is set to false or if currently not authenticated.
     * @return true if enabling was successful.
     * @throws AuthenticationFailedException if user is not logged in
     */
    boolean enableServerSide() throws AuthenticationFailedException;

    /**
     * Returns logged in user and null if no user is currently authenticated
     * @return user which is currently logged in
     * @throws AuthenticationFailedException if no user currently logged in
     */
    User getUser() throws AuthenticationFailedException;

    /**
     * Waits until User is set! - This method should be called on a background thread
     * @return user
     * @throws InterruptedException if waiting was interrupted
     * @throws AuthenticationFailedException if no user currently logged in
     */
    User waitForUser() throws AuthenticationFailedException, InterruptedException;

    /**
     * Used in {@link SyncProtocol SyncProtocol} for the authentication.<br>
     * Authentication String can look like this:<br>
     * <code>
     *     "auth[userName|passwordHash]"
     * </code>
     * @return String containing username and password hash
     * @throws AuthenticationFailedException if user is not logged in
     */
    String getAuthenticationString() throws AuthenticationFailedException;

    /**
     * Returns authenticated status for user username
     * @param user user to be checked.
     * @return true if the passed userName is currently authenticated
     */
    boolean authenticated(User user);

    /**
     * Checks authentication on server and sets the values of this instance if successful<br><br>
     * Does nothing if already authenticated.<br>
     * Note that in this case the method only returns true if the already authenticated user
     * is the user passed in the parameter
     * @param userName userName for authentication
     * @param password password for authentication
     * @return true or false whether or not the user is authenticated now.
     * @throws AuthenticationFailedException if logging in failed (because username not found or incorrect password)
     * @throws InterruptedException if waiting for server gets interrupted
     */
    boolean login(String userName, String password) throws AuthenticationFailedException, InterruptedException;

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
     * @throws SignUpException if username or password are not valid, if username exists of if connection to server could not be achieved.
     */
    void signUp(String userName, String password, boolean localOnly) throws SignUpException, InterruptedException;

    /**
     * Checks if username is currently available
     * @param userName username to be checked
     * @param onServerToo whether or not the method should check on server too
     * @return wheter or noto the username is available
     * @throws IOException if communication to server failed
     */
    boolean checkIfUserNameAvailable(String userName, boolean onServerToo) throws IOException;

}
