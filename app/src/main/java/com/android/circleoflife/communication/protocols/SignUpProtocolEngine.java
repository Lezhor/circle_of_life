package com.android.circleoflife.communication.protocols;

import android.util.Log;

import com.android.circleoflife.application.App;
import com.android.circleoflife.communication.pdus.PDU;
import com.android.circleoflife.communication.pdus.auth.SendLoginAuthDataPDU;
import com.android.circleoflife.communication.pdus.auth.SendUserPDU;
import com.android.circleoflife.communication.pdus.auth.SignUpFailedPDU;
import com.android.circleoflife.communication.socket_communication.SocketCommunication;
import com.android.circleoflife.database.models.User;

import java.io.IOException;

/**
 * Engine for {@link SignUpProtocol}. Follows singleton pattern.
 */
public class SignUpProtocolEngine implements SignUpProtocol {

    private static final String TAG = SignUpProtocolEngine.class.getSimpleName();

    private static volatile SignUpProtocolEngine instance;

    /**
     * Returns the only existing instance of this class
     *
     * @return instance of this class
     */
    public static SignUpProtocol getInstance() {
        if (instance == null) {
            synchronized (SyncProtocolEngine.class) {
                if (instance == null)
                    instance = new SignUpProtocolEngine();
            }
        }
        return instance;
    }

    /**
     * Private constructor for setting singleton
     */
    private SignUpProtocolEngine() {
    }


    public static final String PROTOCOL_NAME = "COL_SignUpProt";
    public static final String VERSION = "v1.0";


    @Override
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public User signUp(String username, String password) throws IOException {
        Log.d(TAG, "Begin signUp request...");
        SocketCommunication com = App.openCommunicationSessionWithServer();
        try {
            Log.d(TAG, "Connecting to Server");
            com.connectToServer();
        } catch (IOException e) {
            Log.w(TAG, "Connection to server failed", e);
            throw new IOException("Connection to Server failed");
        }
        Log.d(TAG, "Connection Successful");
        try {
            ProtocolSerializer serializer = new ProtocolSerializer(this, com);

            // Step 1:
            SendLoginAuthDataPDU sendLoginAuthDataPDU = new SendLoginAuthDataPDU(username, password);
            Log.d(TAG, "1) Sending SendLoginAuthDataPDU");
            serializer.serialize(sendLoginAuthDataPDU);

            // Step 2:
            PDU pdu = serializer.deserialize();
            Log.d(TAG, "2) received PDU with ID: " + pdu.getID());
            if (pdu instanceof SendUserPDU sendUserPDU) {
                Log.d(TAG, "Returning user: " + sendUserPDU.getUser());
                return sendUserPDU.getUser();
            } else if (pdu instanceof SignUpFailedPDU) {
                Log.i(TAG, "signUp failed!");
            }

        } catch (IOException e) {
            Log.w(TAG, "Communication failed", e);
            throw e;
        } finally {
            com.disconnectFromServer();
        }
        return null;
    }
}
