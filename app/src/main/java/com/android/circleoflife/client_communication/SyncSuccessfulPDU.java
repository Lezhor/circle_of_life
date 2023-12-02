package com.android.circleoflife.client_communication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * PDU which terminates the synchronisation. has no datablock
 */
class SyncSuccessfulPDU implements PDU {

    final static int ID = 6;

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
    public static SyncSuccessfulPDU fromInputStream(InputStream is) throws IOException {
        SyncSuccessfulPDU pdu = new SyncSuccessfulPDU();
        pdu.deserialize(is);
        return pdu;
    }
}
