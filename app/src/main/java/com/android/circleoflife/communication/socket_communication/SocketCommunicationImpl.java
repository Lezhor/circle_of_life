package com.android.circleoflife.communication.socket_communication;

import com.android.circleoflife.communication.protocols.SyncProtocolEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * SocketCommunication used in {@link SyncProtocolEngine}
 */
public class SocketCommunicationImpl implements SocketCommunication {

    /**
     * Timeout after which stops trying to connect to server.
     */
    public final static int CONNECTION_TIMEOUT_MILLIS = 1500;

    private final String serverIP;
    private final int port;

    private Socket socket;


    /**
     * Constructor for SocketCommunication
     *
     * @param serverIP new value for serverIP
     * @param port     new value for port
     */
    public SocketCommunicationImpl(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    @Override
    public void connectToServer(String serverIP, int port) throws IOException {
        if (!connected()) {
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverIP, port), CONNECTION_TIMEOUT_MILLIS);
        }
    }

    /**
     * Disconnects from server.
     * After this method is called the socket attribute is always null.
     */
    @Override
    public void disconnectFromServer() {
        if (connected()) {
            try {
                socket.close();
            } catch (IOException ignored) {
            } finally {
                socket = null;
            }
        }
    }

    @Override
    public boolean connected() {
        if (socket == null) {
            return false;
        }
        if (socket.isConnected()) {
            return true;
        } else {
            socket = null;
            return false;
        }
    }


    @Override
    public InputStream getInputStream() {
        if (connected()) {
            try {
                return socket.getInputStream();
            } catch (NullPointerException | IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        if (connected()) {
            try {
                return socket.getOutputStream();
            } catch (NullPointerException | IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String getServerIP() {
        return serverIP;
    }

    @Override
    public int getPort() {
        return port;
    }
}
