package com.android.circleoflife.database.validators;

import com.android.circleoflife.auth.UsernameParser;

/**
 * This class contains static methods for validating e.g. different Strings throughout the project.
 */
public final class StringValidator {

    // TODO: 20.12.2023 Error-Messages should be fetched from strings.xml

    public static final int MIN_LENGTH_USERNAME = 6;
    public static final int MIN_LENGTH_PASSWORD = 6;

    /**
     * Validates a username. succeeds if:
     * <pre>
     *     1) username isn't null or empty
     *     2) username has at least <code>MIN_LENGTH_USERNAME</code> characters
     *     3) username contains letters
     *     4) it only contains letters numbers and underscores (no special chars)
     * </pre>
     * @param username username to be validated
     * @return itself if all checks succeed
     * @throws IllegalArgumentException if at least one check fails
     */
    public static String validateUsername(String username) throws IllegalArgumentException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username can't be empty");
        } else if (!stringHasMinLength(username, MIN_LENGTH_USERNAME)) {
            throw new IllegalArgumentException("Username is too short");
        } else if (!stringContainsLetters(username)) {
            throw new IllegalArgumentException("Username has to have at least one letter");
        } else if (!stringConsistsOfLettersNumbersAndUnderscores(username)) {
            throw new IllegalArgumentException("Username can't have special characters");
        }
        return username;
    }

    /**
     * Validates a displayed lversion of a username by converting it to the actual version with {@link UsernameParser} and then calling {@link #validateUsername(String)}
     * @param displayedUsername displayedUsername
     * @return displayed username (adapted version if param was somehow wrong - e.g. had too many capital letters)
     * @throws IllegalArgumentException if validating fails
     * @see #validateUsername(String)
     */
    public static String validateDisplayedUsername(String displayedUsername) throws IllegalArgumentException {
        String username = UsernameParser.displayedUsernameToActualVersion(displayedUsername);
        username = validateUsername(username);
        return UsernameParser.usernameToDisplayedVersion(username);
    }

    /**
     * Validates a password. succeeds if:
     * <pre>
     *     1) password isn't null or empty
     *     2) password has at least <code>MIN_LENGTH_PASSWORD</code> characters
     *     3) password contains letters
     *     4) password does not contain spaces
     * </pre>
     * @param password username to be validated
     * @return itself if all checks succeed
     * @throws IllegalArgumentException if at least one check fails
     */
    public static String validatePassword(String password) throws IllegalArgumentException {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password can't be empty");
        } else if (!stringHasMinLength(password, MIN_LENGTH_PASSWORD)) {
            throw new IllegalArgumentException("Password is too short");
        } else if (!stringContainsLetters(password)) {
            throw new IllegalArgumentException("Password needs to contain at least 1 letter");
        } else if (password.contains(" ")) {
            throw new IllegalArgumentException("Password can't contain spaces");
        }
        return password;
    }

    public static void validateString(String str) throws IllegalArgumentException {
        validateString(str, "String");
    }

    /**
     * Validates a String<br>
     * String counts as valid if it is not null and not empty.
     * @param str string to be validated
     * @param name name of the string e.g. user, category
     * @throws IllegalArgumentException if str is null or empty
     */
    public static void validateString(String str, String name) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException(name + " can't be null");
        }
        if (str.isEmpty()) {
            throw new IllegalArgumentException(name + " can't be empty");
        }
    }

    /**
     * Validates string for specific length. Succeeds if Strings length is at least length
     * @param str string
     * @param length length
     * @return unchanged str if string is at least length characters long
     * @throws IllegalArgumentException if str is shorter than length
     */
    public static String validateStringMinLength(String str, int length) throws IllegalArgumentException {
        if (!stringHasMinLength(str, length)) {
            throw new IllegalArgumentException("String is too short");
        }
        return str;
    }





    // -----------------------   Static String methods   -----------------------------------

    /**
     * Checks if given string has a minimal length
     * @param str given string
     * @param minLength minimal length
     * @return whether or not given string has minimal length
     */
    public static boolean stringHasMinLength(String str, int minLength) {
        return str.length() >= minLength;
    }

    /**
     * Checks whether a string contains numbers or not
     * @param str given string
     * @return true if str contains at least one number
     */
    public static boolean stringContainsNumbers(String str) {
        return str != null && str.matches(".*\\d.*");
    }

    /**
     * Checks whether a string contains letters or not
     * @param str string
     * @return true if str contains at last one letter
     */
    public static boolean stringContainsLetters(String str) {
        return str != null && str.matches(".*\\p{L}.*");
    }


    /**
     * Returns true if a string consists only of letters and underscores. Empty string counts too
     * @param str string
     * @return true if strirng consists ony of letters and underscores
     */
    public static boolean stringConsistsOfLettersNumbersAndUnderscores(String str) {
        return str != null && str.matches("^[\\p{L}0-9_]*$");
    }


    /**
     * succeeds if str has no capital letters. empty string counts too
     * @param str string
     * @return true if str has no capital letters
     */
    public static boolean stringHasNoUppercaseLetters(String str) {
        return str != null && str.matches("^[^A-Z]*$");
    }

}
