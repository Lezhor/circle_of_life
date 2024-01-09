package com.android.circleoflife.database.control;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.database.control.observers.DatabaseObserver;
import com.android.circleoflife.database.models.*;
import com.android.circleoflife.logging.model.DBLog;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * This is the interface via which each component should communicate with the database.<br>
 * It calls methods in the {@link AppDatabase} and also notifies any present {@link DatabaseObserver observers}
 * if necessary.
 */
public interface DatabaseController {

    // Observer methods

    /**
     * Adds a observer to the observer-list.<br>
     * Upon adding any db-method which affects the database will trigger the corresponding method in the observer.<br>
     * e.g. {@link DatabaseController#updateCategory(Category)} will trigger {@link DatabaseObserver#onUpdateCategory(Category)}
     * @param observer observer
     */
    void addObserver(DatabaseObserver observer);

    /**
     * Removes observer from observer-list
     * @param observer observer
     */
    void removeObserver(DatabaseObserver observer);

    /**
     * Synchronizes database with server by calling the {@link com.android.circleoflife.communication.protocols.SyncProtocol#sync(User, LocalDateTime, DBLog[], List)} method.
     * @param auth the SyncProtocol-parameters are retrieved from this parameter: user, lastSyncDate...
     */
    void syncWithServer(Authentication auth);


    // Users
    void insertUsers(User... users);

    default void insertUsers(Collection<User> users) {
        insertUsers(users.stream().toArray(User[]::new));
    }

    void updateUser(User user);

    void deleteUser(User user);

    LiveData<User> getUser(UUID userID);

    User getUserByUsername(String username);


    // Categories
    void insertCategories(Category... categories);

    default void insertCategories(Collection<Category> categories) {
        insertCategories(categories.stream().toArray(Category[]::new));
    }

    void updateCategory(Category category);

    void deleteCategory(Category category);

    LiveData<List<Category>> getAllCategories(User user);

    LiveData<List<Category>> getRootCategories(User user);

    LiveData<Category> getCategoryParent(Category category);

    LiveData<List<Category>> getChildCategories(Category category);

    LiveData<List<Cycle>> getCycles(Category category);

    LiveData<List<Todo>> getTodos(Category category);


    // Cycles
    void insertCycles(Cycle... cycles);

    default void insertCycles(Collection<Cycle> cycles) {
        insertCycles(cycles.stream().toArray(Cycle[]::new));
    }

    void updateCycle(Cycle cycle);

    void deleteCycle(Cycle cycle);

    LiveData<List<Cycle>> getAllCycles(User user);

    LiveData<Category> getCategory(Cycle cycle);

    LiveData<List<Accomplishment>> getAccomplishments(Cycle cycle);


    // To Do
    void insertTodos(Todo... todos);

    default void insertTodos(Collection<Todo> todos) {
        insertTodos(todos.stream().toArray(Todo[]::new));
    }

    void updateTodo(Todo todo);

    void deleteTodo(Todo todo);

    LiveData<List<Todo>> getAllTodos(User user);

    LiveData<Category> getCategory(Todo todo);

    LiveData<List<Accomplishment>> getAccomplishments(Todo todo);


    // Accomplishment
    void insertAccomplishment(Accomplishment... accomplishments);

    default void insertAccomplishment(Collection<Accomplishment> accomplishments) {
        insertAccomplishment(accomplishments.stream().toArray(Accomplishment[]::new));
    }

    void updateAccomplishment(Accomplishment accomplishment);

    void deleteAccomplishment(Accomplishment accomplishment);

    LiveData<List<Accomplishment>> getAllAccomplishments(User user);

    LiveData<List<Accomplishment>> getAllAccomplishments(Category category);

    LiveData<List<Accomplishment>> getAllAccomplishmentsAfterTimestamp(User user, @NonNull LocalDateTime timestamp);

    LiveData<List<Accomplishment>> getAllAccomplishmentsBeforeTimestamp(User user, @NonNull LocalDateTime timestamp);

    LiveData<List<Accomplishment>> getAllAccomplishmentsBetweenTimestamps(User user, LocalDateTime timestamp1, LocalDateTime timestamp2);


    // Logs

    void insertLog(DBLog<?>... logs);

    default void insertLog(Collection<DBLog<?>> logs) {
        insertLog(logs.toArray(DBLog[]::new));
    }
}
