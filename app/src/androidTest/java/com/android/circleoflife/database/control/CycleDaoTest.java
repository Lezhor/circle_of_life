package com.android.circleoflife.database.control;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.database.control.daos.CycleDao;
import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class CycleDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    RoomDBTester tester;
    AppDatabase db;
    CycleDao dao;

    @Before
    public void setUp() {
        tester = new RoomDBTester(InstrumentationRegistry.getInstrumentation().getTargetContext());
        db = tester.getDb();
        dao = db.getCycleDao();
    }

    @After
    public void tearDown() {
        if (db != null)
            db.close();
    }

    @Test
    public void testGetAllCycles() throws InterruptedException {
        User user = tester.users[0];
        LiveData<List<Cycle>> cycleLiveData = dao.getAllCycles(user);
        List<Cycle> retrievedCycles = RoomDBTester.getOrAwaitValue(cycleLiveData);

        for (Cycle cycle : tester.getCycles()) {
            if (cycle.getUserID().equals(user.getId())) {
                assertTrue(retrievedCycles.contains(cycle));
            } else {
                assertFalse(retrievedCycles.contains(cycle));
            }
        }
    }

    @Test
    public void testGetCategory() throws InterruptedException {
        User user = tester.users[1];
        Cycle cycle = tester.getCycle(user, "Read TAOCP");
        assertNotNull(cycle);

        LiveData<Category> categoryLiveData = dao.getCategory(cycle);
        Category retrievedCategory = RoomDBTester.getOrAwaitValue(categoryLiveData);

        Category actualCategory = tester.getCategory(user, "Reading");

        assertEquals(actualCategory, retrievedCategory);
    }

    @Test
    public void testGetCategoryNull() throws InterruptedException {
        User user = tester.users[2];
        Cycle cycle = tester.getCycle(user, "Do Nothing");
        assertNotNull(cycle);

        LiveData<Category> categoryLiveData = dao.getCategory(cycle);
        Category retrievedCategory = RoomDBTester.getOrAwaitValue(categoryLiveData);

        assertNull(retrievedCategory);
    }

    @Test
    public void testGetAccomplishments() throws InterruptedException {
        User user = tester.users[0];
        Cycle cycle = tester.getCycle(user, "Piano");
        assertNotNull(cycle);

        LiveData<List<Accomplishment>> accomplishmentLiveData = dao.getAccomplishments(cycle);
        List<Accomplishment> retrievedAccomplishments = RoomDBTester.getOrAwaitValue(accomplishmentLiveData);

        for (Accomplishment acc : tester.getAccomplishments()) {
            if (acc.getUserID().equals(user.getId()) && Objects.equals(acc.getCycleID(), cycle.getId())) {
                assertTrue(retrievedAccomplishments.contains(acc));
            } else {
                assertFalse(retrievedAccomplishments.contains(acc));
            }
        }
    }

}
