package com.android.circleoflife.ui.activities.categories;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.circleoflife.R;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.ui.other.EntityFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryHolder> implements Filterable {
    private static final String TAG = "CategoryRecyclerViewAdapter";

    private final List<Category> categoryList = new ArrayList<>();
    private final List<Category> filteredList = new ArrayList<>();

    public Category getFilteredCategoryAtIndex(int index) {
        if (index >= 0 && index < filteredList.size()) {
            return filteredList.get(index);
        } else {
            return null;
        }
    }

    public void setCategories(List<Category> categoryList) {
        boolean filtering = this.categoryList.size() > filteredList.size();
        synchronized (this.categoryList) {
            Log.d(TAG, "setCategories: " + categoryList.stream().map(Category::getName).reduce("Categories: ", (a, b) -> a + b + "; "));
            this.categoryList.clear();
            this.categoryList.addAll(categoryList);
        }
        if (!filtering) {
            setFilteredCategories(categoryList);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setFilteredCategories(List<Category> filteredList) {
        if (this.filteredList.size() == 0) {
            this.filteredList.addAll(filteredList);
            notifyItemRangeInserted(0, filteredList.size());
            return;
        }

        List<Category> added = filteredList.stream().filter(c -> !this.filteredList.contains(c)).collect(Collectors.toList());
        List<Category> removed = this.filteredList.stream().filter(c -> !filteredList.contains(c)).collect(Collectors.toList());
        Map<Category, Integer> addMap = added.stream()
                .collect(Collectors.toMap(Function.identity(), c -> getAddIndex(this.filteredList, c)));
        for (Map.Entry<Category, Integer> entry : addMap.entrySet().stream()
                .peek(e -> e.setValue(-e.getValue()))
                .sorted(Map.Entry.comparingByValue())
                .peek(e -> e.setValue(-e.getValue()))
                .collect(Collectors.toList())) {
            synchronized (this.filteredList) {
                Log.d(TAG, "setFilteredCategories: Add at index: " + entry.getValue());
                this.filteredList.add(entry.getValue(), entry.getKey());
            }
            notifyItemInserted(entry.getValue());
        }

        Map<Category, Integer> removeMap = removed.stream()
                .collect(Collectors.toMap(Function.identity(), c -> getRemoveIndex(this.filteredList, c)));
        for (Map.Entry<Category, Integer> entry : removeMap.entrySet().stream()
                .peek(e -> e.setValue(-e.getValue()))
                .sorted(Map.Entry.comparingByValue())
                .peek(e -> e.setValue(-e.getValue()))
                .collect(Collectors.toList())) {
            synchronized (this.filteredList) {
                Log.d(TAG, "setFilteredCategories: Remove from index: " + entry.getValue());
                this.filteredList.remove(entry.getKey());
            }
            notifyItemRemoved(entry.getValue());
        }
    }

    private int getAddIndex(List<Category> list, Category item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().compareToIgnoreCase(item.getName()) > 0) {
                return i;
            }
        }
        return list.size();
    }

    private int getRemoveIndex(List<Category> list, Category item) {
        return list.indexOf(item);
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.root_category_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.title.setText(filteredList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    private List<Category> getCategoryList() {
        return categoryList;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    // TODO: 19.12.2023 Filtering should filter through ALL categories not just root-categories
    private final Filter filter = new EntityFilter<>(Category::getName, this::getCategoryList) {
        @Override
        protected void publishResults(List<Category> resultList) {
            setFilteredCategories(resultList);
        }
    };

    static class CategoryHolder extends RecyclerView.ViewHolder {

        private final TextView title;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.root_category_item);
        }
    }
}
