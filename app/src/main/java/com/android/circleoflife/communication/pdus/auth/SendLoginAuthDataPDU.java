package com.android.circleoflife.communication.pdus.auth;

import com.android.circleoflife.communication.pdus.PDU;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Part of the LoginProtocol. Is being send from client to server to log in.
 */
public class SendLoginAuthDataPDU implements PDU {

    public final static int ID = 101;

    private String username;
    private String password;

    @Override
    public int getID() {
        return ID;
    }

    /**
     * Initializes username and password which are then ready to be serialized.
     *
     * @param username username (not displayed variant)
     * @param password password
     */
    public SendLoginAuthDataPDU(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void serialize(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(getID());
        dos.writeUTF(username);
        dos.writeUTF(password);
    }

    @Override
    public void deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        this.username = dis.readUTF();
        this.password = dis.readUTF();
    }

    /**
     * Deserializes LoginPDU from inputstream by calling {@link #deserialize(InputStream)}
     * @param is input stream
     * @return instance of type LoginPDU
     * @throws IOException if deserializing fails
     */
    public static SendLoginAuthDataPDU fromInputStream(InputStream is) throws IOException {
        SendLoginAuthDataPDU pdu = new SendLoginAuthDataPDU("", "");
        pdu.deserialize(is);
        return pdu;
    }

    /**
     * Getter for username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for password
     * @return password
     */
    public String getPassword() {
        return password;
    }
}
