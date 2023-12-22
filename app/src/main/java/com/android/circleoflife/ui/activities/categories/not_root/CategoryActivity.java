package com.android.circleoflife.ui.activities.categories.not_root;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.ui.viewmodels.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryActivity extends AppCompatActivity implements CategoryRecyclerViewAdapter.CategoryHolder.CategoryHolderInterface {
    private static final String TAG = "CategoryActivity";

    private CategoryViewModel categoryViewModel;

    private RecyclerView recyclerView;
    private CategoryRecyclerViewAdapter adapter;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_categories);

        Intent intent = getIntent();
        Category category = intent.getParcelableExtra("category");

        if (category == null) {
            // THIS SHOULD NOT HAPPEN
            Toast.makeText(this, "Error occured loading category!!!", Toast.LENGTH_SHORT).show();
            //finish();
        } else {

            ActionBar actionBar = getSupportActionBar();
            if (actionBar == null) {
                Log.i(TAG, "onCreate: Actionbar is null!");
            } else {
                actionBar.setTitle(category.getName());
            }

            fab = findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(this::onFabClicked);

            // TODO: 21.12.2023 ViewModel
            // TODO: 21.12.2023 Recycler View!!!!!

            recyclerView = findViewById(R.id.recyclerView);
            adapter = new CategoryRecyclerViewAdapter(this);
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

    }

    private void onFabClicked(View view) {
        // TODO: 21.12.2023 Create Category/Cycle/Todo
    }

    @Override
    public void onCategoryClicked(Category category) {

    }

    @Override
    public void onLongCategoryClicked(Category category) {

    }
}