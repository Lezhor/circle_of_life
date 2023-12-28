package com.android.circleoflife.ui.viewmodels;

/**
 * A ViewModel can implement this interface so that the actions it takes can be reverted.
 */
public interface RevertibleActions {

    /**
     * Returns text description of last performed action
     * @return text description of last performed action
     */
    String getLastActionText();

    /**
     * reverts last action
     * returns true if reverted and false if there is nothing to revert
     */
    boolean revertLastAction();


}
