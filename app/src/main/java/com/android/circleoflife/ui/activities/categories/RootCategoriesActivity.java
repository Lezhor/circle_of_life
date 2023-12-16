package com.android.circleoflife.ui.activities.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.ui.viewmodels.CategoryViewModel;

public class RootCategoriesActivity extends AppCompatActivity {


    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_categories);

        categoryViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryViewModel(App.getAuthentication().getUser());
            }
        }).get(CategoryViewModel.class);

        categoryViewModel.getCurrentCategories().observe(this, categories -> Toast.makeText(this, categories.stream().map(Category::toString).reduce("Categories changed: ", (a, b) -> a + b + ";"), Toast.LENGTH_SHORT).show());

    }
}