package com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.circleoflife.R;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;

import java.util.function.Function;

public class CategoryHolder extends Holder<Category> {

    private final TextView title;

    public CategoryHolder(@NonNull View itemView, RVHolderInterface holderInterface, Function<Integer, Category> categoryFromPositionGetter) {
        super(itemView, holderInterface, categoryFromPositionGetter);
        title = itemView.findViewById(R.id.root_category_item);
    }

    @Override
    protected void onClick(Category category) {
        holderInterface.onCategoryClicked(category);
    }

    @Override
    protected void onLongClick(Category category) {
        holderInterface.onCategoryLongClicked(category);
    }


}
