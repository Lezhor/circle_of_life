package com.android.circleoflife.database.control;

import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.database.control.observers.DatabaseObserver;
import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;
import com.android.circleoflife.communication.protocols.SyncProtocol;

import java.util.Collection;

// TODO: 03.12.2023 DATABASE CONTROLLER
public interface DatabaseController {

    // Observer methods
    
    void addObserver(DatabaseObserver observer);

    void removeObserver(DatabaseObserver observer);

    // TODO: 10.12.2023 Get methods (from DAOs)

    // Users
    void insertUsers(User... user);

    void insertUsers(Collection<User> user);

    void updateUser(User user);

    void deleteUser(User user);


    // Categories
    void insertCategories(Category... categories);

    void insertCategories(Collection<Category> categories);

    void updateCategory(Category category);

    void deleteCategory(Category category);


    // Cycles
    void insertCycles(Cycle... cycles);

    void insertCycles(Collection<Cycle> cycles);

    void updateCycle(Cycle cycle);

    void deleteCycle(Cycle cycle);


    // To Do
    void insertTodos(Todo... todos);

    void insertTodos(Collection<Todo> todos);

    void updateTodo(Todo todo);

    void deleteTodo(Todo todo);


    // Accomplishment
    void insertAccomplishment(Accomplishment... accomplishments);

    void insertAccomplishment(Collection<Accomplishment> accomplishments);

    void updateAccomplishment(Accomplishment accomplishment);

    void deleteAccomplishment(Accomplishment accomplishment);




}
