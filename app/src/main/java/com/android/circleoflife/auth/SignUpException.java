package com.android.circleoflife.auth;

/**
 * Exception thrown in case signing up fails.<br>
 * This can happen for example because username already exists or because username or password isn't valid
 */
public class SignUpException extends Exception {

    /**
     * Default constructor for SignUpException<br>
     * Message: <code>Signing up failed</code>
     */
    public SignUpException() {
        this("Signing up failed");
    }

    /**
     * Constructor for SignUpException
     * @param message Exception-message
     */
    public SignUpException(String message) {
        super(message);
    }

}
