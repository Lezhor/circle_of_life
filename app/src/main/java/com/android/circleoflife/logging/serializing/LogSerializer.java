package com.android.circleoflife.logging.serializing;

import com.android.circleoflife.communication.pdus.SendLogsPDU;
import com.android.circleoflife.logging.model.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * LogSerializer can serialize and deserialize {@link Log logs} from and to IOStreams.<br>
 * It is used in the {@link SendLogsPDU SendLogsPDU}.
 */
public interface LogSerializer {

    /**
     * serializes log to OutputStream
     * @param os OutputStream
     * @param log log to be serialized
     * @throws IOException if serializing fails
     */
    public void serialize(OutputStream os, Log log) throws IOException;

    /**
     * deserializes log from InputStream
     * @param is InputStream
     * @return log to be serialized
     * @throws IOException if deserializing fails
     */
    public Log deserialize(InputStream is) throws IOException;

}
