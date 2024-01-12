package com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.circleoflife.R;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;

import java.util.function.Function;

public class TodoHolder extends Holder<Todo> {

    private final TextView title;
    private final ImageView checkbox;
    private boolean currentlyChecked = false;
    private final TextView dueDate;

    public TodoHolder(@NonNull View itemView, RVHolderInterface holderInterface, Function<Integer, Todo> itemFromPositionGetter) {
        super(itemView, holderInterface, itemFromPositionGetter);
        title = itemView.findViewById(R.id.crv_todo_item_title);
        checkbox = itemView.findViewById(R.id.crv_todo_item_icon);
        checkbox.setOnClickListener(v -> onCheckBoxClicked());
        dueDate = itemView.findViewById(R.id.crv_todo_item_due_date);
    }

    @Override
    protected void onClick(Todo todo) {
        holderInterface.onTodoClicked(todo);
    }

    @Override
    protected void onLongClick(Todo todo) {
        holderInterface.onTodoLongClicked(todo);
    }

    @Override
    public TextView getTitleView() {
        return title;
    }

    public TextView getDueDateView() {
        return dueDate;
    }

    /**
     * Sets the resource of the icon to checkbox or checkboxoutline based on passed argument
     * @param checked whether or not the checkbox should be checked our not
     */
    public void setCheckbox(boolean checked) {
        this.currentlyChecked = checked;
        if (checked) {
            checkbox.setImageResource(R.drawable.ic_checkbox);
        } else {
            checkbox.setImageResource(R.drawable.ic_checkbox_outline);
        }
    }

    /**
     * if checked calls holerInterface.uncheck else calls holderInterface.check
     */
    private void onCheckBoxClicked() {
        int pos = getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            currentlyChecked = !currentlyChecked;
            if (currentlyChecked) {
                holderInterface.onTodoCheckboxChecked(itemFromPositionGetter.apply(pos));
            } else {
                holderInterface.onTodoCheckboxUnchecked(itemFromPositionGetter.apply(pos));
            }
        }
    }

}
