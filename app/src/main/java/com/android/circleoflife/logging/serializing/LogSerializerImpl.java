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
public class LogSerializerImpl implements LogSerializer {

    private static volatile LogSerializer instance;

    /**
     * Follows the singleton pattern:<br>
     * Returns the only existing instance of this class
     *
     * @return instance of this class
     */
    public static LogSerializer getInstance() {
        if (instance == null) {
            synchronized (LogSerializerImpl.class) {
                if (instance == null)
                    instance = new LogSerializerImpl();
            }
        }
        return instance;
    }

    private LogSerializerImpl() {
    }

    @Override
    public void serialize(OutputStream os, DBLog log) throws IOException {
        // TODO: 02.12.2023 serialize logs
    }

    @Override
    public DBLog deserialize(InputStream is) throws IOException {
        // TODO: 02.12.2023 deserialize logs
        return null;
    }

    @Override
    public String dbLogToString(DBLog log) {
        // TODO: 06.12.2023 Implement LogToString (maybe by calling DBLog.toString())
        return null;
    }

    @Override
    public DBLog stringToDBLog(String str) {
        // TODO: 06.12.2023 Implement StringToLog (maybe by calling a method in DBLog)
        return null;
    }

}
