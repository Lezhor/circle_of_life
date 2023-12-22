package com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;

import java.util.function.Function;

public abstract class Holder<T> extends RecyclerView.ViewHolder {
    protected final RVHolderInterface holderInterface;
    protected final Function<Integer, T> itemFromPositionGetter;

    public Holder(@NonNull View itemView, RVHolderInterface holderInterface, Function<Integer, T> itemFromPositionGetter) {
        super(itemView);

        this.holderInterface = holderInterface;
        this.itemFromPositionGetter = itemFromPositionGetter;

        itemView.setOnClickListener(view -> onClick());
        itemView.setOnLongClickListener(view -> {
            onLongClick();
            return true;
        });
    }


    private void onClick() {
        int pos = getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            onClick(itemFromPositionGetter.apply(pos));
        }
    }
    protected abstract void onClick(T object);

    private void onLongClick() {
        int pos = getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            onLongClick(itemFromPositionGetter.apply(pos));
        }
    }
    protected abstract void onLongClick(T object);

}
