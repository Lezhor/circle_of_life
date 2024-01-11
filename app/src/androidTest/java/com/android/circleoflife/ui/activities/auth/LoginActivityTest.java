package com.android.circleoflife.ui.activities.auth;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.R;
import com.android.circleoflife.auth.AuthenticationImpl;
import com.android.circleoflife.ui.CustomEspressoAddOns;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenario = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void aaaTestSetup() {
        // This should be the fist test called
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sp = context.getSharedPreferences(AuthenticationImpl.LAST_LOGIN_DATA_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(AuthenticationImpl.PREFS_USERNAME);
        editor.remove(AuthenticationImpl.PREFS_PASSWORD);
        editor.apply();
    }

    @Test
    public void testLoginActivityDisplayed() {
        onView(withId(R.id.login_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginActivityLeaveBothFieldsEmptyProducesError() {
        onView(withId(R.id.edit_username)).check(matches(CustomEspressoAddOns.hasNoErrorTextInputLayout()));
        onView(withId(R.id.edit_password)).check(matches(CustomEspressoAddOns.hasNoErrorTextInputLayout()));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.edit_username)).check(matches(CustomEspressoAddOns.hasErrorTextInputLayout(null)));
        onView(withId(R.id.edit_password)).check(matches(CustomEspressoAddOns.hasErrorTextInputLayout(null)));
    }

    @Test
    public void testLoginActivityToToSignUpActivityAndBack() {
        onView(withId(R.id.login_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.signup_button)).perform(click());
        onView(withId(R.id.signup_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_activity)).check(matches(isDisplayed()));
    }

}
