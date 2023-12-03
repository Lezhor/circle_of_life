package com.android.circleoflife;

import com.android.circleoflife.communication.socket_communication.SocketCommunication;
import com.android.circleoflife.communication.socket_communication.SocketCommunicationImpl;
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

    // TODO: 03.12.2023 SERVER connection data can't just be saevd in final attributes! Change!

    /**
     * IP of the server
     */
    public final static String SERVER_IP = "";

    /**
     * Port the server listens on
     */
    public final static int PORT = 7777;

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

    /**
     * Creates an instance of {@link SocketCommunication}, which has the set default values for communicating with server
     * @return instance of SocketCommunication
     */
    public static SocketCommunication openCommunicationSessionWithServer() {
        return new SocketCommunicationImpl(SERVER_IP, PORT);
    }

}
