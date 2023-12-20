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

    private LiveData<List<Category>> categories;
    private final User user;

    private Category root;

    public CategoryViewModel(User user) {
        this.user = user;
        repository = new CategoryRepository(App.getDatabaseController());
        //categories = repository.getAllCategories(user);
    }

    @Nullable
    public User getUser() {
        return user;
    }

    public void setRoot(Category root) {
        this.root = root;
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
        if (categories == null) {
            if (user == null) {
                Log.w(TAG, "getAllCategories: Tried to get Categories before User is set!!!");
                return null;
            } else {
                categories = repository.getAllCategories(user);
            }
        }
        return categories;
    }

    public LiveData<List<Category>> getCurrentCategories() {
        if (user == null) {
            Log.w(TAG, "getCurrentCategories: tried to get currentCategories before user is set");
            return null;
        }
        if (root == null) {
            return repository.getRootCategories(user);
        } else {
            return repository.getChildren(root);
        }
    }
}
