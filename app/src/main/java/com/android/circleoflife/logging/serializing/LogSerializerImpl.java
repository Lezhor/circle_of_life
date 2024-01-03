package com.android.circleoflife.logging.serializing;

import com.android.circleoflife.communication.pdus.sync.SendLogsPDU;
import com.android.circleoflife.database.models.type_converters.DBLogConverter;
import com.android.circleoflife.logging.model.DBLog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    public void serialize(OutputStream os, DBLog<?> log) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeUTF(DBLogConverter.dbLogToString(log));
    }

    @Override
    public DBLog<?> deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        return DBLogConverter.stringToDBLog(dis.readUTF());
    }

}
