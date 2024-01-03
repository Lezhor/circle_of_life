package com.android.circleoflife.communication.pdus.sync;

import static org.junit.Assert.*;

import com.android.circleoflife.communication.pdus.PDU;
import com.android.circleoflife.communication.pdus.sync.SyncSuccessfulPDU;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests {@link SyncSuccessfulPDU}
 */
public class SyncSuccessfulPDUTest {

    /**
     * Tests serialize() method by serializing to ByteArrayOutputStream and then checking
     * if the according inputstream.readInt() returns the needed pdu ID
     */
    @Test
    public void testSerialize() {
        System.out.println("Testing SyncSuccessfulPDU Serializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            SyncSuccessfulPDU pdu = new SyncSuccessfulPDU();
            pdu.serialize(os);
            assertEquals(4, os.size());
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
        System.out.println("Testing SyncSuccessfulPDU Deserializing");
        try {
            InputStream is = new ByteArrayInputStream(new byte[0]);
            PDU pdu = SyncSuccessfulPDU.fromInputStream(is);
            assertEquals(SyncSuccessfulPDU.ID, pdu.getID());
        } catch (IOException e) {
            fail();
        }
    }
}