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
public class LogSerializerImpl implements LogSerializer {

    private static LogSerializer instance;

    /**
     * Follows the singleton pattern:<br>
     * Returns the only existing instance of this class
     * @return instance of this class
     */
    public static LogSerializer getInstance() {
        if (instance == null) {
            instance = new LogSerializerImpl();
        }
        return instance;
    }

    private LogSerializerImpl() {}

    @Override
    public void serialize(OutputStream os, Log log) throws IOException {
        // TODO: 02.12.2023 serialize logs
    }

    @Override
    public Log deserialize(InputStream is) throws IOException {
        // TODO: 02.12.2023 deserialize logs
        return new Log("Log|max_mustermann|" + System.currentTimeMillis());
    }

}
