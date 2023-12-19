package com.android.circleoflife.repositories;

import androidx.lifecycle.LiveData;

import com.android.circleoflife.database.control.DatabaseController;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;

import java.util.List;

public class CategoryRepository extends BaseRepository {

    private final DatabaseController db;

    public CategoryRepository(DatabaseController db) {
        super();
        this.db = db;
    }

    public void insertCategory(Category category) {
        doInBackground(() -> db.insertCategories(category));
    }

    public void updateCategory(Category category) {
        doInBackground(() -> db.updateCategory(category));
    }

    // TODO: 15.12.2023 Maybe consider changing parentID of all children before deleting?
    public void deleteCategory(Category category) {
        doInBackground(() -> db.deleteCategory(category));
    }

    public LiveData<List<Category>> getAllCategories(User user) {
        return db.getAllCategories(user);
    }

    public LiveData<List<Category>> getRootCategories(User user) {
        return db.getRootCategories(user);
    }

    public LiveData<List<Category>> getChildren(Category category) {
        return db.getChildCategories(category);
    }

    public LiveData<List<Cycle>> getCycles(Category category) {
        return db.getCycles(category);
    }

    public LiveData<List<Todo>> getTodos(Category category) {
        return db.getTodos(category);
    }

}