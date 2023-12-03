package com.android.circleoflife;

import com.android.circleoflife.communication.SyncProtocol;
import com.android.circleoflife.communication.SyncProtocolEngine;
import com.android.circleoflife.logging.serializing.LogSerializer;
import com.android.circleoflife.logging.serializing.LogSerializerImpl;

/**
 * This project follows the Singleton pattern.<br>
 * Every Class which allows only one existing instance is accessed from this class.<br>
 * This way every component can access each other component easily
 */
public final class Application {

    /**
     * {@link LogSerializer} follows the Singleton pattern.
     * @return the only existing instance of {@link LogSerializer}
     * @see LogSerializerImpl#getInstance()
     */
    public static LogSerializer getLogSerializer() {
        return LogSerializerImpl.getInstance();
    }

    /**
     * {@link SyncProtocol} follows the Singleton pattern.
     * @return the only existing instance of {@link SyncProtocol}
     * @see SyncProtocolEngine#getInstance()
     */
    public static SyncProtocol getSyncProtocol() {
        return SyncProtocolEngine.getInstance();
    }

}
