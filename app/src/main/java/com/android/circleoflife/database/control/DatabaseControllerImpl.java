package com.android.circleoflife.database.control;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.circleoflife.application.App;
import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.database.control.daos.BaseDao;
import com.android.circleoflife.database.control.observers.DatabaseObserver;
import com.android.circleoflife.database.models.*;
import com.android.circleoflife.database.models.additional.HasUserId;
import com.android.circleoflife.logging.model.DBLog;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Implementation of the {@link DatabaseController}. Offers methods for communicating with the database
 */
public class DatabaseControllerImpl implements DatabaseController {
    private static final String TAG = DatabaseController.class.getSimpleName();

    private static volatile DatabaseController instance;

    /**
     * Singleton-pattern. At all times there is only one instance of this class
     * @return only existing instance of this class
     */
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

    private DatabaseControllerImpl() {
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

    @Override
    public void syncWithServer(Authentication auth) {
        List<DBLog<?>> serverInstructions = new LinkedList<>();
        LocalDateTime newLastSyncDate = App.getSyncProtocol().sync(
                auth.getUser(),
                auth.getSettings().getLastSyncDate(),
                db.getLogDao().getLogsBetweenTimestamps(auth.getUser(), auth.getSettings().getLastSyncDate(), LocalDateTime.now()),
                serverInstructions
        );
        if (newLastSyncDate == null) {
            Log.d(TAG, "syncWithServer: Synchronisation failed");
        } else {
            Log.d(TAG, "syncWithServer: Synchronisation succeeded!");
            auth.getSettings().setLastSyncDate(newLastSyncDate);
            // executing server instructions:
            for (DBLog<?> log : serverInstructions) {
                if (executeLog(log)) {
                    insertLog(log);
                }
            }
        }
    }

    /**
     * Gets correct dao with {@link AppDatabase#getDao(Class)} and executes log on it
     * @param log log to be executed
     * @param <E> type of the log-object
     */
    private <E extends HasUserId> boolean executeLog(DBLog<E> log) {
        try {
            //noinspection unchecked
            BaseDao<E> dao = (BaseDao<E>) db.getDao(log.getChangedObject().getClass());
            dao.executeLog(log);
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    private void triggerObservers(Consumer<DatabaseObserver> observedMethod) {
        observers.stream()
                .filter(DatabaseObserver::isActive)
                .forEach(observedMethod);
    }

    @Override
    public void insertUsers(User... users) {
        db.getUserDao().insert(users);
        triggerObservers(o -> o.onInsertUsers(users));
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
    public LiveData<User> getUser(UUID userID) {
        return db.getUserDao().getUser(userID);
    }

    @Override
    public User getUserByUsername(String username) {
        return db.getUserDao().getUser(username);
    }

    @Override
    public void insertCategories(Category... categories) {
        db.getCategoryDao().insert(categories);
        triggerObservers(o -> o.onInsertCategories(categories));
    }

    @Override
    public void updateCategory(Category category) {
        db.getCategoryDao().update(category);
        triggerObservers(o -> o.onUpdateCategory(category));
    }

    @Override
    public void deleteCategory(Category category) {
        db.getCategoryDao().delete(category);
        triggerObservers(o -> o.onDeleteCategory(category));
    }

    @Override
    public LiveData<List<Category>> getAllCategories(User user) {
        return db.getCategoryDao().getAllCategories(user);
    }

    @Override
    public LiveData<List<Category>> getRootCategories(User user) {
        return db.getCategoryDao().getRootCategories(user);
    }

    @Override
    public LiveData<Category> getCategoryParent(Category category) {
        return db.getCategoryDao().getParent(category);
    }

    @Override
    public LiveData<List<Category>> getChildCategories(Category category) {
        return db.getCategoryDao().getChildCategories(category);
    }

    @Override
    public LiveData<List<Cycle>> getCycles(Category category) {
        return db.getCategoryDao().getCycles(category);
    }

    @Override
    public LiveData<List<Todo>> getTodos(Category category) {
        return db.getCategoryDao().getTodos(category);
    }

    @Override
    public void insertCycles(Cycle... cycles) {
        db.getCycleDao().insert(cycles);
        triggerObservers(o -> o.onInsertCycles(cycles));
    }

    @Override
    public void updateCycle(Cycle cycle) {
        db.getCycleDao().update(cycle);
        triggerObservers(o -> o.onUpdateCycle(cycle));
    }

    @Override
    public void deleteCycle(Cycle cycle) {
        db.getCycleDao().delete(cycle);
        triggerObservers(o -> o.onDeleteCycle(cycle));
    }

    @Override
    public LiveData<List<Cycle>> getAllCycles(User user) {
        return db.getCycleDao().getAllCycles(user);
    }

    @Override
    public LiveData<Category> getCategory(Cycle cycle) {
        return db.getCycleDao().getCategory(cycle);
    }

    @Override
    public LiveData<List<Accomplishment>> getAccomplishments(Cycle cycle) {
        return db.getCycleDao().getAccomplishments(cycle);
    }

    @Override
    public void insertTodos(Todo... todos) {
        db.getTodoDao().insert(todos);
        triggerObservers(o -> o.onInsertTodos(todos));
    }

    @Override
    public void updateTodo(Todo todo) {
        db.getTodoDao().update(todo);
        triggerObservers(o -> o.onUpdateTodo(todo));
    }

    @Override
    public void deleteTodo(Todo todo) {
        db.getTodoDao().delete(todo);
        triggerObservers(o -> o.onDeleteTodo(todo));
    }

    @Override
    public LiveData<List<Todo>> getAllTodos(User user) {
        return db.getTodoDao().getAllTodos(user);
    }

    @Override
    public LiveData<Category> getCategory(Todo todo) {
        return db.getTodoDao().getCategory(todo);
    }

    @Override
    public LiveData<List<Accomplishment>> getAccomplishments(Todo todo) {
        return db.getTodoDao().getAccomplishments(todo);
    }

    @Override
    public void insertAccomplishment(Accomplishment... accomplishments) {
        db.getAccomplishmentDao().insert(accomplishments);
        triggerObservers(o -> o.onInsertAccomplishments(accomplishments));
    }

    @Override
    public void updateAccomplishment(Accomplishment accomplishment) {
        db.getAccomplishmentDao().update(accomplishment);
        triggerObservers(o -> o.onUpdateAccomplishment(accomplishment));
    }

    @Override
    public void deleteAccomplishment(Accomplishment accomplishment) {
        db.getAccomplishmentDao().delete(accomplishment);
        triggerObservers(o -> o.onDeleteAccomplishment(accomplishment));
    }

    @Override
    public LiveData<List<Accomplishment>> getAllAccomplishments(User user) {
        return db.getAccomplishmentDao().getAllAccomplishments(user);
    }

    @Override
    public LiveData<List<Accomplishment>> getAllAccomplishments(Category category) {
        return db.getAccomplishmentDao().getAllAccomplishments(category);
    }

    @Override
    public LiveData<List<Accomplishment>> getAllAccomplishmentsAfterTimestamp(User user, @NonNull LocalDateTime timestamp) {
        return db.getAccomplishmentDao().getAllAccomplishmentsAfterTimestamp(user, timestamp);
    }

    @Override
    public LiveData<List<Accomplishment>> getAllAccomplishmentsBeforeTimestamp(User user, @NonNull LocalDateTime timestamp) {
        return db.getAccomplishmentDao().getAllAccomplishmentsBeforeTimestamp(user, timestamp);
    }

    @Override
    public LiveData<List<Accomplishment>> getAllAccomplishmentsBetweenTimestamps(User user, LocalDateTime timestamp1, LocalDateTime timestamp2) {
        return db.getAccomplishmentDao().getAllAccomplishmentsBetweenTimestamps(user, timestamp1, timestamp2);
    }

    @Override
    public void insertLog(DBLog<?>... logs) {
        db.getLogDao().insert(
                Arrays.stream(logs)
                        .map(LogEntity::new)
                        .toArray(LogEntity[]::new)
        );
    }
}
