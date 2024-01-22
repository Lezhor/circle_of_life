package com.android.circleoflife.communication.models;

import androidx.annotation.Nullable;

import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Wrapper class for everything the {@link com.android.circleoflife.communication.protocols.SyncProtocolEngine#sync(User, LocalDateTime, DBLog[], List) sync()}
 * method needs to return.
 */
public interface SyncResult {

    /**
     * Returns user which got synced (or which failed sync
     * @return instance of class User
     */
    User getUser();

    /**
     * Whether or not sync was successful
     * @return true if sync was successful
     */
    boolean wasSuccessful();

    /**
     * Returns timestamp of new last synchronisation (stays same as passed, if synchronisation fails)
     * @return new timestamp of last synchronisation
     */
    LocalDateTime getNewLastSyncDate();

    /**
     * Returns instructions, the server sent to the client
     * @return instructions the server sent to the client
     */
    List<DBLog<?>> getOutLogs();

    /**
     * Exception if an exception occurred or null if no exception occurred
     * @return exception
     */
    @Nullable
    IOException getException();
}
