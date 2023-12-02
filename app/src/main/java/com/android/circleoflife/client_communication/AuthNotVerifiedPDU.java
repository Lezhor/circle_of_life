package com.android.circleoflife.client_communication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Send from server to client in case the authentication was not successful
 */
public class AuthNotVerifiedPDU implements PDU {

    final static int ID = 3;

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
    public static AuthNotVerifiedPDU fromInputStream(InputStream is) throws IOException {
        AuthNotVerifiedPDU pdu = new AuthNotVerifiedPDU();
        pdu.deserialize(is);
        return pdu;
    }
}
