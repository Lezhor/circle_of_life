package com.android.circleoflife.communication.pdus;

import com.android.circleoflife.Application;
import com.android.circleoflife.logging.model.DBLog;
import com.android.circleoflife.logging.serializing.LogSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Used to send logs to server.<br>
 * The datablock contains an integer (how many Logs are there), followed by the serialisation of the logs.
 * @see LogSerializer
 */
public class SendLogsPDU implements PDU {

    public final static int ID = 4;

    private DBLog[] logArray;

    /**
     * Creates a pdu with logs - Note: There is no way to edit the logs later
     */
    public SendLogsPDU(DBLog... logs) {
        this.logArray = logs;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void serialize(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(getID());
        dos.writeInt(logArray.length);
        LogSerializer logSerializer = Application.getLogSerializer();
        for (DBLog log : logArray) {
            logSerializer.serialize(os, log);
        }
    }

    @Override
    public void deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int logCount = dis.readInt();
        DBLog[] logs = new DBLog[logCount];
        LogSerializer logSerializer = Application.getLogSerializer();
        for (int i = 0; i < logs.length; i++) {
            logs[i] = logSerializer.deserialize(is);
        }
        this.logArray = logs;
    }

    /**
     * Returns an instance of this class by calling {@link SendLogsPDU#deserialize(InputStream)}
     * @param is Inputstream
     * @return deserialized PDU
     */
    public static SendLogsPDU fromInputStream(InputStream is) throws IOException {
        SendLogsPDU pdu = new SendLogsPDU();
        pdu.deserialize(is);
        return pdu;
    }

    /**
     * Getter for the logs
     * @return the log array
     */
    public DBLog[] getLogs() {
        return logArray;
    }
}
