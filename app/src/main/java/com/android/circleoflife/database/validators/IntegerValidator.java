package com.android.circleoflife.database.validators;

/**
 * Offers methods for integer validation. e.g. so that integer is in specific bounds.
 */
public class IntegerValidator {

    /**
     * Validates an integer. succeeds if integer is between min and max
     * @param value integer to validate
     * @param min min value (included)
     * @param max max value (included)
     * @return given integer value
     * @throws IllegalArgumentException if value is smaller than min or value is greater then max
     */
    public static int validateIntBetweenBounds(int value, int min, int max) throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException("min can't be greater than max values!!!");
        }
        if (value < min) {
            throw new IllegalArgumentException("Value " + value + " is too small");
        } else if (value > max) {
            throw new IllegalArgumentException("Value " + value + " is too big");
        }
        return value;
    }

}
