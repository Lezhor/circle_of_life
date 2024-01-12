package com.android.circleoflife.e2e_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.android.circleoflife.e2e_tests.E2ETestUtils.*;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.R;
import com.android.circleoflife.auth.AuthenticationImpl;
import com.android.circleoflife.auth.UsernameParser;
import com.android.circleoflife.ui.CustomEspressoAddOns;
import com.android.circleoflife.ui.activities.auth.LoginActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateCycleEndToEndTest {
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

    /**
     * Espresso e2e-Test contains following steps:
     * <pre>
     *     1) Login
     *     2) Go to Categories
     *     3) Go inside "Test Category" ("Another Category" is located here)
     *     4) Create Cycle "My Cycle" (Frequency Saturday & Sunday)
     *     5) Move "MyCycle" into "Another Category"
     *     6) Undo last action
     *     7) Rename "My Cycle" to "Other Cycle"
     *     8) Delete "Other Cycle"
     * </pre>
     */
    @Test
    public void testZCreateMoveUndoAndDeleteCycle() {

        // Typing Login data:
        onView(withId(R.id.edit_username))
                .perform(CustomEspressoAddOns.replaceTextInInputTextLayout(
                        UsernameParser.usernameToDisplayedVersion(USERNAME)
                ));
        onView(withId(R.id.edit_password))
                .perform(CustomEspressoAddOns.replaceTextInInputTextLayout(PASSWORD));

        // Click Login Button:
        onView(withId(R.id.login_button)).perform(click());

        // Assert in MainMenuActivity
        onView(withId(R.id.main_menu_activity)).check(matches(isDisplayed()));

        // Go to categories
        onView(withId(R.id.main_menu_view_categories)).perform(click());

        // Assert RecyclerView has 1 Item with text "Test Category"
        onView(withId(R.id.recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewHasItemCount(1)));
        onView(withId(R.id.recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewItemHasText(0, "Test Category")));

        // Click on Item
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Assert RecyclerView has 1 Item with text "Another Category"
        onView(withId(R.id.categories_recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewHasItemCount(1)));
        onView(withId(R.id.categories_recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewItemHasText(0, "Another Category")));

        // Open Cycle Dialog
        onView(withId(com.getbase.floatingactionbutton.R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.fab_create_cycle)).perform(click());

        // Fill dialog
        onView(withId(R.id.edit_name_dialog_input)).perform(CustomEspressoAddOns.replaceTextInInputTextLayout("My Cycle"));
        onView(withId(R.id.option_saturday)).perform(click());
        onView(withId(R.id.option_sunday)).perform(click());

        // Press OK
        onView(withText(R.string.dialog_ok))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        // Assert RecyclerView has 2 Items. Second with text "My Cycle"
        onView(withId(R.id.categories_recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewHasItemCount(2)));
        onView(withId(R.id.categories_recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewItemHasText(1, "My Cycle")));

        // Undo last action
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.undo_last_action)).perform(click());

        // Assert RecyclerView has 1 Item with text "Another Category"
        onView(withId(R.id.categories_recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewHasItemCount(1)));
        onView(withId(R.id.categories_recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewItemHasText(0, "Another Category")));
    }


}
