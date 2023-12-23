package com.android.circleoflife.ui.activities.categories.not_root.recycler_view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.circleoflife.R;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.ui.activities.categories.not_root.recycler_view.RVHolderInterface;

import java.util.function.Function;

public class CycleHolder extends Holder<Cycle> {

    private final TextView title;
    private final TextView frequency;

    public CycleHolder(@NonNull View itemView, RVHolderInterface holderInterface, Function<Integer, Cycle> itemFromPositionGetter) {
        super(itemView, holderInterface, itemFromPositionGetter);
        title = itemView.findViewById(R.id.crv_cycle_item_title);
        frequency = itemView.findViewById(R.id.crv_cycle_item_frequency);
    }

    @Override
    protected void onClick(Cycle cycle) {
        holderInterface.onCycleClicked(cycle);
    }

    @Override
    protected void onLongClick(Cycle cycle) {
        holderInterface.onCycleLongClicked(cycle);
    }

    public TextView getTitleView() {
        return title;
    }

    /**
     * Getter for frequency
     * @return frequencyTextView
     */
    public TextView getFrequencyView() {
        return frequency;
    }

}
