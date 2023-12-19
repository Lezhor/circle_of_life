package com.android.circleoflife.ui.other;

import android.util.Log;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A straight up implementation of filtering Lists of entities. Filters so that the char-sequence should be contained in the specified attribute.
 * @param <E> Entity type to be filtered
 */
public abstract class EntityFilter<E> extends Filter {

    private final Function<E, String> filteredAttribute;
    private final Callable<? extends List<E>> listGetter;

    public EntityFilter(@NonNull Function<E, String> filteredAttribute, @NonNull Callable<? extends List<E>> listGetter) {
        super();
        this.filteredAttribute = filteredAttribute;
        this.listGetter = listGetter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        List<E> filteredList = new ArrayList<>();
        try {
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listGetter.call());
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                filteredList.addAll(listGetter.call().stream().filter(entity -> filteredAttribute.apply(entity).toLowerCase().contains(filterPattern)).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            Log.w("EntityFilter", "performFiltering: Exception occured", e);
        }
        FilterResults results = new FilterResults();
        results.values = filteredList;
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        publishResults((List<E>) results.values);
    }

    /**
     * After the filtering was called this method is called with the filteredList
     * @param filteredLit filteredList
     */
    protected abstract void publishResults(List<E> filteredLit);
}
