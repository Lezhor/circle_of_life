package com.android.circleoflife.database.control;

public class DatabaseControllerImpl implements DatabaseController {

    private static volatile DatabaseController instance;

    public static DatabaseController getInstance() {
        if (instance == null) {
            synchronized (DatabaseControllerImpl.class) {
                if (instance == null) {
                    instance = new DatabaseControllerImpl();
                }
            }
        }
        return instance;
    }

}
