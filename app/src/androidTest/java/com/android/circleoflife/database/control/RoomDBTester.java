package com.android.circleoflife.database.control;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RoomDBTester {

    public static AppDatabase setUpMemoryDB(Context appContext) {
        return Room.inMemoryDatabaseBuilder(appContext.getApplicationContext(), AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }


    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] result = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<>() {
            @Override
            public void onChanged(T t) {
                result[0] = t;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        System.out.println("Waiting.....");
        //noinspection ResultOfMethodCallIgnored
        latch.await(2, TimeUnit.SECONDS);
        System.out.println("Stopped waiting!");
        //noinspection unchecked
        return (T) result[0];
    }


}
