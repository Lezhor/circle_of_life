package com.android.circleoflife.logging.serializing;

import com.android.circleoflife.communication.pdus.sync.SendLogsPDU;
import com.android.circleoflife.logging.model.DBLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * LogSerializer can serialize and deserialize {@link DBLog logs} from and to IOStreams.<br>
 * It is used in the {@link SendLogsPDU SendLogsPDU}.
 */
public interface LogSerializer {

    /**
     * serializes log to OutputStream
     * @param os OutputStream
     * @param log log to be serialized
     * @throws IOException if serializing fails
     */
    void serialize(OutputStream os, DBLog<?> log) throws IOException;

    /**
     * deserializes log from InputStream
     * @param is InputStream
     * @return log to be serialized
     * @throws IOException if deserializing fails
     */
    DBLog<?> deserialize(InputStream is) throws IOException;

}
