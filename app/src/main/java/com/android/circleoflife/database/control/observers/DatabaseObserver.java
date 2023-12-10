package com.android.circleoflife.database.control.observers;

import com.android.circleoflife.database.models.*;

import java.util.Collection;

public interface DatabaseObserver {

    // TODO: 10.12.2023 Change onInsert(Collection<T>) to be default and called varargs method

    // Users
    void onInsertUsers(User... user);

    void onInsertUsers(Collection<User> user);

    void onUpdateUser(User user);

    void onDeleteUser(User user);


    // Categories
    void onInsertCategories(Category... categories);

    void onInsertCategories(Collection<Category> categories);

    void onUpdateCategory(Category category);

    void onDeleteCategory(Category category);


    // Cycles
    void onInsertCycles(Cycle... cycles);

    void onInsertCycles(Collection<Cycle> cycles);

    void onUpdateCycle(Cycle cycle);

    void onDeleteCycle(Cycle cycle);


    // To Do
    void onInsertTodos(Todo... todos);

    void onInsertTodos(Collection<Todo> todos);

    void onUpdateTodo(Todo todo);

    void onDeleteTodo(Todo todo);


    // Accomplishment
    void onInsertAccomplishment(Accomplishment... accomplishments);

    void onInsertAccomplishment(Collection<Accomplishment> accomplishments);

    void onUpdateAccomplishment(Accomplishment accomplishment);

    void onDeleteAccomplishment(Accomplishment accomplishment);

}
