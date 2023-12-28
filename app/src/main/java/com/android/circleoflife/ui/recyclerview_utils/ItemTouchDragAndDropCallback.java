package com.android.circleoflife.ui.recyclerview_utils;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemTouchDragAndDropCallback extends ItemTouchHelper.SimpleCallback {

    private final RecyclerView recyclerView;

    public ItemTouchDragAndDropCallback(RecyclerView recyclerView) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        this.recyclerView = recyclerView;
        attachCallback();
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
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

    /**
     * If true than the ItemView at the given index will be highlighted when dragging another Item over it,
     * and method moveInto will be executed when dropped on it.
     * @param index index of the itemview which is being dragged on
     * @return true if the item at given index is a category
     */
    protected abstract boolean isCategory(int index);

    /**
     * This method implements the logic for moving an Item inside a category.<br>
     * It should delete {@code from} from current recyclerview.
     * @param from index of the item which was dragged
     * @param into index of the category where {@code from} should be inserted
     */
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
                    if (isCategory(i)) {
                        if (child.getTop() < itemActualPosition && itemActualPosition < child.getBottom()) {
                            folder = child;
                            highlightFolder(folder);
                            folderIndex = i;
                            break;
                        }
                    }
                }
            }

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * Highlights a folder. e.g. make background green<br>
     * @param folder view where the current item was dragged onto
     */
    protected abstract void highlightFolder(View folder);

    /**
     * Resets highlighting on a folder. e.g. reset background<br>
     * @param folder view where hovering above just stopped (left view or released/dropped)
     */
    protected abstract void revertHighlightFolder(View folder);


    /**
     * Creates an ItemTouchHelper with <code>this</code> as Callback and attaches the recyclerView to it
     */
    private void attachCallback() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
