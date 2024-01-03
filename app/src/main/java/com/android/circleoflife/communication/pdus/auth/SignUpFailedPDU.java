package com.android.circleoflife.communication.pdus.auth;

import com.android.circleoflife.communication.pdus.PDUWithNoData;

import java.io.IOException;
import java.io.InputStream;

/**
 * Part of the {@link com.android.circleoflife.communication.protocols.SignUpProtocol SignUpProtocol}.
 * Server sends this to client if signUp failed
 */
public class SignUpFailedPDU implements PDUWithNoData {

    public static final int ID = 104;

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
    public static SignUpFailedPDU fromInputStream(InputStream is) throws IOException {
        SignUpFailedPDU pdu = new SignUpFailedPDU();
        pdu.deserialize(is);
        return pdu;
    }
}
