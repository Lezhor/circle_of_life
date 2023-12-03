package com.android.circleoflife.communication.socket_communication;

import com.android.circleoflife.Application;
import com.android.circleoflife.communication.protocols.SyncProtocolEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * SocketCommunication used in {@link SyncProtocolEngine}<br>
 * @see Application#openCommunicationSessionWithServer()
 */
public interface SocketCommunication {

    /**
     * Connects to Server with Server IP and Port.<br>
     * Doesn't even try to connect if socket is not null
     * @throws IOException if connecting to server fails.
     */
    void connectToServer(String ip, int port) throws IOException;

    /**
     * Connects to Server with Server IP and Port which aer defined in this class.<br>
     * Doesn't even try to connect if socket is not null
     * @throws IOException if connecting to server fails.
     * @see SocketCommunication#getServerIP()
     * @see SocketCommunication#getPort()
     */
    default void connectToServer() throws IOException {
        connectToServer(getServerIP(), getPort());
    }

    /**
     * Disconnects from server.
     */
    void disconnectFromServer() throws IOException;

    /**
     * checks if the socket is connected to the server
     * @return true if the socket is connected.
     */
    boolean connected();

    /**
     * Returns inputstream of the socket. If there is no connection returns null;
     * @return InputStream of the socket.
     */
    InputStream getInputStream();

    /**
     * Returns OutputStream of the socket. If there is no connection returns null;
     * @return OutputStream of the socket.
     */
    OutputStream getOutputStream();

    /**
     * Getter for Server IP
     * @return serverIP
     */
    String getServerIP();

    /**
     * Getter for port
     * @return port
     */
    int getPort();
}
