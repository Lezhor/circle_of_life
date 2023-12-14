package com.android.circleoflife.database.control.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;

import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;

import java.util.List;
import java.util.UUID;

@Dao
public interface TodoDao extends BaseDao<Todo> {


    @Query("SELECT * FROM todos WHERE userID = :userID ORDER BY todo_name")
    LiveData<List<Todo>> getAllTodos(UUID userID);

    @Ignore
    default LiveData<List<Todo>> getAllTodos(User user) {
        return getAllTodos(user.getId());
    }

    @Query("SELECT * FROM categories WHERE userID = :userID AND ID = :categoryID LIMIT 1")
    LiveData<Category> getCategory(UUID userID, UUID categoryID);

    @Ignore
    default LiveData<Category> getCategory(Todo todo) {
        return getCategory(todo.getUserID(), todo.getCategoryID());
    }

    @Query("SELECT * FROM accomplishments WHERE userID = :userID AND accomplishments.todoID = :todoID ORDER BY timestamp DESC")
    LiveData<List<Accomplishment>> getAccomplishments(UUID userID, UUID todoID);

    @Ignore
    default LiveData<List<Accomplishment>> getAccomplishments(Todo todo) {
        return getAccomplishments(todo.getUserID(), todo.getId());
    }

}
