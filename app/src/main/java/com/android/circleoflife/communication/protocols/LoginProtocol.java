package com.android.circleoflife.communication.protocols;

import com.android.circleoflife.database.models.User;

import java.io.IOException;

/**
 * This protocol is for logging in. It sends Login-Data (username/password) to the server
 * and the server returns an instance of {@link User} if one is found.
 */
public interface LoginProtocol extends Protocol {

    /**
     * Fetches user from server with given username and password.
     * If no user with given username is found or if password is invalid returns null
     * @param username username
     * @param password password
     * @return user or null if no user found
     * @throws IOException if communication fails
     */
    User login(String username, String password) throws IOException;

}
