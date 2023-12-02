package com.android.circleoflife.logging.serializing;

import com.android.circleoflife.logging.model.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * LogSerializer can serialize and deserialize {@link Log logs} from and to IOStreams.<br>
 * It is used in the {@link com.android.circleoflife.client_communication.SendLogsPDU SendLogsPDU}.
 */
public class LogSerializer {

    private static LogSerializer instance;

    public static LogSerializer getInstance() {
        if (instance == null) {
            instance = new LogSerializer();
        }
        return instance;
    }

    private LogSerializer() {}

    /**
     * serializes log to OutputStream
     * @param os OutputStream
     * @param log log to be serialized
     * @throws IOException if serializing fails
     */
    public void serialize(OutputStream os, Log log) throws IOException {
        // TODO: 02.12.2023 serialize logs
    }

    /**
     * deserializes log from InputStream
     * @param is InputStream
     * @return log to be serialized
     * @throws IOException if deserializing fails
     */
    public Log deserialize(InputStream is) throws IOException {
        // TODO: 02.12.2023 deserialize logs
        return new Log("Log|max_mustermann|" + System.currentTimeMillis());
    }

}
