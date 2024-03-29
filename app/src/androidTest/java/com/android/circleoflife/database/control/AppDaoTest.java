package com.android.circleoflife.database.control;

import static com.android.circleoflife.database.control.RoomDBTester.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.control.daos.UserDao;
import com.android.circleoflife.database.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class AppDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private UserDao userDao;


    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = setUpMemoryDB(context);
        userDao = database.getUserDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testDatabaseAccess() {
        User user = new User(UUID.randomUUID(), "john_lennon", "blub123", LocalDateTime.now(App.SERVER_TIMEZONE));
        userDao.insert(user);
        User retrievedUser = userDao.getUser("john_lennon");
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        assertEquals(user.getTimeOfCreation(), retrievedUser.getTimeOfCreation());
        assertEquals(user, retrievedUser);
    }

    @Test
    public void testLiveDataKeepTrackOfDatabaseChanges() {
        try {
            User user = new User(UUID.randomUUID(), "hello_there", "blab123", LocalDateTime.now(App.SERVER_TIMEZONE));
            userDao.insert(user);
            LiveData<User> liveData = userDao.getUser(user.getId());
            user.setPassword("huhuhu");
            userDao.update(user);

            User retrievedUser = getOrAwaitValue(liveData);
            assertEquals(user, retrievedUser);
            assertEquals(user.getUsername(), retrievedUser.getUsername());
            assertEquals(user.getPassword(), retrievedUser.getPassword());
            assertEquals(user.getTimeOfCreation(), retrievedUser.getTimeOfCreation());
            assertEquals(user.getId(), retrievedUser.getId());


        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }
}