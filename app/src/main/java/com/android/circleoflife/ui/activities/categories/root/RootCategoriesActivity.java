package com.android.circleoflife.ui.activities.categories.root;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.ui.activities.SuperActivity;
import com.android.circleoflife.ui.activities.categories.CreateCategoryDialog;
import com.android.circleoflife.ui.activities.categories.EditCategoryDialog;
import com.android.circleoflife.ui.activities.categories.not_root.CategoryActivity;
import com.android.circleoflife.ui.viewmodels.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

public class RootCategoriesActivity extends SuperActivity implements RootCategoryRecyclerViewAdapter.CategoryHolder.CategoryHolderInterface {
    private static final String TAG = "RootCategoriesActivity";


    private CategoryViewModel categoryViewModel;

    RecyclerView recyclerView;
    RootCategoryRecyclerViewAdapter adapter;
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

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> addCategory());

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RootCategoryRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView invisText = findViewById(R.id.no_categories_created_yet);

        final User user = App.getAuthentication().getUser();
        if (user != null) {
            categoryViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
                @NonNull
                @Override
                public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                    return (T) new CategoryViewModel(user);
                }
            }).get(CategoryViewModel.class);
            categoryViewModel.getCurrentCategories().observe(this, list -> {
                invisText.setVisibility(list.size() == 0 ? View.VISIBLE : View.INVISIBLE);
                adapter.setCategories(list);
            });
        }
    }

    private void openCategoryActivity(Category category) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
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
            addCategory();
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

    private void addCategory() {
        if (categoryViewModel.getUser() != null) {
            openCreateCategoryDialog();
        } else {
            Toast.makeText(this, "Can't create category - User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCreateCategoryDialog() {
        CreateCategoryDialog dialog = new CreateCategoryDialog(this::submitCreateCategoryDialog);
        dialog.show(getSupportFragmentManager(), "create root category dialog");
    }

    private void submitCreateCategoryDialog(String name) {
        Log.d(TAG, "submitCreateCategoryDialog: " + name);
        categoryViewModel.insert(new Category(UUID.randomUUID(), name, categoryViewModel.getUser().getId(), null));
    }

    @Override
    public void onCategoryClicked(Category category) {
        Log.d(TAG, "onCategoryClicked: Category clicked: " + category);
        //Toast.makeText(this, "Category clicked: " + category, Toast.LENGTH_SHORT).show();
        openCategoryActivity(category);
    }

    @Override
    public void onLongCategoryClicked(Category category) {
        Log.d(TAG, "Category long clicked: " + category);
        EditCategoryDialog dialog = new EditCategoryDialog(categoryViewModel::update, category);
        dialog.show(getSupportFragmentManager(), "edit root category dialog");
    }
}