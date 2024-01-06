package com.android.circleoflife.communication.pdus.sync;

import static org.junit.Assert.*;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.type_converters.LocalDateTimeConverter;
import com.android.circleoflife.logging.model.DBLog;
import com.android.circleoflife.logging.serializing.LogSerializer;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Tests {@link SendLogsPDU}
 */
public class SendLogsPDUTest {
    DBLog<?>[] logArray;

    @Before
    public void setUp() {
        logArray = new DBLog[3];
        // TODO: 03.12.2023 Test SendLogsPDU Setup
        logArray[0] = new DBLog<>(new Category(UUID.randomUUID(), "Test Category", UUID.randomUUID(), null), DBLog.ChangeMode.INSERT);
        logArray[1] = new DBLog<>(new User(UUID.randomUUID(), "john_doe", "this.password1", LocalDateTime.now()), DBLog.ChangeMode.UPDATE);
        logArray[2] = new DBLog<>(new Category(UUID.randomUUID(), "Test Category", UUID.randomUUID(), null), DBLog.ChangeMode.DELETE);
    }

    /**
     * Tests serialize() method by serializing to ByteArrayOutputStream and then checking
     * if the according inputstream.readInt() returns the needed pdu ID
     */
    @Test
    public void testSerialize() {
        System.out.println("Testing SendLogsPDU Serializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            LocalDateTime lastSyncDate = LocalDateTime.now();
            SendLogsPDU pdu = new SendLogsPDU(lastSyncDate, logArray);
            pdu.serialize(os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            DataInputStream dis = new DataInputStream(is);
            assertEquals(pdu.getID(), dis.readInt());
            assertEquals(LocalDateTimeConverter.localDateTimeToString(lastSyncDate), dis.readUTF());
            assertEquals(logArray.length, dis.readInt());

            LogSerializer serializer = App.getLogSerializer();
            for (DBLog<?> actualLog : logArray) {
                assertEquals(actualLog, serializer.deserialize(is));
            }

        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Checks if deserialisation works correctly with a given inputstream.<br>
     * actually a joke-test because the pdu has no datablock...
     */
    @Test
    public void testDeserialize() {
        System.out.println("Testing SendLogsPDU Deserializing");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            LogSerializer serializer = App.getLogSerializer();
            LocalDateTime lastSyncDate = LocalDateTime.now();

            dos.writeInt(SendLogsPDU.ID);
            dos.writeUTF(LocalDateTimeConverter.localDateTimeToString(lastSyncDate));
            dos.writeInt(logArray.length);
            for (DBLog<?> log : logArray) {
                serializer.serialize(bos, log);
            }

            InputStream is = new ByteArrayInputStream(bos.toByteArray());
            DataInputStream dis = new DataInputStream(is);
            assertEquals(SendLogsPDU.ID, dis.readInt());
            SendLogsPDU pdu = SendLogsPDU.fromInputStream(is);
            assertEquals(SendLogsPDU.ID, pdu.getID());
            assertArrayEquals(logArray, pdu.getLogs());
        } catch (IOException e) {
            fail();
        }
    }
}