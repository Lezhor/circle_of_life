package com.android.circleoflife.database.control.daos;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;

import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;

import java.util.List;

@Dao
public interface CategoryDao extends BaseDao<Category> {


    @Query("SELECT * FROM categories WHERE uid = :userID ORDER BY category_name")
    LiveData<List<Category>> getAllCategories(int userID);

    @Ignore
    default LiveData<List<Category>> getAllCategories(User user) {
        return getAllCategories(user.getId());
    }

    @Query("SELECT * FROM categories WHERE uid = :userID AND parent_category IS NULL ORDER BY category_name")
    LiveData<List<Category>> getRootCategories(int userID);

    @Ignore
    default LiveData<List<Category>> getRootCategories(User user) {
        return getRootCategories(user.getId());
    }

    @Nullable
    @Query("SELECT c1.uid, c1.category_name, c1.parent_category FROM categories AS c1 JOIN categories AS c2 ON c1.uid = c2.uid AND c1.category_name LIKE c2.parent_category AND c2.uid = :userID AND c2.category_name LIKE :categoryName LIMIT 1")
    LiveData<Category> getCategoryParent(int userID, String categoryName);

    @Ignore
    default LiveData<Category> getParent(Category category) {
        return getCategoryParent(category.getUserID(), category.getName());
    }

    @Query("SELECT c1.uid, c1.category_name, c1.parent_category FROM categories AS c1 JOIN categories AS c2 ON c1.uid = c2.uid AND c1.parent_category LIKE c2.category_name AND c2.uid = :userID AND c2.category_name LIKE :categoryName ORDER BY c1.category_name")
    LiveData<List<Category>> getChildCategories(int userID, String categoryName);

    @Ignore
    default LiveData<List<Category>> getChildCategories(Category category) {
        return getChildCategories(category.getUserID(), category.getName());
    }

    // TODO: 10.12.2023 Method: getCycles(), getTodos


}
