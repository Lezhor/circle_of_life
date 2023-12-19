package com.android.circleoflife.ui.activities.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.android.circleoflife.ui.viewmodels.CategoryViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RootCategoriesActivity extends AppCompatActivity {
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
        getMenuInflater().inflate(R.menu.categories_root_toolbar_menu, menu);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr = obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorSecondary});
        int primaryColor = arr.getColor(0, -1);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
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