package com.android.circleoflife.client_communication;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests {@link SendInstructionsPDU}
 */
public class SendInstructionsPDUTest {

    private String[] instructions;

    @Before
    public void setUp() {
        this.instructions = new String[]{
                "SELECT * FROM table1 WHERE age > 10",
                "DELETE FROM table1 WHERE ID = 22",
                "DELETE FROM table1 WHERE ID = 25",
                "SELECT * FROM table1 WHERE age < 2",
        };
    }

    /**
     * Tests serialize() method by serializing to ByteArrayOutputStream and then checking
     * if the according inputstream.readInt() returns the needed pdu ID
     */
    @Test
    public void testSerialize() {
        System.out.println("Testing SendInstructionsPDU Serializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            SendInstructionsPDU pdu = new SendInstructionsPDU(instructions);
            pdu.serialize(os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            DataInputStream dis = new DataInputStream(is);
            assertEquals(pdu.getID(), dis.readInt());
            assertEquals(instructions.length, dis.readInt());
            for (String instr : instructions) {
                assertEquals(instr, dis.readUTF());
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
        System.out.println("Testing SendInstructionsPDU Deserializing");
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            new SendInstructionsPDU(instructions).serialize(os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            DataInputStream dis = new DataInputStream(is);
            assertEquals(SendInstructionsPDU.ID, dis.readInt());
            SendInstructionsPDU pdu = SendInstructionsPDU.fromInputStream(is);
            assertEquals(SendInstructionsPDU.ID, pdu.getID());
            assertArrayEquals(instructions, pdu.getInstructions());
        } catch (IOException e) {
            fail();
        }
    }
}