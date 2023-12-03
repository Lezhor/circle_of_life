package com.android.circleoflife.communication.pdus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The interface for the PDUs.<br><br>
 * Each of the PDUs implements this PDU
 */
public interface PDU {

    /**
     * Every PDU has a unique ID which is an integer from 1 to 7
     * @return PDU-ID
     */
    int getID();

    /**
     * Serializes current PDU<br>
     * It does NOT serialize the protocol name and version, only PDU-ID and Data
     * @param os Outputstream
     */
    void serialize(OutputStream os) throws IOException;

    /**
     * Deserializes current PDU<br>
     * It does NOT deserialize the ID, the protocol name and version, only the Data<br>
     * Sets attributes of <code>this</code> object to the deserialized ones
     * @param is Inputstream
     */
    void deserialize(InputStream is) throws IOException;

}
