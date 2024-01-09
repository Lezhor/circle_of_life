package com.android.circleoflife.communication.protocols;

import com.android.circleoflife.database.models.User;

import java.io.IOException;

/**
 * The SignUp protocol is for creating a new User serverside.
 */
public interface SignUpProtocol extends Protocol {

    /**
     * The server creates a new User with given username and password and sends it to this client.
     * @param username username
     * @param password password
     * @return user or null if no user was created.
     * @throws IOException if communication with server fails
     */
    User signUp(String username, String password) throws IOException;

}
