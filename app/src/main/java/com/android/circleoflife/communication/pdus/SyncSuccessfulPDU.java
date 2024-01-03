package com.android.circleoflife.communication.pdus;

import java.io.IOException;
import java.io.InputStream;

/**
 * PDU which terminates the synchronisation. has no datablock
 */
public class SyncSuccessfulPDU implements PDUWithNoData {

    public final static int ID = 6;

    @Override
    public int getID() {
        return ID;
    }

    /**
     * Returns an instance of this class by calling {@link SyncSuccessfulPDU#deserialize(InputStream)}
     * @param is Inputstream
     * @return deserialized PDU
     */
    public static SyncSuccessfulPDU fromInputStream(InputStream is) throws IOException {
        SyncSuccessfulPDU pdu = new SyncSuccessfulPDU();
        pdu.deserialize(is);
        return pdu;
    }
}
