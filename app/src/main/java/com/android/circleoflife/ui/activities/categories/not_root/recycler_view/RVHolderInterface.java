package com.android.circleoflife.ui.activities.categories.not_root.recycler_view;


import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;

public interface RVHolderInterface {
    void onCategoryClicked(Category category);

    void onCategoryLongClicked(Category category);

    void onCycleClicked(Cycle cycle);

    void onCycleLongClicked(Cycle cycle);

    void onTodoClicked(Todo todo);

    void onTodoLongClicked(Todo todo);
}