package com.android.circleoflife.ui.recyclerview_utils;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

public abstract class ItemTouchDragAndDropCallback extends ItemTouchHelper.SimpleCallback {

    private final RecyclerView recyclerView;

    public ItemTouchDragAndDropCallback(RecyclerView recyclerView) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        this.recyclerView = recyclerView;
        attachCallback();
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    private View folder;
    private int folderIndex = -1;
    private RecyclerView.ViewHolder draggedItem = null;

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            draggedItem = viewHolder;
            if (folder != null) {
                folderIndex = -1;
                revertHighlightFolder(folder);
                folder = null;
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            if (folder != null) {

                moveInto(draggedItem.getAdapterPosition(), folderIndex);
                revertHighlightFolder(folder);
                draggedItem.itemView.setVisibility(View.INVISIBLE);
                draggedItem = null;
            }
        }

    }

    protected abstract void moveInto(int from, int into);

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && isCurrentlyActive) {
            if (folder != null) {
                folderIndex = -1;
                revertHighlightFolder(folder);
                folder = null;
            }

            float itemActualPosition = viewHolder.itemView.getTop() + dY + viewHolder.itemView.getHeight() / 2.0f;

            // fild folder under dragged item:
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View child = recyclerView.getChildAt(i);

                if (!child.equals(viewHolder.itemView)) {
                    if (child.getTop() < itemActualPosition && itemActualPosition < child.getBottom()) {
                        folder = child;
                        highlightFolder(folder);
                        folderIndex = i;
                        break;
                    }
                }
            }

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    protected abstract void highlightFolder(View folder);
    protected abstract void revertHighlightFolder(View folder);


    public void attachCallback() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
