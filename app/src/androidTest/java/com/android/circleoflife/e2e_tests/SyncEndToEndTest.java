package com.android.circleoflife.e2e_tests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.android.circleoflife.e2e_tests.E2ETestUtils.*;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.auth.AuthenticationImpl;
import com.android.circleoflife.auth.UsernameParser;
import com.android.circleoflife.database.control.RoomDBTester;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;
import com.android.circleoflife.ui.CustomEspressoAddOns;
import com.android.circleoflife.ui.activities.auth.LoginActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SyncEndToEndTest {
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
    public void testLoginOnServerE2E() {
        User user = App.getDatabaseController().getUserByUsername(USERNAME);
        if (user != null) {
            try {
                RoomDBTester.clearUserData(App.getDatabaseController(), user);
            } catch (InterruptedException ignored) {
            }
            try {
                App.getDatabaseController().deleteUser(user);
            } catch (Exception e) {
                fail("Deleting user failed: " + e.getLocalizedMessage());
            }
        }

        try {
            App.getAuthentication().signUp(USERNAME, PASSWORD, false);
        } catch (IOException e) {
            fail(e.getLocalizedMessage());
        }

        // Typing Login data:
        onView(withId(R.id.edit_username))
                .perform(CustomEspressoAddOns.replaceTextInInputTextLayout(
                        UsernameParser.usernameToDisplayedVersion(USERNAME)
                ));
        onView(withId(R.id.edit_password))
                .perform(CustomEspressoAddOns.replaceTextInInputTextLayout(PASSWORD));

        // Login:
        onView(withId(R.id.login_button)).perform(click());

        // Assert in MainMenuActivity
        onView(withId(R.id.main_menu_activity)).check(matches(isDisplayed()));

        App.getAuthentication().removeLoginDataFromPrefs();
    }

    @Test
    public void testSyncManuallyE2E() {
        try {
            App.getAuthentication().login(USERNAME, PASSWORD);
        } catch (IOException e) {
            fail(e.getLocalizedMessage());
        }

        // adding log to server if not there - and clearing local database for testuser
        if (App.getAuthentication().authenticated()) {
            App.getAuthentication().getSettings().setAutomaticServerSync(false);
            App.getAuthentication().getSettings().setLastSyncDate(null);
            App.getAuthentication().manualSync();
            DBLog<?>[] logs = App.getDatabaseController().getLogs(App.getAuthentication().getUser(), null, null);
            if (logs.length == 0) {
                App.getDatabaseController().insertCategories(new Category(UUID.randomUUID(), "Test Category", App.getAuthentication().getUser().getId(), null));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
                App.getAuthentication().getSettings().setLastSyncDate(null);
                App.getAuthentication().manualSync();
            }
            try {
                RoomDBTester.clearUserData(App.getDatabaseController(), App.getAuthentication().getUser());
            } catch (InterruptedException ignored) {
            }
            App.getAuthentication().getSettings().setAutomaticServerSync(false);
            App.getAuthentication().getSettings().setLastSyncDate(null);
            App.getAuthentication().logout();
        } else {
            fail("Login doesn't work - Check if server is running!");
        }

        // Typing Login data:
        onView(withId(R.id.edit_username))
                .perform(CustomEspressoAddOns.replaceTextInInputTextLayout(
                        UsernameParser.usernameToDisplayedVersion(USERNAME)
                ));
        onView(withId(R.id.edit_password))
                .perform(CustomEspressoAddOns.replaceTextInInputTextLayout(PASSWORD));

        // Login:
        onView(withId(R.id.login_button)).perform(click());

        // Assert in MainMenuActivity
        onView(withId(R.id.main_menu_activity)).check(matches(isDisplayed()));

        // Go to categories
        onView(withId(R.id.main_menu_view_categories)).perform(click());

        // Check that recyclerView is empty
        onView(withId(R.id.recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewIsEmpty()));

        // Return to Main Activity
        pressBack();

        // Assert in MainMenuActivity
        onView(withId(R.id.main_menu_activity)).check(matches(isDisplayed()));

        // manual sync
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_item_sync)).perform(click());

        // Go to categories
        onView(withId(R.id.main_menu_view_categories)).perform(click());

        // Check that recyclerView has 1 item
        onView(withId(R.id.recyclerView)).check(matches(CustomEspressoAddOns.recyclerViewHasItemCount(1)));


        App.getAuthentication().removeLoginDataFromPrefs();
    }

}
