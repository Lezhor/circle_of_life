package com.android.circleoflife.communication.socket_communication;

import com.android.circleoflife.communication.SyncProtocolEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * SocketCommunication used in {@link SyncProtocolEngine}
 */
public class SocketCommunicationImpl implements SocketCommunication {

    /**
     * Default IP address
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
    public SocketCommunicationImpl(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    /**
     * Default Constructor for te SocketCommunication
     */
    public SocketCommunicationImpl() {
        this(SERVER_IP, PORT);
    }

    @Override
    public void connectToServer(String serverIP, int port) throws IOException {
        if (!connected()) {
            try {
                socket = new Socket(serverIP, port);
            } catch (UnknownHostException e) {
                throw new IOException("Host unknown");
            }
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
    public InputStream getInputStream()  {
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
