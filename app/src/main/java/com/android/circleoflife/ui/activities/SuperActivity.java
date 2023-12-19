package com.android.circleoflife.ui.activities;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Provides some basic methods which are used all the time for all activities throughout the project.
 */
public class SuperActivity extends AppCompatActivity {

    /**
     * Inflates a menu with given ID and colors EVERY Icon of every item to the color textColorSecondary specified by the theme<br>
     * Should be used like this:
     * <pre>{@code
     *     @Override
     *     public boolean onCreateOptionsMenu(Menu menu) {
     *         setUpMenu(menu, R.menu.menu_id);
     *         // Other code
     *         return true;
     *     }
     * }</pre>
     *
     * @param menu   menu item which is passed as parameter in {@link AppCompatActivity#onCreateOptionsMenu(Menu) onCreateOptionsMenu(Menu)}
     * @param menuID menuID which should be only accessed via {@code R.menu.menu_id}
     */
    protected void setUpMenu(Menu menu, int menuID) {
        getMenuInflater().inflate(menuID, menu);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        @SuppressLint("Recycle")
        TypedArray arr = obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorSecondary});
        int primaryColor = arr.getColor(0, -1);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

    }

    /**
     * Executes given task on a background-thread
     * @param task task
     */
    protected void executeInBackground(@NonNull Runnable task) {
        executeInBackground(task, null);
    }

    /**
     * Executes a given task on a background thread and afterwards executes the after-task on the main-thread
     * @param task task
     * @param after after-task
     */
    protected void executeInBackground(@NonNull Runnable task, @Nullable Runnable after) {
        ExecutorService service = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        service.execute(() -> {
            task.run();
            if (after != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(after);
            }
        });
    }

    /**
     * Executes a given task which should return a value of type T. Execution happens on a background-thread.
     * Afterwards the after-task is executed on the main-thread with the result of task passed as parameter.
     *
     * @param task  task to be executed on a background-thread
     * @param after after-task to be executed on the main-thread
     * @param <T>   Type the task needs to return
     */
    protected <T> void executeInBackground(@NonNull Callable<T> task, @NonNull Consumer<T> after) {
        ExecutorService service = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        service.execute(() -> {
            try {
                final T result = task.call();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> after.accept(result));
            } catch (Exception e) {
                Log.w("BackgroundThread", "Exception in background-thread ", e);
            }
        });
    }

}
