package com.android.circleoflife.ui.activities.categories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
}