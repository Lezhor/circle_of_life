package com.android.circleoflife.client_communication;

import static org.junit.Assert.*;

import com.android.circleoflife.auth.Authentication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests {@link SendAuthPDU}. Mocks {@link Authentication} for this purpose.
 */
public class SendAuthPDUTest {

    Authentication auth;

    /**
     * Mocks the {@link Authentication} so that {@link Authentication#getAuthenticationString()}
     * returns <code>auth[johnny_depp123|24257]</code>
     */
    @Before
    public void setUp() {
        auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getAuthenticationString()).thenReturn("auth[johnny_depp123|24257]");
        //System.out.println("Mocked Authentication.getAuthenticationString() to return '" + auth.getAuthenticationString() + "'");
    }

    /**
     * Tests serialize() method by serializing to ByteArrayOutputStream and then checking
     * if the according inputstream.readInt() returns the needed pdu ID
     */
    @Test
    public void testSerialize() {
        System.out.println("Testing SendAuthPDUTest Serializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            SendAuthPDU pdu = new SendAuthPDU(auth);
            assertEquals(auth.getAuthenticationString(), pdu.getAuthString());
            pdu.serialize(os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            DataInputStream dis = new DataInputStream(is);
            assertEquals(pdu.getID(), dis.readInt());
            assertEquals(pdu.getAuthString(), dis.readUTF());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Checks if deserialisation works correctly with a given inputstream.
     */
    @Test
    public void testDeserialize() {
        System.out.println("Testing SendAuthPDUTest Deserializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(auth.getAuthenticationString());
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            SendAuthPDU pdu = SendAuthPDU.fromInputStream(is);
            assertEquals(SendAuthPDU.ID, pdu.getID());
            assertEquals(auth.getAuthenticationString(), pdu.getAuthString());
        } catch (IOException e) {
            fail();
        }
    }
}