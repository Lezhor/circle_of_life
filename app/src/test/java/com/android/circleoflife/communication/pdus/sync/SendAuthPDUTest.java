package com.android.circleoflife.communication.pdus.sync;

import static org.junit.Assert.*;

import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.EntityStringParser;

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
 * Tests {@link SendAuthPDU}. Mocks {@link Authentication} for this purpose.
 */
public class SendAuthPDUTest {

    User user;


    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), "john_doe", "a.Password123", LocalDateTime.of(2023, 12, 20, 20, 20));
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
            SendAuthPDU pdu = new SendAuthPDU(user);
            assertEquals(user, pdu.getUser());
            assertTrue(user.equalsAllParams(pdu.getUser()));
            pdu.serialize(os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            DataInputStream dis = new DataInputStream(is);
            assertEquals(pdu.getID(), dis.readInt());
            User deserialized = EntityStringParser.userFromString(dis.readUTF());
            assertEquals(user, deserialized);
            assertTrue(user.equalsAllParams(deserialized));
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
            dos.writeUTF(EntityStringParser.userToString(user));
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            SendAuthPDU pdu = SendAuthPDU.fromInputStream(is);
            assertEquals(SendAuthPDU.ID, pdu.getID());
            assertEquals(user, pdu.getUser());
            assertTrue(user.equalsAllParams(pdu.getUser()));
        } catch (IOException e) {
            fail();
        }
    }
}