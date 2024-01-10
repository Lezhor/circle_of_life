package com.android.circleoflife.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.MenuCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.auth.UserSettings;
import com.android.circleoflife.auth.UsernameParser;
import com.android.circleoflife.database.models.type_converters.LocalDateTimeConverter;
import com.android.circleoflife.ui.activities.auth.LoginActivity;
import com.android.circleoflife.ui.activities.categories.root.RootCategoriesActivity;

import java.time.LocalDateTime;

public class MainMenuActivity extends SuperActivity {
    private static final String TAG = MainMenuActivity.class.getSimpleName();

    private MenuItem syncEnabledItem;
    private MenuItem autoSyncEnabledItem;
    private MenuItem syncItem;

    private TextView usernameDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.i(TAG, "onCreate: Actionbar is null!");
        } else {
            actionBar.setTitle(R.string.app_name);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT, this::finish);
        }

        usernameDisplay = findViewById(R.id.main_menu_username_display);

        View rootButton = findViewById(R.id.main_menu_view_categories);
        rootButton.setOnClickListener(v -> goToRootCategoriesActivity());
        View cycleButton = findViewById(R.id.main_menu_view_cycles);
        cycleButton.setOnClickListener(v -> goToCyclesActivity());
        View todoButton = findViewById(R.id.main_menu_view_todos);
        todoButton.setOnClickListener(v -> goToTodoActivity());

        if (App.getAuthentication().authenticated()) {
            executeInBackground(
                    () -> App.getDatabaseController().getLogs(App.getAuthentication().getUser(), App.getAuthentication().getSettings().getLastSyncDate(), LocalDateTime.now(App.SERVER_TIMEZONE)),
                    logs -> {
                        Log.d(TAG, "LastSyncDate: " + LocalDateTimeConverter.localDateTimeToString(App.getAuthentication().getSettings().getLastSyncDate()));
                        Log.d(TAG, "now: " + LocalDateTimeConverter.localDateTimeToString(LocalDateTime.now(App.SERVER_TIMEZONE)));
                        Log.d(TAG, "Printing logs of user " + App.getAuthentication().getUser());
                        for (int i = 0; i < logs.length; i++) {
                            Log.d(TAG, (i + 1) + ") " + logs[i]);
                        }
                    }
            );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (App.getAuthentication().authenticated()) {
            usernameDisplay.setText(UsernameParser.usernameToDisplayedVersion(App.getAuthentication().getUser().getUsername()));
            Log.d(TAG, "LastSyncDate: " + LocalDateTimeConverter.localDateTimeToString(App.getAuthentication().getSettings().getLastSyncDate()));
            autoSync();
        } else {
            finish();
        }

    }

    private void autoSync() {
        executeInBackground(App.getAuthentication()::autoSync);
    }

    /**
     * Starts {@link RootCategoriesActivity}
     */
    private void goToRootCategoriesActivity() {
        Log.d(TAG, "goToRootCategoriesActivity: clicked");
        Intent intent = new Intent(this, RootCategoriesActivity.class);
        startActivity(intent);
    }

    private void goToCyclesActivity() {
        Log.d(TAG, "goToCyclesActivity: clicked");
        // TODO: 10.01.2024 Go to Cycles Activity
        Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
    }

    private void goToTodoActivity() {
        Log.d(TAG, "goToTodoActivity: clicked");
        // TODO: 10.01.2024 Go to Todo Activity
        Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setUpMenu(menu, R.menu.main_menu_options_menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        syncEnabledItem = menu.findItem(R.id.item_sync_enabled);
        autoSyncEnabledItem = menu.findItem(R.id.item_auto_sync_enabled);
        syncItem = menu.findItem(R.id.item_sync);
        UserSettings settings = App.getAuthentication().getSettings();
        syncEnabled(settings.isServerSyncEnabled());
        autoSyncEnabled(settings.isAutomaticServerSync());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_sync) {
            sync();
        } else if (item.getItemId() == R.id.item_sync_enabled) {
            syncEnabled(!item.isChecked());
        } else if (item.getItemId() == R.id.item_auto_sync_enabled) {
            autoSyncEnabled(!item.isChecked());
        } else if (item.getItemId() == R.id.item_logout) {
            logout();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Called by {@link #onOptionsItemSelected(MenuItem)}, if sync is selected
     */
    private void sync() {
        Log.d(TAG, "sync: pressed");
        executeInBackground(App.getAuthentication()::manualSync);
    }

    /**
     * Called by {@link #onOptionsItemSelected(MenuItem)}, if syncEnabled is selected
     */
    private void syncEnabled(boolean checked) {
        Log.d(TAG, "syncEnabled: pressed");
        syncEnabledItem.setChecked(checked);
        autoSyncEnabledItem.setChecked(checked);
        autoSyncEnabledItem.setEnabled(checked);
        syncItem.setEnabled(checked);
        App.getAuthentication().getSettings().setServerSyncEnabled(checked);
    }

    /**
     * Called by {@link #onOptionsItemSelected(MenuItem)}, if autoSyncEnabled is selected
     */
    private void autoSyncEnabled(boolean checked) {
        Log.d(TAG, "autoSyncEnabled: pressed");
        autoSyncEnabledItem.setChecked(checked);
        App.getAuthentication().getSettings().setAutomaticServerSync(checked);
    }

    /**
     * Logs out and opens login activity
     */
    private void logout() {
        Log.d(TAG, "logout: pressed");
        App.getAuthentication().logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}