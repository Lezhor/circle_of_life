package com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder;

import android.view.View;

import androidx.annotation.NonNull;

import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;

import java.util.function.Function;

public class TodoHolder extends Holder<Todo> {
    public TodoHolder(@NonNull View itemView, RVHolderInterface holderInterface, Function<Integer, Todo> itemFromPositionGetter) {
        super(itemView, holderInterface, itemFromPositionGetter);
        // TODO: 22.12.2023 init todo layout
    }

    @Override
    protected void onClick(Todo todo) {
        holderInterface.onTodoClicked(todo);
    }

    @Override
    protected void onLongClick(Todo todo) {
        holderInterface.onTodoLongClicked(todo);
    }
}
