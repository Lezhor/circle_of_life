package com.android.circleoflife.communication.protocols;

import com.android.circleoflife.Application;

import java.util.Date;

/**
 * Implementation of interface {@link SyncProtocol}.<br>
 * Implements {@link SyncProtocolEngine#sync()} and {@link SyncProtocolEngine#getLastSuccessfulSyncDate()}<br><br>
 * <code>
 *     PROTOCOL_NAME = "COL_SyncProt";<br>
 *     VERSION = "v1.0";<br><br>
 * </code>
 * Follows the singleton pattern.
 * @see Application#getSyncProtocol()
 * @see SyncProtocol
 */
public class SyncProtocolEngine implements SyncProtocol {

    private static SyncProtocolEngine instance;

    /**
     * Returns the only existing instance of this class
     * @return instance of this class
     */
    public static SyncProtocolEngine getInstance() {
        if (instance == null) {
            instance = new SyncProtocolEngine();
        }
        return instance;
    }

    /**
     * Private Constructor (for singleton principle)
     */
    private SyncProtocolEngine() {
    }

    public static String PROTOCOL_NAME = "COL_SyncProt";
    public static String VERSION = "v1.0";

    @Override
    public boolean sync() {
        // TODO: 30.11.2023 Implement Sync Protocol
        return false;
    }

    @Override
    public Date getLastSuccessfulSyncDate() {
        // TODO: 30.11.2023 Get last successful Sync Date
        return null;
    }

    @Override
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
