package com.android.circleoflife.communication.pdus.sync;

import com.android.circleoflife.application.App;
import com.android.circleoflife.communication.pdus.PDU;
import com.android.circleoflife.database.models.type_converters.LocalDateTimeConverter;
import com.android.circleoflife.logging.model.DBLog;
import com.android.circleoflife.logging.serializing.LogSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * Used to send logs to server.<br>
 * The datablock contains an integer (how many Logs are there), followed by the serialisation of the logs.
 * @see LogSerializer
 */
public class SendLogsPDU implements PDU {

    public final static int ID = 4;

    private LocalDateTime lastSyncDate;
    private DBLog<?>[] logArray;

    /**
     * Creates a pdu with logs - Note: There is no way to edit the logs later
     */
    public SendLogsPDU(LocalDateTime lastSyncDate, DBLog<?>... logs) {
        this.lastSyncDate = lastSyncDate;
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
        String lastSyncDateString = LocalDateTimeConverter.localDateTimeToString(lastSyncDate);
        if (lastSyncDateString == null) {
            lastSyncDateString = "null";
        }
        dos.writeUTF(lastSyncDateString);
        dos.writeInt(logArray.length);
        LogSerializer logSerializer = App.getLogSerializer();
        for (DBLog<?> log : logArray) {
            logSerializer.serialize(os, log);
        }
    }

    @Override
    public void deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        lastSyncDate = LocalDateTimeConverter.localDateTimeFromString(dis.readUTF());
        int logCount = dis.readInt();
        DBLog<?>[] logs = new DBLog[logCount];
        LogSerializer logSerializer = App.getLogSerializer();
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
        SendLogsPDU pdu = new SendLogsPDU(LocalDateTime.of(0, 1, 1, 0, 0));
        pdu.deserialize(is);
        return pdu;
    }

    /**
     * Getter for the logs
     * @return the log array
     */
    public DBLog<?>[] getLogs() {
        return logArray;
    }

    public LocalDateTime getLastSyncDate() {
        return lastSyncDate;
    }
}
