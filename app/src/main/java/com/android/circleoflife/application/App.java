package com.android.circleoflife.application;

import android.content.Context;
import android.content.res.Resources;

import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.communication.socket_communication.SocketCommunication;
import com.android.circleoflife.communication.socket_communication.SocketCommunicationImpl;
import com.android.circleoflife.communication.protocols.SyncProtocol;
import com.android.circleoflife.communication.protocols.SyncProtocolEngine;
import com.android.circleoflife.logging.serializing.LogSerializer;
import com.android.circleoflife.logging.serializing.LogSerializerImpl;

import java.util.Objects;

/**
 * This project follows the Singleton pattern.<br>
 * Every Class which allows only one existing instance is accessed from this class.<br>
 * This way every component can access each other component easily
 */
public final class App {

    // TODO: 03.12.2023 SERVER connection data can't just be saevd in final attributes! Change!

    /**
     * IP of the server
     */
    public final static String SERVER_IP = "";

    /**
     * Port the server listens on
     */
    public final static int PORT = 7777;

    private static Context applicationContext;

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

    /**
     * {@link Authentication} follows the Singleton Pattern.
     * @return the only existing instance of {@link Authentication}
     */
    public Authentication getAuthentication() {
        // TODO: 03.12.2023 AuthenticationImpl singleton instance
        return null;
    }

    /**
     * Returns system resources. useful to access e.g. String from strings.xml:<br>
     * Can be used like this:
     * <pre>
     *     {@code String s = Application.getResources()
     *     .getString(R.string.my_string);}
     * </pre>
     * This resource Object does NOT contain data from
     * @return System resources
     */
    public static Resources getResources() {
        return Objects.requireNonNull(applicationContext).getResources();
    }

    public static void setApplicationContext(Context context) {
        applicationContext = context.getApplicationContext();
    }
}
