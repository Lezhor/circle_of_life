package com.android.circleoflife.client_communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * SocketCommunication used in {@link SyncProtocolEngine}
 */
class SocketCommunication {

    /**
     * Default IP adress
     */
    public static String SERVER_IP = "192.168.188.70";

    /**
     * Default port
     */
    public static int PORT = 7777;

    private final String serverIP;
    private final int port;

    private Socket socket;


    /**
     * Constructor for SocketCommunication
     * @param serverIP new value for serverIP
     * @param port new value for port
     */
    public SocketCommunication(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    /**
     * Default Constructor for te SocketCommunication
     */
    public SocketCommunication() {
        this(SERVER_IP, PORT);
    }

    /**
     * Connects to Server with Server IP and Port which are static final values.<br>
     * Doesn't even try to connect if socket is not null
     * @throws IOException if connecting to server fails.
     */
    public void connectToServer() throws IOException {
        if (!connected()) {
            try {
                socket = new Socket(serverIP, port);
            } catch (UnknownHostException e) {
                throw new IOException("Host unknown");
            }
        }
    }

    /**
     * Disconnects from server. After this method is called the socket attribute is always null.
     */
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

    /**
     * checks if the socket is connected to the server
     * @return true if the socket is connected.
     */
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

    /**
     * Returns inputstream of the socket. If there is no connection (socket is null) returns null;
     * @return InputStream of the socket.
     */
    public InputStream getInputStream()  {
        if (connected()) {
            try {
                return socket.getInputStream();
            } catch (NullPointerException | IOException ignored) {
            }
        }
        return null;
    }

    /**
     * Returns OutputStream of the socket. If there is no connection (socket is null) returns null;
     * @return OutputStream of the socket.
     */
    public OutputStream getOutputStream() {
        if (connected()) {
            try {
                return socket.getOutputStream();
            } catch (NullPointerException | IOException ignored) {
            }
        }
        return null;
    }
}
