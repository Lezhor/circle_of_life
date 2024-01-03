package com.android.circleoflife.communication.pdus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Used for PDUs which have no datablock. offers an implementation for serializing and deserializing the pdu
 */
public interface PDUWithNoData extends PDU {

    @Override
    default void serialize(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(getID());
    }

    @Override
    default void deserialize(InputStream is) throws IOException {
    }
}
