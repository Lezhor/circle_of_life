package com.android.circleoflife.database.control;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.database.control.daos.AccomplishmentDao;
import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class AccomplishmentDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    RoomDBTester tester;
    AppDatabase db;
    AccomplishmentDao dao;

    @Before
    public void setUp() {
        tester = new RoomDBTester(InstrumentationRegistry.getInstrumentation().getTargetContext());
        db = tester.getDb();
        dao = db.getAccomplishmentDao();
    }

    @After
    public void tearDown() {
        if (db != null)
            db.close();
    }

    @Test
    public void testGetAllAccomplishments() throws InterruptedException {
        User user = tester.users[0];
        LiveData<List<Accomplishment>> cycleLiveData = dao.getAllAccomplishments(user);
        List<Accomplishment> retrievedCycles = RoomDBTester.getOrAwaitValue(cycleLiveData);

        for (Accomplishment accomplishment : tester.getAccomplishments()) {
            if (accomplishment.getUserID().equals(user.getId())) {
                assertTrue(retrievedCycles.contains(accomplishment));
            } else {
                assertFalse(retrievedCycles.contains(accomplishment));
            }
        }
    }

    @Test
    public void testGetAccomplishmentsFromCategory() throws InterruptedException {
        User user = tester.users[0];
        Category category = tester.getCategory(user, "Piano");
        assertNotNull(category);

        LiveData<List<Accomplishment>> categoryLiveDataLiveData = dao.getAllAccomplishments(category);
        List<Accomplishment> retrievedAccomplishments = RoomDBTester.getOrAwaitValue(categoryLiveDataLiveData);

        List<UUID> cycles = Arrays.stream(tester.getCycles()).filter(c -> category.getId().equals(c.getCategoryID())).map(Cycle::getId).toList();
        List<UUID> todos = Arrays.stream(tester.getTodos()).filter(t -> category.getId().equals(t.getCategoryID())).map(Todo::getId).toList();

        for (Accomplishment acc : tester.getAccomplishments()) {
            if (acc.getUserID().equals(user.getId()) && (acc.getCycleID() != null && cycles.contains(acc.getCycleID()) || acc.getTodoID() != null && todos.contains(acc.getTodoID()))) {
                assertTrue(retrievedAccomplishments.contains(acc));
            } else {
                assertFalse(retrievedAccomplishments.contains(acc));
            }
        }
    }

    @Test
    public void testGetAccomplishmentsAfterAndBeforeTimestamp() throws InterruptedException {
        User user = tester.users[0];

        LocalDateTime timestamp = LocalDateTime.of(2023, 11, 30, 18, 0);

        LiveData<List<Accomplishment>> liveDataBefore = dao.getAllAccomplishmentsBeforeTimestamp(user, timestamp);
        List<Accomplishment> accBefore = RoomDBTester.getOrAwaitValue(liveDataBefore);

        LiveData<List<Accomplishment>> liveDataAfter = dao.getAllAccomplishmentsAfterTimestamp(user, timestamp);
        List<Accomplishment> accAfter = RoomDBTester.getOrAwaitValue(liveDataAfter);

        for (Accomplishment acc : tester.getAccomplishments()) {
            if (acc.getUserID().equals(user.getId())) {
                if (acc.getLocalDateTime().equals(timestamp) || acc.getLocalDateTime().isAfter(timestamp)) {
                    assertFalse(accBefore.contains(acc));
                    assertTrue(accAfter.contains(acc));
                } else {
                    assertTrue(accBefore.contains(acc));
                    assertFalse(accAfter.contains(acc));
                }
            } else {
                assertFalse(accBefore.contains(acc));
                assertFalse(accAfter.contains(acc));
            }
        }
    }

    @Test
    public void testGetAccomplishmentsBetweenTimestamps() throws InterruptedException {
        User user = tester.users[0];

        LocalDateTime timestamp1 = LocalDateTime.of(2023, 11, 30, 0, 0);
        LocalDateTime timestamp2 = timestamp1.plusDays(1);

        LiveData<List<Accomplishment>> liveDataBetween = dao.getAllAccomplishmentsBetweenTimestamps(user, timestamp1, timestamp2);
        List<Accomplishment> accBetween = RoomDBTester.getOrAwaitValue(liveDataBetween);

        for (Accomplishment acc : tester.getAccomplishments()) {
            if (acc.getUserID().equals(user.getId()) && (acc.getLocalDateTime().equals(timestamp1) || acc.getLocalDateTime().isAfter(timestamp1)) && acc.getLocalDateTime().isBefore(timestamp2)) {
                assertTrue(accBetween.contains(acc));
            } else {
                assertFalse(accBetween.contains(acc));
            }
        }
    }
}
