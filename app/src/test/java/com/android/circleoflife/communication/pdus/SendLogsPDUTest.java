package com.android.circleoflife.communication.pdus;

import static org.junit.Assert.*;

import com.android.circleoflife.communication.pdus.PDU;
import com.android.circleoflife.communication.pdus.SendLogsPDU;
import com.android.circleoflife.logging.model.Log;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests {@link SendLogsPDU}
 */
public class SendLogsPDUTest {
    Log[] logArray;

    @Before
    public void setUp() {
        logArray = new Log[3];
        // TODO: 03.12.2023 Test SendLogsPDU Setup
    }

    /**
     * Tests serialize() method by serializing to ByteArrayOutputStream and then checking
     * if the according inputstream.readInt() returns the needed pdu ID
     */
    @Test
    public void testSerialize() {
        // TODO: 03.12.2023 Test SendLogsPDU serialize
        System.out.println("Testing SendLogsPDU Serializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PDU pdu = new SendLogsPDU(logArray);
            pdu.serialize(os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            DataInputStream dis = new DataInputStream(is);
            assertEquals(pdu.getID(), dis.readInt());
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
        // TODO: 03.12.2023 Test SendLogsPDU deserialize
        System.out.println("Testing SendLogsPDU Deserializing");
        try {
            InputStream is = new ByteArrayInputStream(new byte[0]);
            PDU pdu = SendLogsPDU.fromInputStream(is);
            assertEquals(SendLogsPDU.ID, pdu.getID());
        } catch (IOException e) {
            fail();
        }
    }
}