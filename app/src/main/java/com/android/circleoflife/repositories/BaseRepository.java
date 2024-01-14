package com.android.circleoflife.repositories;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Every repository extends this class.
 * It offers a method for doing a task on a background thread. e.g. call an insert-method on {@link com.android.circleoflife.database.control.DatabaseController}
 */
public abstract class BaseRepository {
    private static final String TAG = "BaseRepository";

    private final ExecutorService service;
    private final Handler handler;

    /**
     * Initializes the executor-service and the handler
     */
    protected BaseRepository() {
        service = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Runs a task on a background-thread
     * @param task to be executed
     * @see BaseRepository#doInBackground(Runnable, Runnable)
     */
    protected void doInBackground(Runnable task) {
        doInBackground(task, null);
    }

    /**
     * Runs a task on a background-thread. After the task is finished, executes the after-task on the main-thread
     * @param task to be executed on background-thread
     * @param after to be executed on main-thread
     */
    protected void doInBackground(Runnable task, Runnable after) {
        service.execute(() -> {
            task.run();
            if (after != null) {
                handler.post(after);
            }
        });
    }

    /**
     * Executes task in background and afterwards executes 'after' with the result of 'task' as parameter
     * @param task task
     * @param after after
     * @param <T> Type of result
     */
    protected <T> void doInBackground(Callable<T> task, Consumer<T> after) {
        service.execute(() -> {
            try {
                T result = task.call();
                handler.post(() -> after.accept(result));
            } catch (Exception e) {
                Log.w(TAG, "doInBackground: task failed", e);
            }
        });
    }

}
