package com.android.circleoflife.database.control.observers;

import com.android.circleoflife.database.models.*;

public interface DatabaseObserver {

    boolean isActive();

    // Users
    void onInsertUsers(User... user);

    void onUpdateUser(User user);

    void onDeleteUser(User user);


    // Categories
    void onInsertCategories(Category... categories);

    void onUpdateCategory(Category category);

    void onDeleteCategory(Category category);


    // Cycles
    void onInsertCycles(Cycle... cycles);

    void onUpdateCycle(Cycle cycle);

    void onDeleteCycle(Cycle cycle);


    // To Do
    void onInsertTodos(Todo... todos);

    void onUpdateTodo(Todo todo);

    void onDeleteTodo(Todo todo);


    // Accomplishment
    void onInsertAccomplishment(Accomplishment... accomplishments);

    void onUpdateAccomplishment(Accomplishment accomplishment);

    void onDeleteAccomplishment(Accomplishment accomplishment);

}
