package com.android.circleoflife.ui;

import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class CustomEspressoAddOns {

    /**
     * used for checking if a given textInputLayout has an errorMessage displayed
     * @param expectedErrorText if null checks if error field is not empty
     * @return matcher used in check() (for Espresso)
     */
    public static Matcher<View> hasErrorTextInputLayout(@Nullable final String expectedErrorText) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextInputLayout textInput) {
                    CharSequence error = textInput.getError();
                    if (error == null) {
                        System.out.println("Error is null");
                        return false;
                    } else {
                        if (expectedErrorText == null) {
                            System.out.println("Error is: '" + error + "'");
                            return !error.toString().equals("");
                        } else {
                            System.out.println("Error is: '" + error + "' and expcted: '" + expectedErrorText + "'");
                            return error.toString().equals(expectedErrorText);
                        }
                    }
                }
                System.out.println("Passed View is not of type TextInputLayout");
                return false;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    /**
     * used for checking if a given textInputLayout has an errorMessage displayed. succeeds if it has no error
     * @return matcher used in check() (for Espresso)
     */
    public static Matcher<View> hasNoErrorTextInputLayout() {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextInputLayout textInput) {
                    CharSequence error = textInput.getError();
                    return error == null;
                }
                System.out.println("Passed View is not of type TextInputLayout");
                return false;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

}
