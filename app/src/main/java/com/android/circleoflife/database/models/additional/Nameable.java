package com.android.circleoflife.database.models.additional;

import com.android.circleoflife.ui.dialogs.EditNameDialog;

/**
 * Interface with get and set methods for name. Used in e.g. {@link EditNameDialog}.
 */
public interface Nameable {

    /**
     * Setter for name
     * @param name new name
     */
    void setName(String name);

    /**
     * Getter for name
     * @return name
     */
    String getName();

}
