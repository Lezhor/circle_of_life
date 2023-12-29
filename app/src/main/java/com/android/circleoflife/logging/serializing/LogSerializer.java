package com.android.circleoflife.logging.serializing;

import com.android.circleoflife.communication.pdus.SendLogsPDU;
import com.android.circleoflife.logging.model.DBLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * LogSerializer can serialize and deserialize {@link DBLog logs} from and to IOStreams.<br>
 * It is used in the {@link SendLogsPDU SendLogsPDU}.
 */
public interface LogSerializer {

    // TODO: 29.12.2023 Make methods generic

    /**
     * serializes log to OutputStream
     * @param os OutputStream
     * @param log log to be serialized
     * @throws IOException if serializing fails
     */
    void serialize(OutputStream os, DBLog log) throws IOException;

    /**
     * deserializes log from InputStream
     * @param is InputStream
     * @return log to be serialized
     * @throws IOException if deserializing fails
     */
    DBLog deserialize(InputStream is) throws IOException;

    /**
     * Converts a log to its String Representation
     * @param log log
     * @return String representation of log
     */
    String dbLogToString(DBLog log);

    /**
     * Converts a String to a DBLog Object
     * @param str string representation of log
     * @return converted DBLog
     */
    DBLog stringToDBLog(String str);

}
