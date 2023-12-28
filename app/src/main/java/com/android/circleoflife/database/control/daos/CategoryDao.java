package com.android.circleoflife.database.control.daos;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    @Query("SELECT * FROM categories WHERE userID = :userID AND parentID IS NULL OR ID = parentID ORDER BY category_name")
    LiveData<List<Category>> getRootCategories(UUID userID);

    @Ignore
    default LiveData<List<Category>> getRootCategories(User user) {
        return getRootCategories(user.getId());
    }

    @Nullable
    @Query("SELECT * FROM categories WHERE userID = :userID AND ID = :id")
    LiveData<Category> getCategory(UUID userID, UUID id);

    @Ignore
    default LiveData<Category> getParent(Category category) {
        if (category.getParentID() == null) {
            return new MutableLiveData<>(null);
        } else {
            return getCategory(category.getUserID(), category.getParentID());
        }
    }

    @Query("SELECT * FROM categories WHERE userID = :userID AND parentID = :parentID ORDER BY category_name")
    LiveData<List<Category>> getChildCategories(UUID userID, UUID parentID);

    @Ignore
    default LiveData<List<Category>> getChildCategories(Category category) {
        return getChildCategories(category.getUserID(), category.getId());
    }

    // Non-Category Methods

    @Query("SELECT * FROM cycles WHERE userID = :userID AND categoryID = :categoryID ORDER BY cycles.cycle_name")
    LiveData<List<Cycle>> getCycles(UUID userID, UUID categoryID);

    @Ignore
    default LiveData<List<Cycle>> getCycles(Category category) {
        return getCycles(category.getUserID(), category.getId());
    }


    @Query("SELECT * FROM todos WHERE userID = :userID AND categoryID = :categoryID ORDER BY todos.todo_name")
    LiveData<List<Todo>> getTodos(UUID userID, UUID categoryID);

    @Ignore
    default LiveData<List<Todo>> getTodos(Category category) {
        return getTodos(category.getUserID(), category.getId());
    }
}
