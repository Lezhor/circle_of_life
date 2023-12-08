package com.android.circleoflife.communication.protocols;

import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.database.control.DatabaseController;
import com.android.circleoflife.logging.model.DBLog;

import java.util.Date;
import java.util.List;

/**
 * via this interface the sync protocol session can be started.
 * Upon starting there is no control over the Sync-Protocol
 */
public interface SyncProtocol extends Protocol {

    /**
     * Calling this method kicks off the communication with the server
     * @param auth authentication that is sent to Server
     * @param logs to be sent to Server
     * @param outSQLQueries where the received sql-queries
     * @return true if sync was successful. false if either synchronisation didn't work or connection to server failed
     */
    boolean sync(Authentication auth, DBLog[] logs, List<String> outSQLQueries);

    /**
     * Returns the timestamp when the last successful sync happened.
     * @return timestamp of last successful sync
     */
    Date getLastSuccessfulSyncDate();

}
