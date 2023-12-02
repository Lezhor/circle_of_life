package com.android.circleoflife.client_communication;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Tests the Socket communication by creating a Server Thread and connecting to it (localhost)
 */
public class SocketCommunicationTest {

    public int PORT = 7777;

    Thread serverThread;
    Socket serverSocket;
    static ServerSocket server;

    /**
     * Creates Serversocket and a serverThread with server.accept()
     */

    public void setUp() {
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            fail("Failed creating server");
        }
        serverThread = new Thread(() -> {
            try {
                serverSocket = server.accept();
            } catch (IOException e) {
                fail("Failed to connect to server");
            }
        });
    }

    /**
     * Test succeeds it connectToServer throws an IOException (because no server found)
     */
    @Test
    public void testConnectToServerNoServer() {
        System.out.println("Testing ConnectToServer No Server");
        SocketCommunication com = new SocketCommunication("localhost", PORT);
        assertThrows(IOException.class, com::connectToServer);
    }

    /**
     * Server setup. Checks {@link SocketCommunication#connectToServer()}, {@link SocketCommunication#connected()}
     * and {@link SocketCommunication#disconnectFromServer()} methods.<br>
     * Also retrieves Input- and OutputStreams from SocketCommunication and from Server-Thread and checks if they are connected (by sending bytes back and forth)
     */
    @Test
    public void testServerConnection() {
        System.out.println("Testing ServerConnection");
        setUp();
        serverThread.start();
        SocketCommunication com = new SocketCommunication("localhost", PORT);
        assertFalse(com.connected());
        try {
            com.connectToServer();
        } catch (IOException e) {
            fail("Connecting to server failed");
            return;
        }
        assertTrue(com.connected());


        checkInputAndOutputStreams(com);


        com.disconnectFromServer();
        assertFalse(com.connected());
    }

    private void checkInputAndOutputStreams(SocketCommunication com) {
        InputStream clientIS = com.getInputStream();
        OutputStream clientOS = com.getOutputStream();
        assertNotNull(clientIS);
        assertNotNull(clientOS);
        InputStream serverIS;
        OutputStream serverOS;
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            fail("waiting for server Thread got interrupted");
        }
        try {
            serverIS = serverSocket.getInputStream();
            serverOS = serverSocket.getOutputStream();
        } catch (IOException e) {
            fail("Getting Server-Streams failed");
            return;
        }

        try {
            clientOS.write(5);
            assertEquals(5, serverIS.read());
            serverOS.write(236);
            assertEquals(236, clientIS.read());
        } catch (IOException e) {
            fail("Writing and reading with IOStreams failed");
        }
    }
}