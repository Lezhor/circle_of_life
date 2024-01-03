package com.android.circleoflife.communication.protocols;

import com.android.circleoflife.database.models.User;

/**
 * The SignUp protocol is for creating a new User serverside.
 */
public interface SignUpProtocol extends Protocol {

    /**
     * The server creates a new User with given username and password and sends it to this client.
     * @param username username
     * @param password password
     * @return user or null if no user was created.
     */
    User signUp(String username, String password);

    /**
     * Registers passed user on server. If user already exists the server does nothing and the method returns false
     * @param user user to be registered on server
     * @return true if user is successfully registered on server
     */
    boolean signUp(User user);

}
