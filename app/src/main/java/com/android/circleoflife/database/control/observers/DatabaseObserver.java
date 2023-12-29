package com.android.circleoflife.database.control.observers;

import com.android.circleoflife.database.models.*;

/**
 * Methods are triggerd in the {@link com.android.circleoflife.database.control.DatabaseController}
 */
public interface DatabaseObserver {

    boolean isActive();

    // Users
    void onInsertUser(User user);
    default void onInsertUsers(User... users) {
        for (User user : users) {
            onInsertUser(user);
        }
    }

    void onUpdateUser(User user);

    void onDeleteUser(User user);


    // Categories
    void onInsertCategory(Category category);
    default void onInsertCategories(Category... categories) {
        for (Category category : categories) {
            onInsertCategory(category);
        }
    }

    void onUpdateCategory(Category category);

    void onDeleteCategory(Category category);


    // Cycles
    void onInsertCycle(Cycle cycle);

    default void onInsertCycles(Cycle... cycles) {
        for (Cycle cycle : cycles) {
            onInsertCycle(cycle);
        }
    }

    void onUpdateCycle(Cycle cycle);

    void onDeleteCycle(Cycle cycle);


    // To Do
    void onInsertTodo(Todo todos);

    default void onInsertTodos(Todo... todos) {
        for (Todo todo : todos) {
            onInsertTodo(todo);
        }
    }

    void onUpdateTodo(Todo todo);

    void onDeleteTodo(Todo todo);


    // Accomplishment
    void onInsertAccomplishment(Accomplishment accomplishment);

    default void onInsertAccomplishments(Accomplishment... accomplishments) {
        for (Accomplishment accomplishment : accomplishments) {
            onInsertAccomplishment(accomplishment);
        }
    }

    void onUpdateAccomplishment(Accomplishment accomplishment);

    void onDeleteAccomplishment(Accomplishment accomplishment);

}
