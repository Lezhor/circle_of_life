package com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder;

import android.view.View;

import androidx.annotation.NonNull;

import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;

import java.util.function.Function;

public class CycleHolder extends Holder<Cycle> {
    public CycleHolder(@NonNull View itemView, RVHolderInterface holderInterface, Function<Integer, Cycle> itemFromPositionGetter) {
        super(itemView, holderInterface, itemFromPositionGetter);
        // TODO: 22.12.2023 init cycle layout
    }

    @Override
    protected void onClick(Cycle cycle) {
        holderInterface.onCycleClicked(cycle);
    }

    @Override
    protected void onLongClick(Cycle cycle) {
        holderInterface.onCycleLongClicked(cycle);
    }
}
