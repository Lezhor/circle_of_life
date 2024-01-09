package com.android.circleoflife.communication.protocols;

import android.util.Log;

import com.android.circleoflife.application.App;
import com.android.circleoflife.communication.pdus.auth.LoginFailedPDU;
import com.android.circleoflife.communication.pdus.auth.SendLoginAuthDataPDU;
import com.android.circleoflife.communication.pdus.PDU;
import com.android.circleoflife.communication.pdus.auth.SendUserPDU;
import com.android.circleoflife.communication.socket_communication.SocketCommunication;
import com.android.circleoflife.database.models.User;

import java.io.IOException;

/**
 * Engine for {@link LoginProtocol}. Follows the Singleton-Pattern.
 */
public class LoginProtocolEngine implements LoginProtocol {
    private static final String TAG = LoginProtocolEngine.class.getSimpleName();


    private static volatile LoginProtocolEngine instance;

    /**
     * Returns the only existing instance of this class
     *
     * @return instance of this class
     */
    public static LoginProtocol getInstance() {
        if (instance == null) {
            synchronized (SyncProtocolEngine.class) {
                if (instance == null)
                    instance = new LoginProtocolEngine();
            }
        }
        return instance;
    }

    /**
     * Private constructor for setting singleton
     */
    private LoginProtocolEngine() {
    }


    public static final String PROTOCOL_NAME = "COL_LoginProt";
    public static final String VERSION = "v1.0";

    @Override
    public User login(String username, String password) throws IOException {
        Log.d(TAG, "Begin login request...");
        SocketCommunication com = App.openCommunicationSessionWithServer();
        try {
            com.connectToServer();
            Log.d(TAG, "Connection to server succeeded!");
        } catch (IOException e) {
            throw new IOException("Connecting to Server failed");
        }
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
            } else if (pdu instanceof LoginFailedPDU) {
                Log.i(TAG, "login failed!");
            }

        } catch (IOException e) {
            Log.w(TAG, "Communication failed", e);
            throw e;
        } finally {
            com.disconnectFromServer();
        }
        return null;
    }

    @Override
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
