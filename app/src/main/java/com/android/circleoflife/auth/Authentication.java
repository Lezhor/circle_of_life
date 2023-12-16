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
     */
    User getUser();

    /**
     * Getter for username. Returns null if currently not authenticated<br>
     * Username contains only lowercase letters, digits and underscores
     * @return returns username
     * @throws AuthenticationFailedException if user is not logged in
     */
    String getUserName() throws AuthenticationFailedException;

    /**
     * Returns the displayed version of the userName.
     * @return displayed version of userName
     * @see Authentication#usernameToDisplayedVersion(String)
     * @throws AuthenticationFailedException if user is not logged in
     */
    default String getDisplayedUsername() throws AuthenticationFailedException {
        return usernameToDisplayedVersion(getUserName());
    }

    /**
     * Converts username to its displayed version<br>
     * The differences are that:<br>
     * 1) all underscores are replaced with spaces<br>
     * 2) every new word starts with a capital letter
     * @param username username
     * @return displayed version of username
     */
    String usernameToDisplayedVersion(String username);

    /**
     * Converts displayedUsername to actual version<br>
     * The differences are that:<br>
     * 1) all spaces are replaced with underscores<br>
     * 2) everything is lowercase
     * @param displayedUsername displayedUsername
     * @return actual version of username with underscores and lowercase letters
     */
    String displayedUsernameToActualVersion(String displayedUsername);

    /**
     * Getter for password hash. PasswordHash makes use of timeStampOfAccountCreation. Returns null if currently not authenticated.
     * @return returns password hash
     * @throws AuthenticationFailedException if user is not logged in
     */
    String getPasswordHash() throws AuthenticationFailedException;

    /**
     * Returns timestamp of account creation. can't be changed.
     * @return timestamp of account creation.
     * @throws AuthenticationFailedException if user is not logged in
     */
    Date getTimeStampOfAccountCreation() throws AuthenticationFailedException;

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
     * @param userName user to be checked.
     * @return true if the passed userName is currently authenticated
     */
    boolean authenticated(String userName);

    /**
     * Checks authentication on server and sets the values of this instance if successful<br><br>
     * Does nothing if already authenticated.<br>
     * Note that in this case the method only returns true if the already authenticated user
     * is the user passed in the parameter
     * @param userName userName for authentication
     * @param password password for authentication
     * @return true or false whether or not the user is authenticated now.
     * @throws AuthenticationFailedException if user is not logged in
     */
    boolean login(String userName, String password) throws AuthenticationFailedException;

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
    void signUp(String userName, String password, boolean localOnly) throws SignUpException;

    /**
     * checks if userName is valid.<br>
     * A userName is valid if:<br>
     * 1) it is at least 4 characters long<br>
     * 2) it contains only lowercase letters, digits and underscores<br><br>Examples for valid usernames are:<br>
     * <code>
     *     max_mustermann23<br>
     *     johnny_depp<br>
     *     mr_beast
     * </code>
     * @param userName username to be checked
     * @throws IllegalArgumentException with corresponding message, if one of the conditions is not satisfied
     */
    void validateUserName(String userName) throws IllegalArgumentException;

    /**
     * Checks if username is currently available
     * @param userName username to be checked
     * @param onServerToo whether or not the method should check on server too
     * @return wheter or noto the username is available
     * @throws IOException if communication to server failed
     */
    boolean checkIfUserNameAvailable(String userName, boolean onServerToo) throws IOException;

    /**
     * checks if userName is valid.<br>
     * A userName is valid if:<br>
     * 1) it is at least 6 characters long<br>
     * 2) it contains only letters, digits, underscores, dots
     * 3) it contains at least one letter and one digit
     * <br><br>Examples for valid passwords are:<br>
     * <code>
     *     PassWord.323<br>
     *     LOL123
     * </code>
     * @param password password to be checked
     * @throws IllegalArgumentException with corresponding message, if one of the conditions is not satisfied
     */
    void validatePassword(String password) throws IllegalArgumentException;

}
