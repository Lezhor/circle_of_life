package com.android.circleoflife.database.validators;

/**
 * This class contains static methods for validating e.g. different Strings throughout the project.
 */
public final class StringValidator {

    public static final int MIN_LENGTH_USERNAME = 6;

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

    public static String validatePassword(String password) throws IllegalArgumentException {
        // TODO: 06.12.2023 Validate password
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password can't be empty");
        }
        return password;
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
