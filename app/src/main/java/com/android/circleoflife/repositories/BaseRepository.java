package com.android.circleoflife.repositories;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Every repository extends this class.
 * It offers a method for doing a task on a background thread. e.g. call an insert-method on {@link com.android.circleoflife.database.control.DatabaseController}
 */
public abstract class BaseRepository {

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
     * Runs a task on a background-thread. After the task is fini
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

}
