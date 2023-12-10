package com.android.circleoflife.database.control;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.control.observers.DatabaseObserver;
import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

public class DatabaseControllerImpl implements DatabaseController {

    private static volatile DatabaseController instance;

    public static DatabaseController getInstance() {
        if (instance == null) {
            synchronized (DatabaseControllerImpl.class) {
                if (instance == null) {
                    instance = new DatabaseControllerImpl();
                }
            }
        }
        return instance;
    }

    private final Collection<DatabaseObserver> observers;

    private final AppDatabase db;

    public DatabaseControllerImpl() {
        this.observers = new LinkedList<>();
        db = AppDatabase.getInstance(App.getApplicationContext());
    }

    @Override
    public void addObserver(DatabaseObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DatabaseObserver observer) {
        observers.remove(observer);
    }

    private void triggerObservers(Consumer<DatabaseObserver> observedMethod) {
        observers.stream()
                //.filter() // TODO - Filter for active?
                .forEach(observedMethod);
    }

    @Override
    public void insertUsers(User... user) {
        if (user.length == 1) {
            db.getUserDao().insert(user[0]);
        } else {
            db.getUserDao().insertAll(user);
        }
        triggerObservers(o -> o.onInsertUsers(user));
    }

    @Override
    public void insertUsers(Collection<User> user) {
        db.getUserDao().insertAll(user);
        triggerObservers(o -> o.onInsertUsers(user));
    }

    @Override
    public void updateUser(User user) {
        db.getUserDao().update(user);
        triggerObservers(o -> o.onUpdateUser(user));
    }

    @Override
    public void deleteUser(User user) {
        db.getUserDao().delete(user);
        triggerObservers(o -> o.onDeleteUser(user));
    }

    @Override
    public void insertCategories(Category... categories) {

    }

    @Override
    public void insertCategories(Collection<Category> categories) {

    }

    @Override
    public void updateCategory(Category category) {

    }

    @Override
    public void deleteCategory(Category category) {

    }

    @Override
    public void insertCycles(Cycle... cycles) {

    }

    @Override
    public void insertCycles(Collection<Cycle> cycles) {

    }

    @Override
    public void updateCycle(Cycle cycle) {

    }

    @Override
    public void deleteCycle(Cycle cycle) {

    }

    @Override
    public void insertTodos(Todo... todos) {

    }

    @Override
    public void insertTodos(Collection<Todo> todos) {

    }

    @Override
    public void updateTodo(Todo todo) {

    }

    @Override
    public void deleteTodo(Todo todo) {

    }

    @Override
    public void insertAccomplishment(Accomplishment... accomplishments) {

    }

    @Override
    public void insertAccomplishment(Collection<Accomplishment> accomplishments) {

    }

    @Override
    public void updateAccomplishment(Accomplishment accomplishment) {

    }

    @Override
    public void deleteAccomplishment(Accomplishment accomplishment) {

    }
}
