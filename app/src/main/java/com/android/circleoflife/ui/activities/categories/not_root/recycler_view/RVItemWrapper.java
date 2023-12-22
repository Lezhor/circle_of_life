package com.android.circleoflife.ui.activities.categories.not_root.recycler_view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;

import java.util.Objects;

/**
 * This class is for wrapping Categories, Cycles and Todos so that they can all be displayed in the {@link CategoryRecyclerViewAdapter Recycler View}
 */
public class RVItemWrapper<T> implements Comparable<RVItemWrapper<?>> {

    public static final int TYPE_CATEGORY = 1;
    public static final int TYPE_CYCLE = 2;
    public static final int TYPE_TODO = 3;
    public static final int TYPE_UNDEFINED = -1;

    private final T object;

    /**
     * Constructor for this wrapper. Parameter will be saved in this wrapper and can't be changed.<br>
     * should be of type {@link Category}, {@link Cycle} or {@link Todo}
     * @param object
     */
    public RVItemWrapper(@NonNull T object) {
        this.object = object;
    }

    /**
     * Returns itemtype. 1,2,3 or -1 if not undefined<br>
     * Used in {@link CategoryRecyclerViewAdapter#getItemViewType(int)}
     * @return itemtype
     */
    public int getItemType() {
        if (object instanceof Category) {
            return TYPE_CATEGORY;
        } else if (object instanceof Cycle) {
            return TYPE_CYCLE;
        } else if (object instanceof Todo) {
            return TYPE_TODO;
        } else {
            return TYPE_UNDEFINED;
        }
    }

    /**
     * Calls getName() method on object (e.g. {@link Category#getName()} or {@link Cycle#getName()}
     * @return name of the object
     */
    public String getName() {
        return switch (getItemType()) {
            case TYPE_CATEGORY -> ((Category) object).getName();
            case TYPE_CYCLE -> ((Cycle) object).getName();
            case TYPE_TODO -> ((Todo) object).getName();
            default -> null;
        };
    }

    /**
     * Getter for object
     * @return object
     */
    public T getObject() {
        return object;
    }

    /**
     * Compares by name (case ignored)
     * @param that the object to be compared.
     * @return compared value
     */
    @Override
    public int compareTo(RVItemWrapper<?> that) {
        return this.getName().compareToIgnoreCase(that.getName());
    }

    /**
     * Wrappers are equal in case that objects are equal or if passed obj is of instance T and is equal bo this.object.
     * @param obj to compare to
     * @return true if obj is {@link RVItemWrapper} and its object is equal to this.object
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof RVItemWrapper<?> that) {
            return Objects.equals(this.object, that.object);
        }
        return this.object.equals(obj);
    }
}
