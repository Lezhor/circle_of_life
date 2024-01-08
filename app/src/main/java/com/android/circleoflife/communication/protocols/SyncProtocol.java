package com.android.circleoflife.communication.protocols;

import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

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
     * @param lastSyncDate timestamp of last successful synchronisation
     * @param logs to be sent to Server
     * @param outLogs where the received sql-queries
     * @return new timestamp of successful synchronisation or null if synchronisation failed
     */
    LocalDateTime sync(User user, LocalDateTime lastSyncDate, DBLog<?>[] logs, List<DBLog<?>> outLogs);

}
