package com.android.circleoflife.communication.pdus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * PDU the server sends to client if authentication succeeded. has no data block
 */
public class AuthVerifiedPDU implements PDU {

    public final static int ID = 2;

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
    public void deserialize(InputStream is) {

    }

    /**
     * Returns an instance of this class by calling {@link SyncSuccessfulPDU#deserialize(InputStream)}
     * @param is Inputstream
     * @return deserialized PDU
     */
    public static AuthVerifiedPDU fromInputStream(InputStream is) throws IOException {
        AuthVerifiedPDU pdu = new AuthVerifiedPDU();
        pdu.deserialize(is);
        return pdu;
    }
}
