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
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.CategoryRecyclerViewAdapter;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;
import com.android.circleoflife.ui.viewmodels.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

public class CategoryActivity extends AppCompatActivity implements RVHolderInterface {
    private static final String TAG = "CategoryActivity";

    private CategoryViewModel categoryViewModel;

    private RecyclerView recyclerView;
    private CategoryRecyclerViewAdapter adapter;
    private FloatingActionButton fab;
    private TextView invisText;


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
                    invisText.setVisibility(list.size() == 0 ? View.VISIBLE : View.INVISIBLE);
                    invisText.setEnabled(list.size() == 0);
                    adapter.setCategories(list);
                });
            }

        }

    }

    private void onFabClicked(View view) {
        // TODO: 21.12.2023 Create Category/Cycle/Todo
        categoryViewModel.insert(new Category(UUID.randomUUID(), "Temp", categoryViewModel.getUser().getId(), categoryViewModel.getRoot().getId()));
    }

    @Override
    public void onCategoryClicked(Category category) {

    }

    @Override
    public void onCategoryLongClicked(Category category) {

    }

    @Override
    public void onCycleClicked(Cycle cycle) {

    }

    @Override
    public void onCycleLongClicked(Cycle cycle) {

    }

    @Override
    public void onTodoClicked(Todo todo) {

    }

    @Override
    public void onTodoLongClicked(Todo todo) {

    }

    @Override
    public void onTodoCheckboxChecked(Todo todo) {

    }

    @Override
    public void onTodoCheckboxUnchecked(Todo todo) {

    }
}