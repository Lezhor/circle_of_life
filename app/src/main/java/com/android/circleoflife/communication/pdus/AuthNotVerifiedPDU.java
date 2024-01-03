package com.android.circleoflife.communication.pdus;

import java.io.IOException;
import java.io.InputStream;

/**
 * Send from server to client in case the authentication was not successful
 */
public class AuthNotVerifiedPDU implements PDUWithNoData {

    public final static int ID = 3;

    @Override
    public int getID() {
        return ID;
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
