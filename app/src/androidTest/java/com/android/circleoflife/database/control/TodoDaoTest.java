package com.android.circleoflife.database.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.database.control.daos.TodoDao;
import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Todo;
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
public class TodoDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    RoomDBTester tester;
    AppDatabase db;
    TodoDao dao;

    @Before
    public void setUp() {
        tester = new RoomDBTester(InstrumentationRegistry.getInstrumentation().getTargetContext());
        db = tester.getDb();
        dao = db.getTodoDao();
    }

    @After
    public void tearDown() {
        if (db != null)
            db.close();
    }

    @Test
    public void testGetAllTodos() throws InterruptedException {
        User user = tester.users[0];
        LiveData<List<Todo>> cycleLiveData = dao.getAllTodos(user);
        List<Todo> retrievedTodos = RoomDBTester.getOrAwaitValue(cycleLiveData);

        for (Todo todo : tester.getTodos()) {
            if (todo.getUserID().equals(user.getId())) {
                assertTrue(retrievedTodos.contains(todo));
            } else {
                assertFalse(retrievedTodos.contains(todo));
            }
        }
    }

    @Test
    public void testGetCategory() throws InterruptedException {
        User user = tester.users[1];
        Todo todo = tester.getTodo(user, "Spanish Homework");
        assertNotNull(todo);

        LiveData<Category> categoryLiveData = dao.getCategory(todo);
        Category retrievedCategory = RoomDBTester.getOrAwaitValue(categoryLiveData);

        Category actualCategory = tester.getCategory(user, "Homework");

        assertEquals(actualCategory, retrievedCategory);
    }

    @Test
    public void testGetCategoryNull() throws InterruptedException {
        User user = tester.users[1];
        Todo todo = tester.getTodo(user, "Shopping");
        assertNotNull(todo);

        LiveData<Category> categoryLiveData = dao.getCategory(todo);
        Category retrievedCategory = RoomDBTester.getOrAwaitValue(categoryLiveData);

        assertNull(retrievedCategory);
    }

    @Test
    public void testGetAccomplishments() throws InterruptedException {
        User user = tester.users[1];
        Todo todo = tester.getTodo(user, "Learn Integrals");
        assertNotNull(todo);

        LiveData<List<Accomplishment>> accomplishmentLiveData = dao.getAccomplishments(todo);
        List<Accomplishment> retrievedAccomplishments = RoomDBTester.getOrAwaitValue(accomplishmentLiveData);

        for (Accomplishment acc : tester.getAccomplishments()) {
            if (acc.getUserID().equals(user.getId()) && Objects.equals(acc.getTodoID(), todo.getId())) {
                assertTrue(retrievedAccomplishments.contains(acc));
            } else {
                assertFalse(retrievedAccomplishments.contains(acc));
            }
        }
    }

}
