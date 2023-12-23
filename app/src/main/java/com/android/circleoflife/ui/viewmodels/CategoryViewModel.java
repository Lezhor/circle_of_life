package com.android.circleoflife.ui.viewmodels;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.repositories.CategoryRepository;

import java.util.List;

/**
 * ViewModel for Category-Activities.<br>
 * Stores all needed livedata and offers getters for those.
 */
public class CategoryViewModel extends ViewModel {
    private static final String TAG = "CategoryViewModel";

    private final CategoryRepository repository;

    private final LiveData<List<Category>> allCategories;

    private final LiveData<List<Category>> currentCategories;
    private final LiveData<List<Cycle>> currentCycles;
    private final LiveData<List<Todo>> currentTodos;

    private final User user;

    private final Category root;

    public CategoryViewModel(User user) {
        this(user, null);
    }

    public CategoryViewModel(User user, @Nullable Category root) {
        this.user = user;
        this.root = root;
        repository = new CategoryRepository(App.getDatabaseController());
        allCategories = user == null ? null : repository.getAllCategories(user);
        if (user == null) {
            Log.w(TAG, "CategoryViewModel: Instantiated CategoryViewModel with null User!!!");
            currentCategories = null;
            currentCycles = null;
            currentTodos = null;
        } else if (root == null) {
            currentCategories = repository.getRootCategories(user);
            currentCycles = null;
            currentTodos = null;
        } else {
            currentCategories = repository.getChildren(root);
            currentCycles = repository.getCycles(root);
            currentTodos = repository.getTodos(root);
        }
    }

    public User getUser() {
        return user;
    }

    public Category getRoot() {
        return root;
    }

    public void insert(Category category) {
        repository.insertCategory(category);
    }

    public void update(Category category) {
        repository.updateCategory(category);
    }

    public void delete(Category category) {
        repository.deleteCategory(category);
    }

    public void insert(Cycle cycle) {
        repository.insertCycle(cycle);
    }

    public void update(Cycle cycle) {
        repository.updateCycle(cycle);
    }

    public void delete(Cycle cycle) {
        repository.deleteCycle(cycle);
    }

    public void insert(Todo todo) {
        repository.insertTodo(todo);
    }

    public void update(Todo todo) {
        repository.updateTodo(todo);
    }

    public void delete(Todo todo) {
        repository.deleteTodo(todo);
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Category>> getCurrentCategories() {
        return currentCategories;
    }

    public LiveData<List<Cycle>> getCurrentCycles() {
        return currentCycles;
    }

    public LiveData<List<Todo>> getCurrentTodos() {
        return currentTodos;
    }
}
