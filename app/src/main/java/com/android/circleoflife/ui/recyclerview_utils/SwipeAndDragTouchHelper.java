package com.android.circleoflife.ui.recyclerview_utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.circleoflife.application.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeAndDragTouchHelper extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = "SwipeWithButtonsHelper";

    public static final int BUTTON_WIDTH = 200;
    private final RecyclerView recyclerView;
    private List<UnderlayButton> buttons;
    private final GestureDetector gestureDetector;
    private int swipedPos = -1;
    private float swipeThreshold = 0.5f;
    private final Map<Integer, List<UnderlayButton>> buttonsBuffer;
    private final Queue<Integer> recoverQueue;

    private final GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            Log.d(TAG, "onSingleTapConfirmed: Touch!!!");
            for (UnderlayButton button : buttons) {
                if (button.onClick(e.getX(), e.getY()))
                    break;
            }
            return true;
        }
    };

    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            view.performClick();
            if (swipedPos < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());

            RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos);
            View swipedItem = swipedViewHolder.itemView;
            Rect rect = new Rect();
            swipedItem.getGlobalVisibleRect(rect);

            if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y) {
                    gestureDetector.onTouchEvent(e);
                } else {
                    recoverQueue.add(swipedPos);
                    swipedPos = -1;
                    recoverSwipedItem();
                }
            }
            return false;
        }
    };

    /**
     * Constructor for SwipeWithButtonsHelper
     *
     * @param context      activity context
     * @param recyclerView recycler view
     */
    public SwipeAndDragTouchHelper(Context context, RecyclerView recyclerView) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.buttons = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        buttonsBuffer = new HashMap<>();
        recoverQueue = new LinkedList<>() {
            // does not add Item if it is already contained
            @Override
            public boolean add(Integer item) {
                return !contains(item) && super.add(item);
            }
        };

        attachCallback();
    }

    /**
     * Does not reorder items
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder   The ViewHolder which is being dragged by the user.
     * @param target       The ViewHolder over which the currently active item is being
     *                     dragged.
     * @return true
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();

        if (swipedPos != pos)
            recoverQueue.add(swipedPos);

        swipedPos = pos;

        if (buttonsBuffer.containsKey(swipedPos))
            buttons = buttonsBuffer.get(swipedPos);
        else
            buttons.clear();

        buttonsBuffer.clear();
        swipeThreshold = 0.5f * buttons.size() * BUTTON_WIDTH;
        recoverSwipedItem();
    }

    /**
     * How much the rv_holder should slide on the screen.
     *
     * @param viewHolder The ViewHolder that is being dragged.
     * @return swipeThreshold
     */
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

        if (pos < 0) {
            swipedPos = pos;
            return;
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                List<UnderlayButton> buffer = new ArrayList<>();
                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer);
                    buttonsBuffer.put(pos, buffer);
                } else {
                    buffer = buttonsBuffer.get(pos);
                }

                translationX = dX * buffer.size() * BUTTON_WIDTH / itemView.getWidth();
                drawButtons(c, itemView, buffer, pos, translationX);
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && isCurrentlyActive) {
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
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    public void recoverCurrentItem() {
        if (swipedPos >= 0) {
            recoverQueue.add(swipedPos);
            swipedPos = -1;
            recoverSwipedItem();
        }
    }

    private synchronized void recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            int pos = recoverQueue.poll();
            if (pos > -1) {
                recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    private void drawButtons(Canvas c, View itemView, List<UnderlayButton> buffer, int pos, float dX) {
        float right = itemView.getRight();
        float dButtonWidth = (-1) * dX / buffer.size();

        for (UnderlayButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(
                    c,
                    new RectF(
                            left,
                            itemView.getTop(),
                            right,
                            itemView.getBottom()
                    ),
                    pos
            );
            right = left;
        }
        drawRemainingBackground(c, itemView, right, buffer.get(buffer.size() - 1));
    }

    private void drawRemainingBackground(Canvas c, View itemView, float right, UnderlayButton lastButton) {
        Paint p = new Paint();
        p.setColor(lastButton.backgroundColor);
        RectF remainingBackground = new RectF(right - 64, itemView.getTop(), right, itemView.getBottom());
        c.drawRect(remainingBackground, p);
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

                revertHighlightFolder(folder);
                if (draggedItem != null && moveInto(draggedItem.getAdapterPosition(), recyclerView.getChildLayoutPosition(folder))) {
                    draggedItem.itemView.setVisibility(View.INVISIBLE);
                }
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
     * @return true if moving was successful and itemview can be made invisible
     */
    protected abstract boolean moveInto(int from, int into);

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

    public void attachCallback() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);

    public static class UnderlayButton {
        private final String text;
        private final int imageResId;
        private final float imageScale;
        private final int backgroundColor;
        private final int foregroundColor;
        private int pos;
        private RectF clickRegion;
        private final UnderlayButtonClickListener clickListener;

        public UnderlayButton(String text, int backgroundColor, int foregroundColor, UnderlayButtonClickListener clickListener) {
            this(text, 0, 1, backgroundColor, foregroundColor, clickListener);
        }

        public UnderlayButton(int imageResId, int backgroundColor, int foregroundColor, UnderlayButtonClickListener clickListener) {
            this(imageResId, .8f, backgroundColor, foregroundColor, clickListener);
        }

        public UnderlayButton(int imageResId, float imageScale, int backgroundColor, int foregroundColor, UnderlayButtonClickListener clickListener) {
            this(null, imageResId, imageScale, backgroundColor, foregroundColor, clickListener);
        }

        private UnderlayButton(String text, int imageResId, float imageScale, int backgroundColor, int foregroundColor, UnderlayButtonClickListener clickListener) {
            this.text = text;
            this.imageResId = imageResId;
            this.imageScale = imageScale;
            this.backgroundColor = App.getResources().getColor(backgroundColor, App.getApplicationContext().getTheme());
            this.foregroundColor = App.getResources().getColor(foregroundColor, App.getApplicationContext().getTheme());
            this.clickListener = clickListener;
        }

        /**
         * Fires {@link UnderlayButtonClickListener#onClick(int)} if given coords are inside clickRegion
         *
         * @param x x coordinate
         * @param y y coordinate
         * @return true if coordinates are inside region and onClick() event was fired.
         */
        public boolean onClick(float x, float y) {
            if (clickRegion != null && clickRegion.contains(x, y)) {
                clickListener.onClick(pos);
                return true;
            }
            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos) {
            Paint p = new Paint();

            p.setColor(backgroundColor);
            c.drawRect(rect, p);

            p.setColor(foregroundColor);
            if (text == null) {
                // draw image
                if (imageResId != 0) {
                    Drawable drawable = ResourcesCompat.getDrawable(App.getResources(), imageResId, App.getApplicationContext().getTheme());
                    if (drawable != null) {
                        drawable.setTint(foregroundColor);
                        Bitmap bitmap = drawableToBitmap(drawable);
                        RectF imageRect = new RectF(rect);
                        float dimension = Math.min(rect.width() * imageScale, rect.height() * imageScale);
                        float centerHorizontal = (rect.left + rect.right) / 2;
                        float centerVertical = (rect.bottom + rect.top) / 2;
                        imageRect.left = Math.max(rect.left, centerHorizontal - dimension / 2);
                        imageRect.right = Math.min(rect.right, centerHorizontal + dimension / 2);
                        imageRect.bottom = Math.min(rect.bottom, centerVertical + dimension / 2);
                        imageRect.top = Math.max(rect.top, centerVertical - dimension / 2);
                        c.drawBitmap(bitmap, null, imageRect, p);
                    }
                }
            } else {
                // draw text
                p.setTextSize(40);
                Rect r = new Rect();
                float cHeight = rect.height();
                float cWidth = rect.width();
                p.setTextAlign(Paint.Align.LEFT);
                p.getTextBounds(text, 0, text.length(), r);
                float x = cWidth / 2f - r.width() / 2f - r.left;
                float y = cHeight / 2f + r.height() / 2f - r.bottom;
                c.drawText(text, rect.left + x, rect.top + y, p);

            }

            clickRegion = rect;
            this.pos = pos;
        }

        /**
         * Converts Drawable to bitmap.
         *
         * @param drawable drawable
         * @return converted bitmap
         */
        private Bitmap drawableToBitmap(Drawable drawable) {
            if (drawable instanceof BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap();
            }

            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }
    }

    /**
     * Interface with onClick-method which is fired from {@link UnderlayButton}
     */
    public interface UnderlayButtonClickListener {
        void onClick(int pos);
    }
}
