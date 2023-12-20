package com.android.circleoflife.ui.activities.categories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.ui.activities.SuperActivity;
import com.android.circleoflife.ui.viewmodels.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

public class RootCategoriesActivity extends SuperActivity {
    private static final String TAG = "RootCategoriesActivity";


    private CategoryViewModel categoryViewModel;

    RecyclerView recyclerView;
    CategoryRecyclerViewAdapter adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_categories);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.i(TAG, "onCreate: Actionbar is null!");
        } else {
            actionBar.setTitle(R.string.categories);
        }

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> addCategory("Lol Category"));

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CategoryRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        executeInBackground(App.getAuthentication()::waitForUser, user -> {
            categoryViewModel.setUser(user);
            categoryViewModel.getCurrentCategories().observe(this, adapter::setCategories);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setUpMenu(menu, R.menu.categories_root_toolbar_menu);

        MenuItem searchItem = menu.findItem(R.id.search_button);
        setUpSearchView((SearchView) searchItem.getActionView());

        return true;
    }

    private void setUpSearchView(SearchView searchView) {
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_category_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // TODO: 19.12.2023 Remove (Temp)
                    Toast.makeText(RootCategoriesActivity.this, "Query Submitted: " + query, Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_button) {
            Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.add_category) {
            Toast.makeText(this, "Creating new category", Toast.LENGTH_SHORT).show();
            addCategory("My Category");
        } else if (id == R.id.remove_first_category) {
            Category category = adapter.getFilteredCategoryAtIndex(0);
            if (category != null) {
                Toast.makeText(this, "Deleting Category: " + category.getName(), Toast.LENGTH_SHORT).show();
                categoryViewModel.delete(category);
            } else {
                Toast.makeText(this, "No Category found", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void addCategory(String categoryName) {
        if (categoryViewModel.getUser() != null) {
            openCreateCategoryDialog();
        } else {
            Toast.makeText(this, "Can't create category - User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCreateCategoryDialog() {
        CreateCategoryDialog dialog = new CreateCategoryDialog(this::submitCreateCategoryDialog);
        dialog.show(getSupportFragmentManager(), "create category dialog");
    }

    private void submitCreateCategoryDialog(String name, @Nullable String parent) {
        Log.d(TAG, "submitCreateCategoryDialog: " + name + ", " + parent);
        categoryViewModel.insert(new Category(UUID.randomUUID(), name, categoryViewModel.getUser().getId(), parent == null ? null : null /* TODO 20.12.23 Fetch Parent if not null */));
    }
}