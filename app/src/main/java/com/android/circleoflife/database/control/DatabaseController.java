package com.android.circleoflife.database.control;

import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.logging.model.DBLog;
import com.android.circleoflife.communication.protocols.SyncProtocol;

// TODO: 03.12.2023 DATABASE CONTROLLER
public interface DatabaseController {


    /**
     * Used in {@link SyncProtocol#sync(Authentication, DBLog[], DatabaseController)}
     * @param instructions instructions to be executed
     */
    void executeSQLQueries(String[] instructions);


}
