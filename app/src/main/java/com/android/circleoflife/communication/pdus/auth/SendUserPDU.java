package com.android.circleoflife.communication.pdus.auth;

import com.android.circleoflife.communication.pdus.PDU;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.EntityStringParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Part of the LoginProtocol. Is being sent from server to client if login succeeded
 */
public class SendUserPDU implements PDU {

    public static final int ID = 102;

    private User user;

    public SendUserPDU(User user) {
        this.user = user;
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
        user = EntityStringParser.userFromString(dis.readUTF());
    }

    /**
     * Deserializes pdu from inputstream by calling {@link #deserialize(InputStream)}
     * @param is InputStream
     * @return instance of SendUserPDU
     * @throws IOException if deserializing fails
     */
    public static SendUserPDU fromInputStream(InputStream is) throws IOException {
        SendUserPDU pdu = new SendUserPDU(null);
        pdu.deserialize(is);
        return pdu;
    }

    /**
     * Getter for user
     * @return user
     */
    public User getUser() {
        return user;
    }
}
