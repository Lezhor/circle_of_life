package com.android.circleoflife.logging.control;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.control.observers.DatabaseObserver;
import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.LogEntity;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

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

    private void saveLogToDB(DBLog<?> log) {
        App.getDatabaseController().insertLog(new LogEntity(log));
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void onInsertUser(User user) {
        saveLogToDB(new DBLog<>(user, DBLog.ChangeMode.INSERT));
    }


    @Override
    public void onUpdateUser(User user) {
        saveLogToDB(new DBLog<>(user, DBLog.ChangeMode.UPDATE));
    }

    @Override
    public void onDeleteUser(User user) {
        saveLogToDB(new DBLog<>(user, DBLog.ChangeMode.DELETE));
    }

    @Override
    public void onInsertCategory(Category category) {
        saveLogToDB(new DBLog<>(category, DBLog.ChangeMode.INSERT));
    }

    @Override
    public void onUpdateCategory(Category category) {
        saveLogToDB(new DBLog<>(category, DBLog.ChangeMode.UPDATE));
    }

    @Override
    public void onDeleteCategory(Category category) {
        saveLogToDB(new DBLog<>(category, DBLog.ChangeMode.DELETE));
    }

    @Override
    public void onInsertCycle(Cycle cycle) {
        saveLogToDB(new DBLog<>(cycle, DBLog.ChangeMode.INSERT));
    }

    @Override
    public void onUpdateCycle(Cycle cycle) {
        saveLogToDB(new DBLog<>(cycle, DBLog.ChangeMode.UPDATE));
    }

    @Override
    public void onDeleteCycle(Cycle cycle) {
        saveLogToDB(new DBLog<>(cycle, DBLog.ChangeMode.DELETE));
    }

    @Override
    public void onInsertTodo(Todo todo) {
        saveLogToDB(new DBLog<>(todo, DBLog.ChangeMode.INSERT));
    }

    @Override
    public void onUpdateTodo(Todo todo) {
        saveLogToDB(new DBLog<>(todo, DBLog.ChangeMode.UPDATE));
    }

    @Override
    public void onDeleteTodo(Todo todo) {
        saveLogToDB(new DBLog<>(todo, DBLog.ChangeMode.DELETE));
    }

    @Override
    public void onInsertAccomplishment(Accomplishment accomplishment) {
        saveLogToDB(new DBLog<>(accomplishment, DBLog.ChangeMode.INSERT));
    }

    @Override
    public void onUpdateAccomplishment(Accomplishment accomplishment) {
        saveLogToDB(new DBLog<>(accomplishment, DBLog.ChangeMode.UPDATE));
    }

    @Override
    public void onDeleteAccomplishment(Accomplishment accomplishment) {
        saveLogToDB(new DBLog<>(accomplishment, DBLog.ChangeMode.DELETE));
    }
}
