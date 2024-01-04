package com.android.circleoflife.communication.protocols;

import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * via this interface the sync protocol session can be started.
 * Upon starting there is no control over the Sync-Protocol
 */
public interface SyncProtocol extends Protocol {

    /**
     * Calling this method kicks off the communication with the server
     * @param user user that needs to be authenticated
     * @param logs to be sent to Server
     * @param outLogs where the received sql-queries
     * @return true if sync was successful. false if either synchronisation didn't work or connection to server failed
     * @throws IOException if synchronisation fails
     */
    boolean sync(User user, DBLog<?>[] logs, List<DBLog<?>> outLogs) throws IOException;

    /**
     * Returns the timestamp when the last successful sync happened.
     * @return timestamp of last successful sync
     */
    LocalDateTime getLastSuccessfulSyncDate();

}
