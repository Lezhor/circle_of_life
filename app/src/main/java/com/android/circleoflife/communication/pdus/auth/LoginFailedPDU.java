package com.android.circleoflife.communication.pdus.auth;

import com.android.circleoflife.communication.pdus.PDUWithNoData;

import java.io.IOException;
import java.io.InputStream;

/**
 * Part of the LoginProtocol. server sends this to client if login failed
 */
public class LoginFailedPDU implements PDUWithNoData {

    public static final int ID = 103;

    @Override
    public int getID() {
        return ID;
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
