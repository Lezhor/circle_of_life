package com.android.circleoflife.database.control;

import com.android.circleoflife.application.App;

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

    private AppDatabase getDatabase() {
        return AppDatabase.getInstance(App.getApplicationContext());
    }

}
