package com.android.circleoflife.communication.pdus;

import static org.junit.Assert.*;

import com.android.circleoflife.communication.pdus.AuthVerifiedPDU;
import com.android.circleoflife.communication.pdus.PDU;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests {@link AuthVerifiedPDU}
 */
public class AuthVerifiedPDUTest {

    /**
     * Tests serialize() method by serializing to ByteArrayOutputStream and then checking
     * if the according inputstream.readInt() returns the needed pdu ID
     */
    @Test
    public void testSerialize() {
        System.out.println("Testing AuthVerifiedPDU Serializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            AuthVerifiedPDU pdu = new AuthVerifiedPDU();
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
        System.out.println("Testing AuthVerifiedPDU Deserializing");
        try {
            InputStream is = new ByteArrayInputStream(new byte[0]);
            PDU pdu = AuthVerifiedPDU.fromInputStream(is);
            assertEquals(AuthVerifiedPDU.ID, pdu.getID());
        } catch (IOException e) {
            fail();
        }
    }
}