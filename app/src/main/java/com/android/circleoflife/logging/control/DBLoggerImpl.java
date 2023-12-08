package com.android.circleoflife.logging.control;

/**
 * Class which implements DBLogger. Follows the singleton principle
 */
public class DBLoggerImpl implements DBLogger {

    private static volatile DBLogger instance;

    /**
     * Singleton oriented getInstance() method.
     * @return only existing instance of this class
     */
    public static DBLogger getInstance() {
        if (instance == null) {
            synchronized (DBLoggerImpl.class) {
                if (instance == null) {
                    instance = new DBLoggerImpl();
                }
            }
        }
        return instance;
    }
}
