package com.android.circleoflife.logging.control;

import android.util.Log;

import com.android.circleoflife.database.control.DatabaseController;
import com.android.circleoflife.database.control.observers.DatabaseObserver;
import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

/**
 * implements DatabaseObserver. An instance of {@link DatabaseController} is passed in the constructor as parameter,
 * an instance of this class assigns to the db as observer, and on every change it writes a log to it.
 */
public class DBLogger implements DatabaseObserver {
    private static final String TAG = DBLogger.class.getSimpleName();

    private boolean active;
    private final DatabaseController db;

    public DBLogger(DatabaseController db) {
        this.db = db;
        active = true;
        db.addObserver(this);
    }

    private void saveLogToDB(DBLog<?> log) {
        Log.d(TAG, "saveLogToDB: " + log);
        db.insertLog(log);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void onInsertUser(User user) {
        //saveLogToDB(new DBLog<>(user, DBLog.ChangeMode.INSERT));
    }


    @Override
    public void onUpdateUser(User user) {
        saveLogToDB(new DBLog<>(user, DBLog.ChangeMode.UPDATE));
    }

    @Override
    public void onDeleteUser(User user) {
        //saveLogToDB(new DBLog<>(user, DBLog.ChangeMode.DELETE));
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
