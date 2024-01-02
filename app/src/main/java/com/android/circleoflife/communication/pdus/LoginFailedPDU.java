package com.android.circleoflife.communication.pdus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Part of the LoginProtocol. server sends this to client if login failed
 */
public class LoginFailedPDU implements PDU {

    public static final int ID = 103;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void serialize(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(getID());
    }

    @Override
    public void deserialize(InputStream is) throws IOException {
    }

    /**
     * Deserializes pdu by calling {@link #deserialize(InputStream)}
     * @param is input stream
     * @return instance of this class
     * @throws IOException if deserializing fails
     */
    public static LoginFailedPDU fromInputStream(InputStream is) throws IOException {
        LoginFailedPDU pdu = new LoginFailedPDU();
        pdu.deserialize(is);
        return pdu;
    }
}
