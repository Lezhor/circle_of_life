package com.android.circleoflife.communication.protocols;

import com.android.circleoflife.database.models.User;

public class LoginProtocolEngine implements LoginProtocol {


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
    public User login(String username, String password) {
        // TODO: 02.01.2024 Login protocol method!
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
