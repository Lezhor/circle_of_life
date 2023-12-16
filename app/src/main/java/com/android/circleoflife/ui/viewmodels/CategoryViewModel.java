package com.android.circleoflife.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private final CategoryRepository repository;

    private final LiveData<List<Category>> categories;
    private final User user;

    private Category root;

    public CategoryViewModel(User user) {
        repository = new CategoryRepository(App.getDatabaseController());
        this.user = user;
        categories = repository.getAllCategories(user);
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
        return categories;
    }

    public LiveData<List<Category>> getCurrentCategories() {
        if (root == null) {
            return repository.getRootCategories(user);
        } else {
            return repository.getChildren(root);
        }
    }

    

    public void setRoot(Category root) {
        this.root = root;
    }
}
