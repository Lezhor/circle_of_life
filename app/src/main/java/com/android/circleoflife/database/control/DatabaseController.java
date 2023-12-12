package com.android.circleoflife.database.control;

import com.android.circleoflife.database.control.observers.DatabaseObserver;
import com.android.circleoflife.database.models.*;

import java.util.Collection;

// TODO: 03.12.2023 DATABASE CONTROLLER
public interface DatabaseController {

    // Observer methods
    
    void addObserver(DatabaseObserver observer);

    void removeObserver(DatabaseObserver observer);

    // TODO: 10.12.2023 Get methods (from DAOs)

    // Users
    void insertUsers(User... users);

    default void insertUsers(Collection<User> users) {
        insertUsers(users.stream().toArray(User[]::new));
    }

    void updateUser(User user);

    void deleteUser(User user);


    // Categories
    void insertCategories(Category... categories);

    default void insertCategories(Collection<Category> categories) {
        insertCategories(categories.stream().toArray(Category[]::new));
    }

    void updateCategory(Category category);

    void deleteCategory(Category category);


    // Cycles
    void insertCycles(Cycle... cycles);

    default void insertCycles(Collection<Cycle> cycles) {
        insertCycles(cycles.stream().toArray(Cycle[]::new));
    }

    void updateCycle(Cycle cycle);

    void deleteCycle(Cycle cycle);


    // To Do
    void insertTodos(Todo... todos);

    default void insertTodos(Collection<Todo> todos) {
        insertTodos(todos.stream().toArray(Todo[]::new));
    }

    void updateTodo(Todo todo);

    void deleteTodo(Todo todo);


    // Accomplishment
    void insertAccomplishment(Accomplishment... accomplishments);

    default void insertAccomplishment(Collection<Accomplishment> accomplishments) {
        insertAccomplishment(accomplishments.stream().toArray(Accomplishment[]::new));
    }

    void updateAccomplishment(Accomplishment accomplishment);

    void deleteAccomplishment(Accomplishment accomplishment);




}
