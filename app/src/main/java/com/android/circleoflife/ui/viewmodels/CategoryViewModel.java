package com.android.circleoflife.ui.viewmodels;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.Nameable;
import com.android.circleoflife.repositories.CategoryRepository;

import java.util.LinkedList;
import java.util.List;

/**
 * ViewModel for Category-Activities.<br>
 * Stores all needed livedata and offers getters for those.<br>
 * Has a root-attribute. If its null the categories here will be root-categories,
 * else they will be child categories of root
 */
public class CategoryViewModel extends ViewModel {
    private static final String TAG = "CategoryViewModel";

    private final CategoryRepository repository;

    /**
     * Stores every single category of this user as livedata
     */
    private final LiveData<List<Category>> allCategoriesLiveData;

    /**
     * Stores all categories. Gets update via observing the {@link #allCategoriesLiveData livedata}.
     */
    private final List<Category> allCategories = new LinkedList<>();

    /**
     * Stores categories that are currently relevant. if root is null stores root-categories.
     * If root is set, stores all child categories of that root.
     */
    private final LiveData<List<Category>> currentCategories;

    /**
     * null if root is null, if root is set, stores its cycles
     */
    private final LiveData<List<Cycle>> currentCycles;

    /**
     * null if root is null, if root is set, stores its todos
     */
    private final LiveData<List<Todo>> currentTodos;

    /**
     * User which is currently logged on
     */
    private final User user;

    /**
     * Root Category
     */
    private final Category root;

    /**
     * Stores revertlast action as a runnable.<br>
     * e.g. last action was delete Category temp, than this instance will store:
     * <pre>{@code
     *      () -> insert(temp);
     * }</pre>
     * Setting the last action happens in the action-methods themselves. and resetting in {@link #revertLastAction()} where the runnable also is run
     * @see #insert(Category)
     * @see #update(Category)
     * @see #delete(Category)
     */
    private Runnable revertLastAction;

    /**
     * Stores the text of the last action. Used for displaying it with undo button in a snackbar.<br>
     * The text is retrieved from the strings-resources.
     */
    private String lastActionText = "";


    /**
     * Constructor. Sets root to null
     * @param user user
     */
    public CategoryViewModel(User user) {
        this(user, null);
    }

    /**
     * Constructor for CategoryViewModel
     * @param user user
     * @param root root
     */
    public CategoryViewModel(User user, @Nullable Category root) {
        this.user = user;
        this.root = root;
        repository = new CategoryRepository(App.getDatabaseController());
        allCategoriesLiveData = user == null ? null : repository.getAllCategories(user);
        if (allCategoriesLiveData != null) {
            allCategoriesLiveData.observeForever(list -> {
                allCategories.clear();
                allCategories.addAll(list);
            });
        }
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

    /**
     * Reverts the last action
     */
    public void revertLastAction() {
        if (revertLastAction != null) {
            revertLastAction.run();
            revertLastAction = null;
            lastActionText = "";
        }
    }

    public void insert(Category category) {
        repository.insertCategory(category);
        revertLastAction = () -> delete(category);
        setLastActionText(R.string.snackbar_text_inserted, category);
    }

    public void update(Category category) {
        Category backup = allCategories.stream().filter(category::equals).findFirst().orElse(null);
        repository.updateCategory(category);
        if (backup != null) {
            revertLastAction = () -> update(backup);
            setLastActionText(R.string.snackbar_text_updated, backup);
        }
    }

    public void delete(Category category) {
        repository.deleteCategory(category);
        revertLastAction = () -> insert(category);
        setLastActionText(R.string.snackbar_text_deleted, category);
    }

    public void insert(Cycle cycle) {
        repository.insertCycle(cycle);
        revertLastAction = () -> delete(cycle);
        setLastActionText(R.string.snackbar_text_inserted, cycle);
    }

    public void update(Cycle cycle) {
        // TODO: 24.12.2023 add revert last action
        repository.updateCycle(cycle);
    }

    public void delete(Cycle cycle) {
        repository.deleteCycle(cycle);
        revertLastAction = () -> insert(cycle);
        setLastActionText(R.string.snackbar_text_deleted, cycle);
    }

    public void insert(Todo todo) {
        repository.insertTodo(todo);
        setLastActionText(R.string.snackbar_text_inserted, todo);
    }

    public void update(Todo todo) {
        // TODO: 24.12.2023 add revert last action
        repository.updateTodo(todo);
    }

    public void delete(Todo todo) {
        repository.deleteTodo(todo);
        revertLastAction = () -> insert(todo);
        setLastActionText(R.string.snackbar_text_deleted, todo);
    }

    public void setLastActionText(@StringRes int actionStringResId, Nameable item) {
        this.lastActionText = App.getApplicationContext().getString(actionStringResId) + ": " + item.getName();
    }

    public LiveData<List<Category>> getAllCategoriesLiveData() {
        return allCategoriesLiveData;
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

    public String getLastActionText() {
        return lastActionText;
    }
}
