package com.android.circleoflife.communication.pdus.sync;

import com.android.circleoflife.communication.pdus.PDUWithNoData;

import java.io.IOException;
import java.io.InputStream;

/**
 * PDU the server sends to client if authentication succeeded. has no data block
 */
public class AuthVerifiedPDU implements PDUWithNoData {

    public final static int ID = 2;

    @Override
    public int getID() {
        return ID;
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
