package com.android.circleoflife.database.control.daos;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;

import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;

import java.util.List;
import java.util.UUID;

@Dao
public interface CategoryDao extends BaseDao<Category> {


    @Query("SELECT * FROM categories WHERE userID = :userID ORDER BY category_name")
    LiveData<List<Category>> getAllCategories(UUID userID);

    @Ignore
    default LiveData<List<Category>> getAllCategories(User user) {
        return getAllCategories(user.getId());
    }

    @Query("SELECT * FROM categories WHERE userID = :userID AND parentID IS NULL ORDER BY category_name")
    LiveData<List<Category>> getRootCategories(UUID userID);

    @Ignore
    default LiveData<List<Category>> getRootCategories(User user) {
        return getRootCategories(user.getId());
    }

    @Nullable
    @Query("SELECT c1.* FROM categories AS c1 JOIN categories AS c2 ON c1.userID = c2.userID AND c1.ID = c2.parentID AND c2.userID = :userID AND c2.category_name LIKE :categoryName LIMIT 1")
    LiveData<Category> getCategoryParent(UUID userID, String categoryName);

    @Ignore
    default LiveData<Category> getParent(Category category) {
        return getCategoryParent(category.getUserID(), category.getName());
    }

    @Query("SELECT c1.* FROM categories AS c1 JOIN categories AS c2 ON c1.userID = c2.userID AND c1.parentID = c2.ID AND c2.userID = :userID AND c2.category_name LIKE :categoryName ORDER BY c1.category_name")
    LiveData<List<Category>> getChildCategories(UUID userID, String categoryName);

    @Ignore
    default LiveData<List<Category>> getChildCategories(Category category) {
        return getChildCategories(category.getUserID(), category.getName());
    }

    // Non-Category Methods

    @Query("SELECT cycles.* FROM (SELECT * FROM categories WHERE userID = :userID AND category_name LIKE :categoryName) AS c JOIN cycles ON c.userID = cycles.userID AND c.ID = cycles.categoryID ORDER BY cycles.cycle_name")
    LiveData<List<Cycle>> getCycles(UUID userID, String categoryName);

    @Ignore
    default LiveData<List<Cycle>> getCycles(Category category) {
        return getCycles(category.getUserID(), category.getName());
    }


    @Query("SELECT todos.* FROM (SELECT * FROM categories WHERE userID = :userID AND category_name LIKE :categoryName) AS c JOIN todos ON c.userID = todos.userID AND c.ID = todos.categoryID ORDER BY todos.todo_name")
    LiveData<List<Todo>> getTodos(UUID userID, String categoryName);

    @Ignore
    default LiveData<List<Todo>> getTodos(Category category) {
        return getTodos(category.getUserID(), category.getName());
    }
}
