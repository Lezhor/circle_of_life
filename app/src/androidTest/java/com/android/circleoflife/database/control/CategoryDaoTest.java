package com.android.circleoflife.database.control;

import static com.android.circleoflife.database.control.RoomDBTester.*;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.database.control.daos.CategoryDao;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class CategoryDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    RoomDBTester tester;
    private AppDatabase db;
    private CategoryDao dao;

    @Before
    public void setUp() {
        tester = new RoomDBTester(InstrumentationRegistry.getInstrumentation().getTargetContext());
        db = tester.getDb();
        dao = db.getCategoryDao();
    }

    @After
    public void tearDown() {
        if (db != null)
            db.close();
    }

    @Test
    public void testGetCategories() throws InterruptedException {
        User user = tester.users[0];
        LiveData<List<Category>> categoryLiveData = dao.getAllCategories(user);
        List<Category> retrievedCategories = getOrAwaitValue(categoryLiveData);
        for (Category category : tester.getCategories()) {
            if (category.getUserID().equals(user.getId())) {
                assertTrue(retrievedCategories.contains(category));
            } else {
                assertFalse(retrievedCategories.contains(category));
            }
        }
    }

    @Test
    public void getRootCategories() throws InterruptedException {
        User user = tester.users[0];
        LiveData<List<Category>> categoryLiveData = dao.getRootCategories(user);
        List<Category> retrievedCategories = getOrAwaitValue(categoryLiveData);
        assertEquals(Arrays.stream(tester.getCategories()).filter(c -> c.getUserID().equals(user.getId())).filter(c -> c.getParentID() == null).count(), retrievedCategories.size());
        for (Category category : tester.getCategories()) {
            if (category.getUserID().equals(user.getId()) && category.getParentID() == null) {
                assertTrue(retrievedCategories.contains(category));
            } else {
                assertFalse(retrievedCategories.contains(category));
            }
        }
    }

    @Test
    public void getParent() throws InterruptedException {
        User user = tester.users[1];
        Category category = tester.getCategory(user, "Volleyball");
        LiveData<Category> parentLiveData = dao.getParent(category);
        Category parent = getOrAwaitValue(parentLiveData);
        Category actualParent = tester.getCategory(user, "Sports");
        assertEquals(actualParent, parent);
    }

    @Test
    public void getChildCategories() throws InterruptedException {
        User user = tester.users[0];
        Category parent = tester.getCategory(user, "Android Project");
        LiveData<List<Category>> childLiveData = dao.getChildCategories(parent);
        List<Category> children = getOrAwaitValue(childLiveData);
        for (Category category : tester.getCategories()) {
            if (Objects.equals(category.getParentID(), parent.getId())) {
                assertTrue(children.contains(category));
            } else {
                assertFalse(children.contains(category));
            }
        }
    }

    @Test
    public void testGetCycles() throws InterruptedException {
        User user = tester.users[0];
        Category category = tester.getCategory(user, "Duolingo");

        LiveData<List<Cycle>> cycleLiveData = dao.getCycles(category);
        List<Cycle> retrievedCycles = getOrAwaitValue(cycleLiveData);

        for (Cycle cycle : tester.getCycles()) {
            if (cycle.getUserID().equals(user.getId()) && Objects.equals(cycle.getCategoryID(), category.getId())) {
                assertTrue(retrievedCycles.contains(cycle));
            } else {
                assertFalse(retrievedCycles.contains(cycle));
            }
        }
    }

    @Test
    public void testGetTodos() throws InterruptedException {
        User user = tester.users[0];
        Category category = tester.getCategory(user, "IT");

        LiveData<List<Todo>> cycleLiveData = dao.getTodos(category);
        List<Todo> retrievedTodos = getOrAwaitValue(cycleLiveData);

        for (Todo todo : tester.getTodos()) {
            if (todo.getUserID().equals(user.getId()) && Objects.equals(todo.getCategoryID(), category.getId())) {
                assertTrue(retrievedTodos.contains(todo));
            } else {
                assertFalse(retrievedTodos.contains(todo));
            }
        }
    }

}