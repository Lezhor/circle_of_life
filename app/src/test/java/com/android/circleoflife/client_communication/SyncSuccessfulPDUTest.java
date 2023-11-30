package com.android.circleoflife.client_communication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SyncSuccessfulPDUTest {

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

    @Test
    public void testDeserialize() {
        System.out.println("Testing SyncSuccessfulPDU Deserializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(SyncSuccessfulPDU.ID);
            assertEquals(4, os.size());
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            PDU pdu = SyncSuccessfulPDU.fromInputStream(is);
            assertEquals(SyncSuccessfulPDU.ID, pdu.getID());
        } catch (IOException e) {
            fail();
        }
    }
}