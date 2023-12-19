package com.android.circleoflife.ui.activities.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.ui.activities.SuperActivity;
import com.android.circleoflife.ui.viewmodels.CategoryViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RootCategoriesActivity extends SuperActivity {
    private static final String TAG = "RootCategoriesActivity";


    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_categories);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                Log.d(TAG, "Waiting for user to authenticate");
                User user = App.getAuthentication().waitForUser();
                categoryViewModel.setUser(user);
                Log.d(TAG, "retrieved authenticated User: " + user);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> categoryViewModel.getCurrentCategories().observe(this, categories -> Toast.makeText(this, categories.stream().map(Category::toString).reduce("Categories changed: ", (a, b) -> a + b + ";"), Toast.LENGTH_SHORT).show()));
            } catch (InterruptedException e) {
                Log.w(TAG, "Waiting for user authentication got interrupted!", e);
            }
        });

        //categoryViewModel.getCurrentCategories().observe(this, categories -> Toast.makeText(this, categories.stream().map(Category::toString).reduce("Categories changed: ", (a, b) -> a + b + ";"), Toast.LENGTH_SHORT).show());

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
                    // TODO: 19.12.2023 Notify RecyclerView if Query-Text changes
                    return false;
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
            Toast.makeText(this, "Creating new...", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}