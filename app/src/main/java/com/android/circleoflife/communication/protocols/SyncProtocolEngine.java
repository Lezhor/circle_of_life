package com.android.circleoflife.communication.protocols;

import android.util.Log;

import com.android.circleoflife.Application;
import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.auth.AuthenticationFailedException;
import com.android.circleoflife.communication.pdus.*;
import com.android.circleoflife.communication.socket_communication.SocketCommunication;
import com.android.circleoflife.database.control.DatabaseController;
import com.android.circleoflife.logging.model.DBLog;

import java.io.IOException;
import java.util.Date;

/**
 * Implementation of interface {@link SyncProtocol}.<br>
 * Implements {@link SyncProtocolEngine#sync(Authentication, DBLog[], DatabaseController)} and {@link SyncProtocolEngine#getLastSuccessfulSyncDate()}<br><br>
 * <code>
 * PROTOCOL_NAME = "COL_SyncProt";<br>
 * VERSION = "v1.0";<br><br>
 * </code>
 * Follows the singleton pattern.
 *
 * @see SyncProtocolEngine#sync(Authentication, DBLog[], DatabaseController)
 * @see Application#getSyncProtocol()
 * @see SyncProtocol
 */
public class SyncProtocolEngine implements SyncProtocol {

    private static volatile SyncProtocol instance;

    /**
     * Returns the only existing instance of this class
     *
     * @return instance of this class
     */
    public static SyncProtocol getInstance() {
        if (instance == null) {
            synchronized (SyncProtocolEngine.class) {
                if (instance == null)
                    instance = new SyncProtocolEngine();
            }
        }
        return instance;
    }

    private Date lastSyncDate;

    /**
     * Private Constructor (for singleton principle)
     */
    private SyncProtocolEngine() {
    }

    public static String PROTOCOL_NAME = "COL_SyncProt";
    public static String VERSION = "v1.0";

    @Override
    public boolean sync(Authentication auth, DBLog[] logs, DatabaseController dbController) {
        boolean successful = true;
        Log.d("SyncProtocolEngine", "Begin syncing...");
        SocketCommunication com = Application.openCommunicationSessionWithServer();
        try {
            com.connectToServer();
        } catch (IOException e) {
            Log.i("SyncProtocolEngine", "Connection to Server failed!");
            return false;
        }
        try {
            ProtocolSerializer serializer = new ProtocolSerializer(this, com);

            // Step 1:
            SendAuthPDU authPDU = new SendAuthPDU(auth);
            Log.d("SyncProtocolEngine", "1) Sending AuthPDU...");
            serializer.serialize(authPDU);

            // Step 2:
            PDU pdu2 = serializer.deserialize();
            Log.d("SyncProtocolEngine", "2) Received PDU with ID " + pdu2.getID());
            if (pdu2.getID() == AuthNotVerifiedPDU.ID) {
                throw new AuthenticationFailedException("Server did not confirm authentication: '" + authPDU.getAuthString() + "'");
            } else if (pdu2.getID() != AuthVerifiedPDU.ID) {
                throw new IOException("Wrong PDU received: PDU" + pdu2.getID());
            }

            // Step 3:
            SendLogsPDU sendLogsPDU = new SendLogsPDU(logs);
            Log.d("SyncProtocolEngine", "3) Sending Logs to Server...");
            serializer.serialize(sendLogsPDU);

            // Step 4:
            SendInstructionsPDU instructionsPDU = serializer.deserialize(SendInstructionsPDU.class);
            String[] instructions = instructionsPDU.getInstructions();
            Log.d("SyncProtocolEngine", "4) Received InstructionsPDU with " + instructions.length + " instructions.");
            dbController.executeSQLQueries(instructions);

            // Step 5:
            SyncSuccessfulPDU syncSuccessfulPDU = new SyncSuccessfulPDU();
            Log.d("SyncProtocolEngine", "5) Sending SyncSuccessfulPDU");
            serializer.serialize(syncSuccessfulPDU);

        } catch (NullPointerException | AuthenticationFailedException | IOException e) {
            Log.i("SyncProtocolEngine", "Synchronisation failed: " + e.getMessage());
            successful = false;
        } finally {
            com.disconnectFromServer();
        }

        if (successful) {
            lastSyncDate = new Date();
        }
        return successful;
    }

    @Override
    public Date getLastSuccessfulSyncDate() {
        return lastSyncDate;
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
