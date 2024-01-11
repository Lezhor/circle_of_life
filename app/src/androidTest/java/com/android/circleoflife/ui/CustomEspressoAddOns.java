package com.android.circleoflife.ui;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;

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

    /**
     * Writes text into edittext inside a InputTextLayout
     * @param text text to write into
     * @return ViewAction used for Espresso
     */
    public static ViewAction replaceTextInInputTextLayout(final String text) {
        return ViewActions.actionWithAssertions(new ViewAction() {
            @Override
            public String getDescription() {
                return String.format("replace text(%s)", text);
            }

            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TextInputLayout.class));
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TextInputLayout textInput) {
                    EditText editText = textInput.getEditText();
                    if (editText != null) {
                        editText.setText(text);
                    }
                }
            }
        });
    }

    /**
     * Used for Espresso checks to check if recyclerView's adapter has given itemCount
     * @param itemCount itemCount
     * @return Matcher for checking if there is given amount of items in recyclerView
     */
    public static Matcher<View> recyclerViewHasItemCount(final int itemCount) {
        return new BoundedMatcher<>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
                if (adapter != null) {
                    return adapter.getItemCount() == itemCount;
                }
                return false;
            }
        };
    }

    /**
     * Checks if itemCount is 0
     * @return Matcher for checking if itemCount is 0
     */
    public static Matcher<View> recyclerViewIsEmpty() {
        return recyclerViewHasItemCount(0);
    }

}
