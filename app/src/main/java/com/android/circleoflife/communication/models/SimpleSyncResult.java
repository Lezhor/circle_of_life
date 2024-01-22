package com.android.circleoflife.communication.models;

import androidx.annotation.Nullable;

import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of interface SyncResult. Has Attributes for each specified getter. Offers constructor to set all attributes
 */
public class SimpleSyncResult implements SyncResult {

    private final User user;
    private final boolean successful;
    private final LocalDateTime newLastSyncDate;
    private final List<DBLog<?>> outLogs;
    private final IOException exception;

    /**
     * Constructor for SimpleSyncResult. Sets outLogs to empty list and exception to null
     * @param user user to whom the result belongs
     * @param successful whether or not the synchronisation was successful
     * @param newLastSyncDate new timestamp of last synchronisation
     */
    public SimpleSyncResult(User user, boolean successful, LocalDateTime newLastSyncDate) {
        this(user, successful, newLastSyncDate, new LinkedList<>(), null);
    }

    /**
     * Constructor for SimpleSyncResult
     * @param user user to whom the result belongs
     * @param successful whether or not the synchronisation was successful
     * @param newLastSyncDate new timestamp of last synchronisation
     * @param outLogs instructions sent from server
     * @param exception pass null if no exception occurred
     */
    public SimpleSyncResult(User user, boolean successful, LocalDateTime newLastSyncDate, List<DBLog<?>> outLogs, @Nullable IOException exception) {
        this.user = user;
        this.successful = successful;
        this.newLastSyncDate = newLastSyncDate;
        this.outLogs = outLogs;
        this.exception = exception;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean wasSuccessful() {
        return successful;
    }

    @Override
    public LocalDateTime getNewLastSyncDate() {
        return newLastSyncDate;
    }

    @Override
    public List<DBLog<?>> getOutLogs() {
        return outLogs;
    }

    @Nullable
    @Override
    public IOException getException() {
        return exception;
    }
}
