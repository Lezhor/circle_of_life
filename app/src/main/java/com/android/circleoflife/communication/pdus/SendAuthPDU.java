package com.android.circleoflife.communication.pdus;

import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.auth.AuthenticationFailedException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This PDU is sent to server to verify the authentication.<br>
 * Its ID is 6 and its data-block consists of a single String containing the auth-String
 *
 * @see Authentication#getAuthenticationString()
 */
public class SendAuthPDU implements PDU {

    public final static int ID = 1;

    private String authString;

    /**
     * Creates a pdu with the authString
     * @param auth needed authentication
     */
    SendAuthPDU(Authentication auth) throws AuthenticationFailedException {
        this.authString = auth.getAuthenticationString();
    }

    /**
     * is private because only {@link SendAuthPDU#fromInputStream(InputStream)} should be able to call this.
     */
    private SendAuthPDU() {
        this.authString = null;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void serialize(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(getID());
        dos.writeUTF(authString);
    }

    @Override
    public void deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        this.authString = dis.readUTF();
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
     * Getter for authString
     * @return returns authString
     */
    public String getAuthString() {
        return authString;
    }
}