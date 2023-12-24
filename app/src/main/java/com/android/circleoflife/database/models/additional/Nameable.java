package com.android.circleoflife.database.models.additional;

/**
 * Interface with get and set methods for name. Used in e.g. {@link com.android.circleoflife.ui.activities.categories.EditNameDialog}.
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
