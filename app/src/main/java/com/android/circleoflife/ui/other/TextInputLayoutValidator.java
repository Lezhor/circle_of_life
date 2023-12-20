package com.android.circleoflife.ui.other;

import com.google.android.material.textfield.TextInputLayout;

/**
 * offers various methods for verifying {@link com.google.android.material.textfield.TextInputLayout TextInputLayouts}.
 */
public class TextInputLayoutValidator {

    /**
     * Validates given inputLayout. Sets error if validator throws an IllegalArgumentException. sets error to null if validation succeeds<br>
     * Replaces every 'String'-String in error-message to 'input'
     * @param inputLayout Inputlayout
     * @param validator validator-method
     * @return true if validation successful
     */
    public static boolean validate(TextInputLayout inputLayout, TextInputLayoutValidateMethod validator) {
        return validate(inputLayout, validator, "input");
    }

    /**
     * Validates given inputLayout. Sets error if validator throws an IllegalArgumentException. sets error to null if validation succeeds
     * @param inputLayout Inputlayout
     * @param validator validator-method
     * @param name Every 'String'-String will be replaced with name
     * @return true if validation successful
     */
    public static boolean validate(TextInputLayout inputLayout, TextInputLayoutValidateMethod validator, String name) {
        if (inputLayout.getEditText() == null) {
            return false;
        }
        try {
            validator.validate(inputLayout.getEditText().getText().toString().trim());
            inputLayout.setError(null);
            return true;
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage().replaceAll("String", name);
            inputLayout.setError(errorMessage);
            return false;
        }
    }

    @FunctionalInterface
    public interface TextInputLayoutValidateMethod {
        void validate(String str) throws IllegalArgumentException;
    }
}
