package com.android.circleoflife.database.control;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.android.circleoflife.database.models.*;

import java.util.List;

@Dao
interface AppDao {
    // TODO: 05.12.2023 Create DAO methods

    @RawQuery(observedEntities = {Category.class, Cycle.class, Todo.class, Accomplishment.class})
    LiveData<List<String>> executeQuery(SupportSQLiteQuery query);


    // ----------------------------------   User Methods   ----------------------------------------
    @Insert()
    void addUser(User user);

    @Update()
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM users WHERE uid = :uid")
    User getUser(int uid);

    @Query("SELECT * FROM users WHERE username LIKE :username LIMIT 1")
    User getUser(String username);


    // --------------------------------   Category Methods   --------------------------------------

    @Insert
    void addCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("SELECT * FROM categories WHERE uid = :userID ORDER BY category_name")
    List<Category> getAllCategories(int userID);

    @Transaction
    default List<Category> getAllCategories(User user) {
        return getAllCategories(user.getId());
    }

    @Query("SELECT * FROM categories WHERE uid = :userID AND parent_category = NULL ORDER BY category_name")
    List<Category> getRootCategories(int userID);

    @Transaction
    default List<Category> getRootCategories(User user) {
        return getRootCategories(user.getId());
    }

    @Nullable
    @Query("SELECT c1.uid, c1.category_name, c1.parent_category FROM categories AS c1 JOIN categories AS c2 ON c1.uid = c2.uid AND c1.category_name LIKE c2.parent_category AND c2.uid = :userID AND c2.category_name LIKE :categoryName LIMIT 1")
    Category getCategoryParent(int userID, String categoryName);

    @Transaction
    default Category getParent(Category category) {
        return getCategoryParent(category.getUserID(), category.getName());
    }

    @Query("SELECT c1.uid, c1.category_name, c1.parent_category FROM categories AS c1 JOIN categories AS c2 ON c1.uid = c2.uid AND c1.parent_category LIKE c2.category_name AND c2.uid = :userID AND c2.category_name LIKE :categoryName ORDER BY c1.category_name")
    List<Category> getChildCategories(int userID, String categoryName);

    @Transaction
    default List<Category> getChildCategories(Category category) {
        return getChildCategories(category.getUserID(), category.getName());
    }

    // ---------------------------------   Cycle Methods   ----------------------------------------


    @Insert
    void addCycle(Cycle cycle);

    @Update
    void updateCycle(Cycle cycle);

    @Delete
    void deleteCycle(Cycle cycle);

    // TODO: 08.12.2023 Continue here

    // ----------------------------------   To-Do Methods   ---------------------------------------

    @Insert
    void addTodo(Todo todo);

    @Update
    void updateTodo(Todo todo);

    @Delete
    void deleteTodo(Todo todo);


    // ------------------------------   Accomplishment Methods   ----------------------------------

    @Insert
    void addAccomplishment(Accomplishment accomplishment);

    @Update
    void updateAccomplishment(Accomplishment accomplishment);

    @Delete
    void deleteAccomplishment(Accomplishment accomplishment);


    // ------------------------------------   Log Methods   ---------------------------------------

    @Insert
    void addLog(LogEntity log);

    @Update
    void updateLog(LogEntity log);

    @Delete
    void deleteLog(LogEntity log);



}
