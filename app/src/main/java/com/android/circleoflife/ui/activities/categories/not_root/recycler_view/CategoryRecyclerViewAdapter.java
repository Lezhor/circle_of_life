package com.android.circleoflife.ui.activities.categories.not_root.recycler_view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.circleoflife.R;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder.CategoryHolder;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder.CycleHolder;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder.TodoHolder;
import com.android.circleoflife.ui.other.EntityFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Recycler-View for displaying a list of categories, cycles and todos
 * The categoryList-Attribute holds every single root-category
 * The filteredList holds the categories which are actually displayed currently.<br>
 * Filtering happens in the {@link CategoryRecyclerViewAdapter#getFilter()} method.
 */
public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final String TAG = "CategoryRecyclerViewAdapter";

    // TODO: 21.12.2023 Implemnt Recycler View with three different Holders

    private final List<RVItemWrapper<?>> fullList = new ArrayList<>();
    private final List<RVItemWrapper<?>> filteredList = new ArrayList<>();

    private final RVHolderInterface holderInterface;

    public CategoryRecyclerViewAdapter(@NonNull RVHolderInterface holderInterface) {
        this.holderInterface = holderInterface;
    }

    public RVItemWrapper<?> getFilteredItemAtIndex(int index) {
        if (index >= 0 && index < filteredList.size()) {
            return filteredList.get(index);
        } else {
            return null;
        }
    }

    public void setCategories(List<Category> categoryList) {
        boolean filtering = this.fullList.size() > filteredList.size();
        synchronized (this.fullList) {
            Log.d(TAG, "setCategories: " + categoryList.stream().map(Category::getName).reduce("Categories: ", (a, b) -> a + b + "; "));
            this.fullList.removeIf(w -> w.getItemType() == RVItemWrapper.TYPE_CATEGORY);
            this.fullList.addAll(categoryList.stream().map(RVItemWrapper<Category>::new).collect(Collectors.toSet()));
            Collections.sort(this.fullList);
        }
        if (!filtering) {
            setFilteredList(this.fullList);
        }
    }

    public void setCycles(List<Cycle> cycleList) {
        // TODO: 22.12.2023 set cycles
    }

    public void setTodos(List<Todo> todoList) {
        // TODO: 22.12.2023 set todos
    }

    private void setFilteredList(List<RVItemWrapper<?>> filteredList) {
        if (this.filteredList.size() == 0) {
            this.filteredList.addAll(filteredList);
            notifyItemRangeInserted(0, filteredList.size());
            return;
        }

        List<RVItemWrapper<?>> added = filteredList.stream().filter(c -> !this.filteredList.contains(c)).collect(Collectors.toList());
        List<RVItemWrapper<?>> removed = this.filteredList.stream().filter(c -> !filteredList.contains(c)).collect(Collectors.toList());

        if (added.size() == 0 && removed.size() == 0) {
            // maybe something changed
            Log.d(TAG, "setFilteredLists: no remove, no add, iterating....");
            Log.d(TAG, this.filteredList.stream().map(RVItemWrapper::getName).reduce("old list: ", (a, b) -> a + b + "; "));
            Log.d(TAG, filteredList.stream().map(RVItemWrapper::getName).reduce("new list: ", (a, b) -> a + b + "; "));
            for (int i = 0; i < filteredList.size(); i++) {
                if (this.filteredList.contains(filteredList.get(i))) {
                    int index = this.filteredList.indexOf(filteredList.get(i));
                    if (!this.filteredList.get(index).equalsAllParams(filteredList.get(i))) {
                        if (i == index) {
                            this.filteredList.remove(index);
                            this.filteredList.add(index, filteredList.get(i));
                            notifyItemChanged(index);
                        } else {
                            this.filteredList.remove(index);
                            notifyItemRemoved(index);
                            int newIndex = getAddIndex(this.filteredList, filteredList.get(i));
                            this.filteredList.add(newIndex, filteredList.get(i));
                            notifyItemInserted(newIndex);
                        }
                    }
                }
            }
        }
        Map<RVItemWrapper<?>, Integer> addMap = added.stream()
                .collect(Collectors.toMap(Function.identity(), c -> getAddIndex(this.filteredList, c)));
        for (Map.Entry<RVItemWrapper<?>, Integer> entry : addMap.entrySet().stream()
                .peek(e -> e.setValue(-e.getValue()))
                .sorted(Map.Entry.comparingByValue())
                .peek(e -> e.setValue(-e.getValue()))
                .collect(Collectors.toList())) {
            synchronized (this.filteredList) {
                Log.d(TAG, "setFilteredList: Add at index: " + entry.getValue());
                this.filteredList.add(entry.getValue(), entry.getKey());
            }
            notifyItemInserted(entry.getValue());
        }

        Map<RVItemWrapper<?>, Integer> removeMap = removed.stream()
                .collect(Collectors.toMap(Function.identity(), c -> getRemoveIndex(this.filteredList, c)));
        for (Map.Entry<RVItemWrapper<?>, Integer> entry : removeMap.entrySet().stream()
                .peek(e -> e.setValue(-e.getValue()))
                .sorted(Map.Entry.comparingByValue())
                .peek(e -> e.setValue(-e.getValue()))
                .collect(Collectors.toList())) {
            synchronized (this.filteredList) {
                Log.d(TAG, "setFilteredList: Remove from index: " + entry.getValue());
                this.filteredList.remove(entry.getKey());
            }
            notifyItemRemoved(entry.getValue());
        }
    }

    private int getAddIndex(List<RVItemWrapper<?>> list, RVItemWrapper<?> item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).compareTo(item) > 0) {
                return i;
            }
        }
        return list.size();
    }

    private int getRemoveIndex(List<RVItemWrapper<?>> list, RVItemWrapper<?> item) {
        return list.indexOf(item);
    }

    @Override
    public int getItemViewType(int position) {
        return this.filteredList.get(position).getItemType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case RVItemWrapper.TYPE_CATEGORY -> {
                View view = inflater.inflate(R.layout.root_category_item, parent, false);
                return new CategoryHolder(view, holderInterface, pos -> ((Category) getFilteredItemAtIndex(pos).getObject()));
            }
            case RVItemWrapper.TYPE_CYCLE -> {
                View view = inflater.inflate(R.layout.category_rv_cycle_item, parent, false);
                return new CycleHolder(view, holderInterface, pos -> ((Cycle) getFilteredItemAtIndex(pos).getObject()));
            }
            case RVItemWrapper.TYPE_TODO -> {
                View view = inflater.inflate(R.layout.category_rv_todo_item, parent, false);
                return new TodoHolder(view, holderInterface, pos -> ((Todo) getFilteredItemAtIndex(pos).getObject()));
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // TODO: 22.12.2023 Init Viewholder based on position
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    private List<RVItemWrapper<?>> getFullList() {
        return fullList;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new EntityFilter<>(RVItemWrapper::getName, this::getFullList) {
        @Override
        protected void publishResults(List<RVItemWrapper<?>> resultList) {
            setFilteredList(resultList);
        }
    };
}
