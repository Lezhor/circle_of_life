package com.android.circleoflife.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.auth.UserSettings;
import com.android.circleoflife.ui.activities.auth.LoginActivity;

public class MainMenuActivity extends SuperActivity {
    private static final String TAG = MainMenuActivity.class.getSimpleName();

    private MenuItem syncEnabledItem;
    private MenuItem autoSyncEnabledItem;

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

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (!App.getAuthentication().authenticated()) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setUpMenu(menu, R.menu.main_menu_options_menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        syncEnabledItem = menu.findItem(R.id.item_sync_enabled);
        autoSyncEnabledItem = menu.findItem(R.id.item_auto_sync_enabled);
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
        // TODO: 09.01.2024 execute sync method
        Log.d(TAG, "sync: pressed");
        Toast.makeText(this, "Syncing (Under construction)", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called by {@link #onOptionsItemSelected(MenuItem)}, if syncEnabled is selected
     */
    private void syncEnabled(boolean checked) {
        Log.d(TAG, "syncEnabled: pressed");
        syncEnabledItem.setChecked(checked);
        autoSyncEnabledItem.setChecked(checked);
        autoSyncEnabledItem.setEnabled(checked);
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