package com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    protected void onClick() {
        int pos = getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            holderInterface.onCategoryClicked(itemFromPositionGetter.apply(pos));
        }
    }

    @Override
    protected void onLongClick() {
        int pos = getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            holderInterface.onLongCategoryClicked(itemFromPositionGetter.apply(pos));
        }
    }
}
