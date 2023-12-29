package com.android.circleoflife.logging.control;

import com.android.circleoflife.database.control.observers.DatabaseObserver;
import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;

/**
 * Class which implements DBLogger. Follows the singleton principle
 */
public class DBLoggerImpl implements DBLogger, DatabaseObserver {

    private static volatile DBLogger instance;

    /**
     * Singleton oriented getInstance() method.
     * @return only existing instance of this class
     */
    public static DBLogger getInstance() {
        if (instance == null) {
            synchronized (DBLoggerImpl.class) {
                if (instance == null) {
                    instance = new DBLoggerImpl();
                }
            }
        }
        return instance;
    }

    private boolean active;

    public DBLoggerImpl() {
        active = true;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void onInsertUser(User user) {

    }


    @Override
    public void onUpdateUser(User user) {

    }

    @Override
    public void onDeleteUser(User user) {

    }

    @Override
    public void onInsertCategory(Category category) {

    }

    @Override
    public void onUpdateCategory(Category category) {

    }

    @Override
    public void onDeleteCategory(Category category) {

    }

    @Override
    public void onInsertCycle(Cycle cycle) {

    }

    @Override
    public void onUpdateCycle(Cycle cycle) {

    }

    @Override
    public void onDeleteCycle(Cycle cycle) {

    }

    @Override
    public void onInsertTodo(Todo todos) {

    }

    @Override
    public void onUpdateTodo(Todo todo) {

    }

    @Override
    public void onDeleteTodo(Todo todo) {

    }

    @Override
    public void onInsertAccomplishment(Accomplishment accomplishment) {

    }

    @Override
    public void onUpdateAccomplishment(Accomplishment accomplishment) {

    }

    @Override
    public void onDeleteAccomplishment(Accomplishment accomplishment) {

    }
}
