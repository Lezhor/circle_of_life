package com.android.circleoflife.application;

import android.app.Application;
import android.util.Log;

/**
 * Application object which extends {@link Application}.<br>
 * This class follows te singleton pattern: It has a {@link ApplicationObject#getInstance()} method
 * which makes sure there is only one instance at all times. <br>
 * If the method is called before the instance is set in {@link ApplicationObject#onCreate()},
 * the thread waits. Setting the instance notifies all waiting Threads.
 */
public class ApplicationObject extends Application {
    /**
     * Tag used in Logcat
     */
    public static final String TAG = "ApplicationObject";

    private static volatile ApplicationObject instance;

    /**
     * Returns instance of the ApplicationObject. If not set yet, waits.
     * @return instance of this class
     */
    static ApplicationObject getInstance() {
        if (instance == null) {
            synchronized (ApplicationObject.class) {
                if (instance == null) {
                    try {
                        Log.d(TAG, "getInstance: Start waiting - Instance not set yet");
                        ApplicationObject.class.wait();
                    } catch (InterruptedException e) {
                        Log.w(TAG, "getInstance: Got interrupted while waiting for instance to be set", e);
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Sets the instance to an instance of ApplicationObject. Since its private it can only be called from this class.<br>
     * Called in {@link ApplicationObject#onCreate()}<br>
     * If instance variable is set already, does not change it
     * @param object new instance of ApplicationObject
     */
    private static void setInstance(ApplicationObject object) {
        if (instance == null) {
            synchronized (ApplicationObject.class) {
                if (instance == null) {
                    instance = object;
                    Log.d(TAG, "setInstance: Instance set!");
                    ApplicationObject.class.notifyAll();
                }
            }
        } else {
            Log.w(TAG, "setInstance: Instance already set!");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }
}
