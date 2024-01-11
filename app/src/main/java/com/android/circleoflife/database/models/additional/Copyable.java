package com.android.circleoflife.database.models.additional;

import androidx.annotation.NonNull;

import com.android.circleoflife.ui.dialogs.EditNameDialog;

/**
 * An interface which offers the clone method. Used in {@link  EditNameDialog}
 * @param <E> type that is returned. should be same class as the class implementing this interface.
 */
public interface Copyable<E> {
    @NonNull
    E copy();
}
