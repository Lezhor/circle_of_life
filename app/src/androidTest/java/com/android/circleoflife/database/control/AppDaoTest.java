package com.android.circleoflife.database.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.database.control.daos.UserDao;
import com.android.circleoflife.database.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class AppDaoTest {

    @Rule public TestRule rule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private UserDao userDao;

    private static final String dbDebug = "DB: ";

    @Before
    public void setUp() {
        System.out.println(dbDebug + "Setting up database in memory.....");
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = database.getUserDao();
        System.out.println(dbDebug + "Database Setup finished!");
    }

    @After
    public void tearDown() {
        System.out.println(dbDebug + "Closing Memory Database");
        database.close();
    }

    private static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
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

    @Test
    public void testDatabaseAccess() {
        try {

            User user = new User("john_lennon", "blub123", LocalDateTime.now());
            userDao.insert(user);
            LiveData<User> liveData = userDao.getUser("john_lennon");
            User retrievedUser = getOrAwaitValue(liveData);
            assertEquals(user.getUsername(), retrievedUser.getUsername());
            assertEquals(user.getPassword(), retrievedUser.getPassword());
            assertEquals(user.getTimeOfCreation(), retrievedUser.getTimeOfCreation());
            assertEquals(user.getId(), retrievedUser.getId());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLiveDataKeepTrackOfDatabaseChanges() {
        try {
            User user = new User("hello_there", "blab123", LocalDateTime.now());
            userDao.insert(user);
            LiveData<User> liveData = userDao.getUser("hello_there");
            user.setPassword("huhuhu");
            userDao.update(user);

            User retrievedUser = getOrAwaitValue(liveData);
            assertEquals(user.getUsername(), retrievedUser.getUsername());
            assertEquals(user.getPassword(), retrievedUser.getPassword());
            assertEquals(user.getTimeOfCreation(), retrievedUser.getTimeOfCreation());
            assertEquals(user.getId(), retrievedUser.getId());


        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }
}