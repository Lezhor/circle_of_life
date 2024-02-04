package com.android.circleoflife.communication.protocols;

import android.util.Log;

import com.android.circleoflife.R;
import com.android.circleoflife.application.App;
import com.android.circleoflife.auth.AuthenticationFailedException;
import com.android.circleoflife.communication.models.SimpleSyncResult;
import com.android.circleoflife.communication.models.SyncResult;
import com.android.circleoflife.communication.pdus.*;
import com.android.circleoflife.communication.pdus.sync.AuthNotVerifiedPDU;
import com.android.circleoflife.communication.pdus.sync.AuthVerifiedPDU;
import com.android.circleoflife.communication.pdus.sync.SendAuthPDU;
import com.android.circleoflife.communication.pdus.sync.SendLogsPDU;
import com.android.circleoflife.communication.pdus.sync.SyncSuccessfulPDU;
import com.android.circleoflife.communication.socket_communication.SocketCommunication;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of interface {@link SyncProtocol}.<br>
 * Implements {@link SyncProtocolEngine#sync(User, LocalDateTime, DBLog[])}<br><br>
 * <code>
 * PROTOCOL_NAME = "COL_SyncProt";<br>
 * VERSION = "v1.0";<br><br>
 * </code>
 * Follows the singleton pattern.
 *
 * @see SyncProtocolEngine#sync(User, LocalDateTime, DBLog[])
 * @see App#getSyncProtocol()
 * @see SyncProtocol
 */
public class SyncProtocolEngine implements SyncProtocol {
    private static final String TAG = SyncProtocolEngine.class.getSimpleName();

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

    /**
     * Private Constructor (for singleton principle)
     */
    private SyncProtocolEngine() {
    }

    public static String PROTOCOL_NAME = "COL_SyncProt";
    public static String VERSION = "v1.0";

    @Override
    public SyncResult sync(User user, LocalDateTime lastSyncDate, DBLog<?>[] logs) {
        Log.d(TAG, "Begin syncing...");
        SocketCommunication com = App.openCommunicationSessionWithServer();
        LocalDateTime newLastSyncDate = lastSyncDate;
        boolean syncSuccessful = true;
        List<DBLog<?>> outLogs = new LinkedList<>();
        IOException exception = null;
        try {
            com.connectToServer();
        } catch (IOException e) {
            Log.i(TAG, "Connection to Server failed!");
            syncSuccessful = false;
            exception = new IOException(App.getResources().getString(R.string.exception_server_connection_failed));
            com.disconnectFromServer();
        }
        if (syncSuccessful) {
            try {
                ProtocolSerializer serializer = new ProtocolSerializer(this, com);

                // Step 1:
                SendAuthPDU authPDU = new SendAuthPDU(user);
                Log.d(TAG, "1) Sending AuthPDU...");
                serializer.serialize(authPDU);

                // Step 2:
                PDU pdu2 = serializer.deserialize();
                Log.d(TAG, "2) Received PDU with ID " + pdu2.getID());
                if (pdu2.getID() == AuthNotVerifiedPDU.ID) {
                    throw new AuthenticationFailedException("Server did not confirm authentication: '" + authPDU.getUser() + "'");
                } else if (pdu2.getID() != AuthVerifiedPDU.ID) {
                    throw new IOException("Wrong PDU received: PDU" + pdu2.getID());
                }

                // Step 3:
                SendLogsPDU sendLogsPDU = new SendLogsPDU(lastSyncDate, logs);
                Log.d(TAG, "3) Sending Logs to Server...");
                serializer.serialize(sendLogsPDU);

                // Step 4:
                SendLogsPDU instructionsPDU = serializer.deserialize(SendLogsPDU.class);
                DBLog<?>[] instructions = instructionsPDU.getLogs();
                newLastSyncDate = instructionsPDU.getLastSyncDate();
                Log.d(TAG, "4) Received SendLogsPDU with " + instructions.length + " instructions.");
                Collections.addAll(outLogs, instructions);

                // Step 5:
                SyncSuccessfulPDU syncSuccessfulPDU = new SyncSuccessfulPDU();
                Log.d(TAG, "5) Sending SyncSuccessfulPDU");
                serializer.serialize(syncSuccessfulPDU);

            } catch (AuthenticationFailedException e) {
                Log.i(TAG, "Synchronisation failed: " + e.getMessage());
                syncSuccessful = false;
                exception = new IOException(App.getResources().getString(R.string.exception_server_authentication_failed));
            } catch (IOException e) {
                Log.i(TAG, "Synchronisation failed: " + e.getMessage());
                syncSuccessful = false;
                exception = new IOException(App.getResources().getString(R.string.exception_server_connection_failed));
            } finally {
                com.disconnectFromServer();
            }
        }
        return new SimpleSyncResult(user, syncSuccessful, newLastSyncDate, outLogs, exception);
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
