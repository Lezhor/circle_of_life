package com.android.circleoflife.ui.activities.categories.not_root;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.Nameable;
import com.android.circleoflife.ui.activities.SuperActivity;
import com.android.circleoflife.ui.activities.categories.EditNameDialog;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.CategoryRecyclerViewAdapter;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVItemWrapper;
import com.android.circleoflife.ui.activities.categories.root.RootCategoriesActivity;
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
        Category root = intent.getParcelableExtra("category");

        if (root == null) {
            // THIS SHOULD NOT HAPPEN
            Toast.makeText(this, "Error occured loading category!!!", Toast.LENGTH_SHORT).show();
            //finish();
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
                    underlayButtons.add(new SwipeWithButtonsHelper.UnderlayButton(
                            R.drawable.ic_back,
                            R.color.md_theme_primaryContainer,
                            R.color.md_theme_primary,
                            pos -> {
                                Nameable nameable = (Nameable) adapter.getFilteredItemAtIndex(pos).getObject();
                                Toast.makeText(CategoryActivity.this, "Clicked on: " + nameable.getName(), Toast.LENGTH_SHORT).show();
                            }
                    ));
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

    @Override
    public void onCategoryClicked(Category category) {
        Toast.makeText(this, "Category clicked", Toast.LENGTH_SHORT).show();
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