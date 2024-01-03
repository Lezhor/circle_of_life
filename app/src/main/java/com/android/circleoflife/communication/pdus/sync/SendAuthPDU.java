package com.android.circleoflife.communication.pdus.sync;

import com.android.circleoflife.communication.pdus.PDU;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.EntityStringParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This PDU is sent to server to verify the authentication.<br>
 * Its ID is 1 and its data-block consists of a single String containing the serialized user
 */
public class SendAuthPDU implements PDU {

    public final static int ID = 1;

    private User user;

    public SendAuthPDU(User user) {
        this.user = user;
    }

    /**
     * is private because only {@link SendAuthPDU#fromInputStream(InputStream)} should be able to call this.
     */
    private SendAuthPDU() {
        this.user = null;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void serialize(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(getID());
        dos.writeUTF(EntityStringParser.userToString(user));
    }

    @Override
    public void deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        this.user = EntityStringParser.userFromString(dis.readUTF());
    }

    /**
     * Returns an instance of this class by calling {@link SendAuthPDU#deserialize(InputStream)}
     * @param is Inputstream
     * @return deserialized PDU
     */
    public static SendAuthPDU fromInputStream(InputStream is) throws IOException {
        SendAuthPDU pdu = new SendAuthPDU();
        pdu.deserialize(is);
        return pdu;
    }

    /**
     * getter for user
     * @return user
     */
    public User getUser() {
        return user;
    }
}