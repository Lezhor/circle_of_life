package com.android.circleoflife.communication.pdus.sync;

import com.android.circleoflife.communication.pdus.PDU;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Used to receive SQL-instructions from server<br>
 * The datablock contains an integer (how many instructions are there), followed by the instructions as strings
 */
public class SendInstructionsPDU implements PDU {

    public final static int ID = 5;

    private String[] instructions;

    /**
     * Creates a pdu with logs - Note: There is no way to edit the logs later
     */
    public SendInstructionsPDU(String... instructions) {
        this.instructions = instructions;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void serialize(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(getID());
        dos.writeInt(instructions.length);
        for (String instruction : instructions) {
            dos.writeUTF(instruction);
        }
    }

    @Override
    public void deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int instructionCount = dis.readInt();
        String[] instructions = new String[instructionCount];
        for (int i = 0; i < instructions.length; i++) {
            instructions[i] = dis.readUTF();
        }
        this.instructions = instructions;
    }

    /**
     * Returns an instance of this class by calling {@link SendInstructionsPDU#deserialize(InputStream)}
     * @param is Inputstream
     * @return deserialized PDU
     */
    public static SendInstructionsPDU fromInputStream(InputStream is) throws IOException {
        SendInstructionsPDU pdu = new SendInstructionsPDU();
        pdu.deserialize(is);
        return pdu;
    }

    /**
     * Getter for the instructions
     * @return the instructions array
     */
    public String[] getInstructions() {
        return instructions;
    }
}
