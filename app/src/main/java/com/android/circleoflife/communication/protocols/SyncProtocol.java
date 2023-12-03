package com.android.circleoflife.communication.protocols;

import java.util.Date;

/**
 * via this interface the sync protocol session can be started.
 * Upon starting there is no control over the Sync-Protocol
 */
public interface SyncProtocol extends Protocol {

    /**
     * Calling this method kicks off the communication with the server
     * @return true if sync was successful. false if either synchronisation didn't work or connection to server failed
     */
    boolean sync();

    /**
     * Returns the timestamp when the last successful sync happened.
     * @return timestamp of last successful sync
     */
    Date getLastSuccessfulSyncDate();

}
