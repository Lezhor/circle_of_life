package com.android.circleoflife.database.models.additional;

import androidx.annotation.NonNull;

import java.util.UUID;

/**
 * Every model implements this interface. It offers just the getUserId() method
 */
public interface HasUserId {

    /**
     * Getter for the userId
     * @return userId
     */
    @NonNull
    UUID getUserID();
}
