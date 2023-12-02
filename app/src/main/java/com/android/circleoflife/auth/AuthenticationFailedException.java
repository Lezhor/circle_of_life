package com.android.circleoflife.auth;

/**
 * Used when trying to use a feature which requires user authentication
 * @see Authentication
 */
public class AuthenticationFailedException extends Exception {

    /**
     * Default Constructor
     */
    public AuthenticationFailedException() {
        this("Authentication failed");
    }

    /**
     * Constructor with message
     * @param message message
     */
    public AuthenticationFailedException(String message) {
        super(message);
    }

}
