package com.android.circleoflife.ui.viewmodels;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private static final String TAG = "CategoryViewModel";

    private final CategoryRepository repository;

    private final LiveData<List<Category>> allCategories;

    private final LiveData<List<Category>> currentCategories;

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
        } else if (root == null) {
            currentCategories = repository.getRootCategories(user);
        } else {
            currentCategories =  repository.getChildren(root);
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

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Category>> getCurrentCategories() {
        return currentCategories;
    }
}
