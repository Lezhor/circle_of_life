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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ViewModel for Category-Activities.<br>
 * Stores all needed livedata and offers getters for those.<br>
 * Has a root-attribute. If its null the categories here will be root-categories,
 * else they will be child categories of root
 */
public class CategoryViewModel extends ViewModel implements RevertibleActions {
    private static final String TAG = "CategoryViewModel";

    private final CategoryRepository repository;

    /**
     * Stores every single category of this user as livedata
     */
    private final LiveData<List<Category>> allCategoriesLiveData;

    /**
     * Stores categories that are currently relevant. if root is null stores root-categories.
     * If root is set, stores all child categories of that root.
     */
    private final LiveData<List<Category>> currentCategoriesLiveData;

    /**
     * Saves current categories as list. gets updated with an observer
     */
    private final List<Category> currentCategories = new ArrayList<>();

    /**
     * null if root is null, if root is set, stores its cycles
     */
    private final LiveData<List<Cycle>> currentCyclesLiveData;

    /**
     * Saves current cycles as list. gets updated with an observer
     */
    private final List<Cycle> currentCycles = new ArrayList<>();

    /**
     * null if root is null, if root is set, stores its todos
     */
    private final LiveData<List<Todo>> currentTodosLiveData;

    /**
     * Saves current todos as list. gets updated with an observer
     */
    private final List<Todo> currentTodos = new ArrayList<>();

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
        if (user == null) {
            Log.w(TAG, "CategoryViewModel: Instantiated CategoryViewModel with null User!!!");
            currentCategoriesLiveData = null;
            currentCyclesLiveData = null;
            currentTodosLiveData = null;
        } else if (root == null) {
            currentCategoriesLiveData = repository.getRootCategories(user);
            currentCyclesLiveData = null;
            currentTodosLiveData = null;
        } else {
            currentCategoriesLiveData = repository.getChildren(root);
            currentCyclesLiveData = repository.getCycles(root);
            currentTodosLiveData = repository.getTodos(root);
        }
        setUpLiveDataObserver(currentCategoriesLiveData, currentCategories);
        setUpLiveDataObserver(currentCyclesLiveData, currentCycles);
        setUpLiveDataObserver(currentTodosLiveData, currentTodos);
    }

    private <T> void setUpLiveDataObserver(LiveData<List<T>> liveData, List<T> list) {
        if (liveData != null) {
            liveData.observeForever(l -> {
                list.clear();
                list.addAll(l);
            });
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
    @Override
    public boolean revertLastAction() {
        if (revertLastAction != null) {
            revertLastAction.run();
            revertLastAction = null;
            lastActionText = "";
            return true;
        } else {
            return false;
        }
    }

    public void insert(Category category) {
        repository.insertCategory(category);
        revertLastAction = () -> repository.deleteCategory(category);
        setLastActionText(R.string.snackbar_text_inserted, category);
    }

    public void update(Category category) {
        update(category, R.string.snackbar_text_updated);
    }

    public void update(Category category, @StringRes int actionText, Nameable... others) {
        Category backup = currentCategories.stream().filter(category::equals).findFirst().orElse(null);
        repository.updateCategory(category);
        if (backup != null) {
            revertLastAction = () -> repository.updateCategory(backup);
            Nameable[] neededForSetLastActionText = new Nameable[others.length + 1];
            neededForSetLastActionText[0] = backup;
            System.arraycopy(others, 0, neededForSetLastActionText, 1, others.length);
            setLastActionText(actionText, neededForSetLastActionText);
        }
    }

    public void delete(Category category) {
        repository.deleteCategory(category);
        revertLastAction = () -> repository.insertCategory(category);
        setLastActionText(R.string.snackbar_text_deleted, category);
    }

    public void insert(Cycle cycle) {
        repository.insertCycle(cycle);
        revertLastAction = () -> repository.deleteCycle(cycle);
        setLastActionText(R.string.snackbar_text_inserted, cycle);
    }

    public void update(Cycle cycle){
        update(cycle, R.string.snackbar_text_updated);
    }

    public void update(Cycle cycle, @StringRes int actionText, Nameable... others) {
        Cycle backup = currentCycles.stream().filter(cycle::equals).findFirst().orElse(null);
        repository.updateCycle(cycle);
        if (backup != null) {
            revertLastAction = () -> repository.updateCycle(backup);
            Nameable[] neededForSetLastActionText = new Nameable[others.length + 1];
            neededForSetLastActionText[0] = backup;
            System.arraycopy(others, 0, neededForSetLastActionText, 1, others.length);
            setLastActionText(actionText, neededForSetLastActionText);
        }
    }

    public void delete(Cycle cycle) {
        repository.deleteCycle(cycle);
        revertLastAction = () -> repository.insertCycle(cycle);
        setLastActionText(R.string.snackbar_text_deleted, cycle);
    }

    public void insert(Todo todo) {
        repository.insertTodo(todo);
        revertLastAction = () -> repository.deleteTodo(todo);
        setLastActionText(R.string.snackbar_text_inserted, todo);
    }

    public void update(Todo todo) {
        update(todo, R.string.snackbar_text_updated);
    }

    public void update(Todo todo, @StringRes int actionText, Nameable... others) {
        Todo backup = currentTodos.stream().filter(todo::equals).findFirst().orElse(null);
        repository.updateTodo(todo);
        if (backup != null) {
            revertLastAction = () -> repository.updateTodo(backup);
            Nameable[] neededForSetLastActionText = new Nameable[others.length + 1];
            neededForSetLastActionText[0] = backup;
            System.arraycopy(others, 0, neededForSetLastActionText, 1, others.length);
            setLastActionText(actionText, neededForSetLastActionText);
        }
    }

    public void delete(Todo todo) {
        repository.deleteTodo(todo);
        revertLastAction = () -> repository.insertTodo(todo);
        setLastActionText(R.string.snackbar_text_deleted, todo);
    }

    /**
     * Retrieves given String from Resources. If string contains String '%s' than '%s' is replaced with item.getName() else it just appends item.getName() to the end.
     * @param actionStringResId description of action
     * @param item item
     */
    public void setLastActionText(@StringRes int actionStringResId, Nameable... item) {
        String passedString = App.getApplicationContext().getString(actionStringResId);

        String[] itemNames = Arrays.stream(item).map(Nameable::getName).toArray(String[]::new);

        this.lastActionText = App.getResources().getString(actionStringResId, (Object[]) itemNames);

        Log.d(TAG, "setLastActionText: Text: '" + passedString + "', items: " + Arrays.stream(itemNames).reduce("", (a, b) -> a + "'" + b + "'; "));
        Log.d(TAG, "setLastActionText: actual text: '" + this.lastActionText + "'");
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategoriesLiveData;
    }

    public LiveData<List<Category>> getCurrentCategories() {
        return currentCategoriesLiveData;
    }

    public LiveData<List<Cycle>> getCurrentCycles() {
        return currentCyclesLiveData;
    }

    public LiveData<List<Todo>> getCurrentTodos() {
        return currentTodosLiveData;
    }

    @Override
    public String getLastActionText() {
        return lastActionText;
    }
}
