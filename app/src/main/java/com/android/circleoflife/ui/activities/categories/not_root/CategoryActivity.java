package com.android.circleoflife.ui.activities.categories.not_root;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.Copyable;
import com.android.circleoflife.ui.activities.SuperActivity;
import com.android.circleoflife.ui.activities.categories.EditNameDialog;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.CategoryRecyclerViewAdapter;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVItemWrapper;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder.CategoryHolder;
import com.android.circleoflife.ui.recyclerview_utils.SwipeAndDragTouchHelper;
import com.android.circleoflife.ui.recyclerview_utils.SwipeWithButtonsHelper;
import com.android.circleoflife.ui.viewmodels.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CategoryActivity extends SuperActivity implements RVHolderInterface {
    private static final String TAG = "CategoryActivity";

    private CategoryViewModel categoryViewModel;

    private RecyclerView recyclerView;
    private CategoryRecyclerViewAdapter adapter;
    private FloatingActionButton fab;
    private TextView invisText;
    private SwipeWithButtonsHelper swipeHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_categories);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("categoryBundle");
        Category passedCategory = null;
        if (bundle != null) {
            passedCategory = bundle.getParcelable("category");
        }
        final Category root = passedCategory;

        if (root == null) {
            // THIS SHOULD NOT HAPPEN
            Toast.makeText(this, "Error occured loading category!!!", Toast.LENGTH_SHORT).show();
            finish();
        } else {

            ActionBar actionBar = getSupportActionBar();
            if (actionBar == null) {
                Log.i(TAG, "onCreate: Actionbar is null!");
            } else {
                // TODO: 25.12.2023 Add optionsMenu
                actionBar.setTitle(root.getName());
            }

            invisText = findViewById(R.id.category_invis_text);
            invisText.setText(R.string.category_empty);
            invisText.setEnabled(false);
            invisText.setOnClickListener(view -> {
                categoryViewModel.delete(categoryViewModel.getRoot());
                finish();
            });

            fab = findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(this::onFabClicked);

            // TODO: 21.12.2023 ViewModel
            // TODO: 21.12.2023 Recycler View!!!!!

            recyclerView = findViewById(R.id.recyclerView);
            adapter = new CategoryRecyclerViewAdapter(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            /*
            swipeHelper = new SwipeWithButtonsHelper(this, recyclerView) {
                @Override
                public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                    underlayButtons.add(new SwipeWithButtonsHelper.UnderlayButton(
                            R.drawable.ic_delete,
                            R.color.md_theme_errorContainer,
                            R.color.md_theme_error,
                            pos -> {
                                RVItemWrapper<?> o = adapter.getFilteredItemAtIndex(pos);
                                switch (o.getItemType()) {
                                    case RVItemWrapper.TYPE_CATEGORY -> categoryViewModel.delete((Category) o.getObject());
                                    case RVItemWrapper.TYPE_CYCLE -> categoryViewModel.delete((Cycle) o.getObject());
                                    case RVItemWrapper.TYPE_TODO -> categoryViewModel.delete((Todo) o.getObject());
                                    default -> {
                                        Log.w(TAG, "button pressed on ItemWrapper: " + o);
                                        return;
                                    }
                                }
                                showSnackbarWithUndoLastAction(recyclerView, categoryViewModel);
                            }
                    ));
                    underlayButtons.add(new SwipeWithButtonsHelper.UnderlayButton(
                            R.drawable.ic_edit,
                            R.color.md_theme_secondaryContainer,
                            R.color.md_theme_secondary,
                            pos -> {
                                try {
                                    RVItemWrapper<?> o = adapter.getFilteredItemAtIndex(pos);
                                    EditNameDialog<?> editNameDialog = switch (o.getItemType()) {
                                        case RVItemWrapper.TYPE_CATEGORY -> new EditNameDialog<>(categoryViewModel::update, (Category) o.getObject(), R.string.category);
                                        case RVItemWrapper.TYPE_CYCLE -> new EditNameDialog<>(categoryViewModel::update, (Cycle) o.getObject(), R.string.cycle);
                                        case RVItemWrapper.TYPE_TODO -> new EditNameDialog<>(categoryViewModel::update, (Todo) o.getObject(), R.string.todo);
                                        default ->
                                                throw new IllegalStateException("Unknown Item Type: " + o.getItemType());
                                    };
                                    editNameDialog.show(getSupportFragmentManager(), "dialog_edit_name");
                                } catch (ClassCastException | IllegalStateException e) {
                                    Log.d(TAG, "Swipe edit button click failed on pos " + pos, e);
                                }
                            }
                    ));
                    if (categoryViewModel.getRoot().getParentID() != null || viewHolder instanceof CategoryHolder) {
                        underlayButtons.add(new SwipeWithButtonsHelper.UnderlayButton(
                                R.drawable.ic_back,
                                R.color.md_theme_primaryContainer,
                                R.color.md_theme_primary,
                                pos -> {
                                    RVItemWrapper<?> itemWrapper = adapter.getFilteredItemAtIndex(pos);
                                    switch (itemWrapper.getItemType()) {
                                        case RVItemWrapper.TYPE_CATEGORY -> moveToParent((Category) itemWrapper.getObject());
                                        case RVItemWrapper.TYPE_CYCLE -> moveToParent((Cycle) itemWrapper.getObject());
                                        case RVItemWrapper.TYPE_TODO -> moveToParent((Todo) itemWrapper.getObject());
                                    }
                                }
                        ));
                    }
                }
            };
             */


            new SwipeAndDragTouchHelper(this, recyclerView) {
                @Override
                protected boolean isCategory(int index) {
                    return adapter.getFilteredItemAtIndex(index).getItemType() == RVItemWrapper.TYPE_CATEGORY;
                }

                @Override
                protected boolean moveInto(int fromIndex, int intoIndex) {
                    if (fromIndex == intoIndex) {
                        return false;
                    }
                    RVItemWrapper<?> fromWrapper = adapter.getFilteredItemAtIndex(fromIndex);
                    RVItemWrapper<?> intoWrapper = adapter.getFilteredItemAtIndex(intoIndex);
                    if (intoWrapper.getItemType() != RVItemWrapper.TYPE_CATEGORY) {
                        return false;
                    }
                    Category intoCategory = (Category) intoWrapper.getObject();
                    if (fromWrapper.getObject() instanceof Copyable<?> copyable) {
                        Object copy = copyable.copy();
                        if (copy instanceof Category category) {
                            category.setParentID(intoCategory.getId());
                            categoryViewModel.update(category, R.string.snackbar_text_moved_into, intoCategory);
                        } else if (copy instanceof Cycle cycle) {
                            cycle.setCategoryID(intoCategory.getId());
                            categoryViewModel.update(cycle, R.string.snackbar_text_moved_into, intoCategory);
                        } else if (copy instanceof Todo todo) {
                            todo.setCategoryID(intoCategory.getId());
                            categoryViewModel.update(todo, R.string.snackbar_text_moved_into, intoCategory);
                        } else {
                            return false;
                        }
                        showSnackbarWithUndoLastAction(recyclerView, categoryViewModel);
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                protected void highlightFolder(View folder) {
                    RelativeLayout layout = folder.findViewById(R.id.root_category_rel_layout);
                    if (layout != null) {
                        layout.setBackgroundColor(getColor(R.color.md_theme_secondaryContainer));
                    }
                }

                @Override
                protected void revertHighlightFolder(View folder) {
                    RelativeLayout layout = folder.findViewById(R.id.root_category_rel_layout);
                    if (layout != null) {
                        layout.setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                @Override
                public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                    underlayButtons.add(new SwipeAndDragTouchHelper.UnderlayButton(
                            R.drawable.ic_delete,
                            R.color.md_theme_errorContainer,
                            R.color.md_theme_error,
                            pos -> {
                                RVItemWrapper<?> wrapper = adapter.getFilteredItemAtIndex(pos);
                                Object object = wrapper.getObject();
                                if (object instanceof Category category) {
                                    categoryViewModel.delete(category);
                                } else if (object instanceof Cycle cycle) {
                                    categoryViewModel.delete(cycle);
                                } else if (object instanceof Todo todo) {
                                    categoryViewModel.delete(todo);
                                } else {
                                    return;
                                }
                                showSnackbarWithUndoLastAction(recyclerView, categoryViewModel);
                            }
                    ));
                    underlayButtons.add(new SwipeAndDragTouchHelper.UnderlayButton(
                            R.drawable.ic_edit,
                            R.color.md_theme_secondaryContainer,
                            R.color.md_theme_secondary,
                            pos -> {
                                EditNameDialog<?> editNameDialog;
                                RVItemWrapper<?> wrapper = adapter.getFilteredItemAtIndex(pos);
                                Object object = wrapper.getObject();
                                if (object instanceof Category category) {
                                    editNameDialog = new EditNameDialog<>(categoryViewModel::update, category, R.string.category);
                                } else if (object instanceof Cycle cycle) {
                                    editNameDialog = new EditNameDialog<>(categoryViewModel::update, cycle, R.string.cycle);
                                } else if (object instanceof Todo todo) {
                                    editNameDialog = new EditNameDialog<>(categoryViewModel::update, todo, R.string.todo);
                                } else {
                                    return;
                                }
                                editNameDialog.show(getSupportFragmentManager(), "dialog_edit_category");
                            }
                    ));
                    if (categoryViewModel.getRoot().getParentID() != null || viewHolder instanceof CategoryHolder) {
                        underlayButtons.add(new SwipeAndDragTouchHelper.UnderlayButton(
                                R.drawable.ic_back,
                                R.color.md_theme_primaryContainer,
                                R.color.md_theme_primary,
                                pos -> {
                                    RVItemWrapper<?> itemWrapper = adapter.getFilteredItemAtIndex(pos);
                                    switch (itemWrapper.getItemType()) {
                                        case RVItemWrapper.TYPE_CATEGORY -> moveToParent((Category) itemWrapper.getObject());
                                        case RVItemWrapper.TYPE_CYCLE -> moveToParent((Cycle) itemWrapper.getObject());
                                        case RVItemWrapper.TYPE_TODO -> moveToParent((Todo) itemWrapper.getObject());
                                    }
                                }
                        ));
                    }
                }
            };


            final User user = App.getAuthentication().getUser();
            if (user != null) {
                categoryViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                        return (T) new CategoryViewModel(user, root);
                    }
                }).get(CategoryViewModel.class);

                categoryViewModel.getCurrentCategories().observe(this, list -> {
                    adapter.setCategories(list);
                    setInvisText(adapter.getItemCount() == 0);
                });
                categoryViewModel.getCurrentCycles().observe(this, list -> {
                    adapter.setCycles(list);
                    setInvisText(adapter.getItemCount() == 0);
                });
                categoryViewModel.getCurrentTodos().observe(this, list -> {
                    adapter.setTodos(list);
                    setInvisText(adapter.getItemCount() == 0);
                });
            }

        }

    }

    private void setInvisText(boolean visible) {
        invisText.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        invisText.setEnabled(visible);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setUpMenu(menu, R.menu.categories_root_toolbar_menu);
        MenuItem searchItem = menu.findItem(R.id.search_button);
        setUpSearchView((SearchView) searchItem.getActionView(), getString(R.string.search_in_hint) + " " + categoryViewModel.getRoot().getName(), adapter);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_category) {
            categoryViewModel.insert(new Category(UUID.randomUUID(), "Hello There", categoryViewModel.getUser().getId(), categoryViewModel.getRoot().getId()));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Moves given category to the parent of the root (one folder up)
     *
     * @param category category to be moved
     */
    private void moveToParent(Category category) {
        Category copy = category.copy();
        copy.setParentID(categoryViewModel.getRoot().getParentID());
        categoryViewModel.update(copy, R.string.snackbar_text_moved_to_parent);
        showSnackbarWithUndoLastAction(recyclerView, categoryViewModel);
    }

    /**
     * Moves given cycle to the parent of the root, unless its null
     *
     * @param cycle cycle to be moved
     */
    private void moveToParent(Cycle cycle) {
        if (categoryViewModel.getRoot().getParentID() != null) {
            Cycle copy = cycle.copy();
            copy.setCategoryID(categoryViewModel.getRoot().getParentID());
            categoryViewModel.update(copy, R.string.snackbar_text_moved_to_parent);
            showSnackbarWithUndoLastAction(recyclerView, categoryViewModel);
        }
    }

    /**
     * Moves given todó to the parent of the root, unless its null
     *
     * @param todoItem todó to be moved
     */
    private void moveToParent(Todo todoItem) {
        if (categoryViewModel.getRoot().getParentID() != null) {
            Todo copy = todoItem.copy();
            copy.setCategoryID(categoryViewModel.getRoot().getParentID());
            categoryViewModel.update(copy, R.string.snackbar_text_moved_to_parent);
            showSnackbarWithUndoLastAction(recyclerView, categoryViewModel);
        }
    }

    private void onFabClicked(View view) {
        // TODO: 21.12.2023 Create Category/Cycle/Todo
        //categoryViewModel.insert(new Category(UUID.randomUUID(), "Temp", categoryViewModel.getUser().getId(), categoryViewModel.getRoot().getId()));

        categoryViewModel.insert(
                new Todo(UUID.randomUUID(),
                        "Test-Todo",
                        categoryViewModel.getUser().getId(),
                        categoryViewModel.getRoot().getId(),
                        0,
                        false,
                        LocalDateTime.of(2023, 12, 25, 0, 0)));
        /*
        categoryViewModel.insert(
                new Cycle(
                        UUID.randomUUID(),
                        "Mathe",
                        categoryViewModel.getUser().getId(),
                        categoryViewModel.getRoot().getId(),
                        1,
                        CycleFrequency.fromBinaryString("10000011")
                )
        );
         */
    }

    /**
     * Opens {@link CategoryActivity} with given category as parameter
     *
     * @param category passed category
     */
    private void openCategoryActivity(@NonNull Category category) {
        Intent intent = new Intent(this, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("category", category);
        intent.putExtra("categoryBundle", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCategoryClicked(Category category) {
        Log.d(TAG, "onCategoryClicked: Switching to CategoryActivity with category: " + category.getName());
        openCategoryActivity(category);
    }

    @Override
    public void onCategoryLongClicked(Category category) {
        Toast.makeText(this, "Category long clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCycleClicked(Cycle cycle) {
        Toast.makeText(this, "Cycle Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCycleLongClicked(Cycle cycle) {
        Toast.makeText(this, "Cycle long clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTodoClicked(Todo todo) {
        Toast.makeText(this, "Todo clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTodoLongClicked(Todo todo) {
        Toast.makeText(this, "Todo long clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTodoCheckboxChecked(Todo todo) {
        todo = new Todo(todo);
        todo.setDone(true);
        categoryViewModel.update(todo);
    }

    @Override
    public void onTodoCheckboxUnchecked(Todo todo) {
        todo = new Todo(todo);
        todo.setDone(false);
        categoryViewModel.update(todo);
    }
}