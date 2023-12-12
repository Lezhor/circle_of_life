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
import com.android.circleoflife.database.models.additional.CycleFrequency;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class CategoryDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private CategoryDao dao;
    private Category[] categories;
    private User user;
    private final Category wrongCategory = new Category("NOT A CATEGORY", 1010101, null);

    @Before
    public void setUp() {
        database = setUpMemoryDB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        dao = database.getCategoryDao();
        user = new User("lizzy_3", "blub123", LocalDateTime.now());
        user.setId(42);
        database.getUserDao().insert(user);
        categories = new Category[]{
                new Category("Uni", user.getId(), null),
                new Category("IT", user.getId(), null),
                new Category("Languages", user.getId(), null),
                new Category("Music", user.getId(), null),
                new Category("Android Project", user.getId(), "IT"),
                new Category("日本語", user.getId(), "Languages"),
                new Category("English", user.getId(), "Languages"),
                new Category("Piano", user.getId(), "Music"),
                new Category("Classic", user.getId(), "Piano"),
                new Category("Android BackEnd", user.getId(), "Android Project"),
                new Category("Android FrondEnd", user.getId(), "Android Project"),
                new Category("Duolingo", user.getId(), "日本語"),
        };
        dao.insert(categories);
        User temp = new User("unnamed_user", "no_password", LocalDateTime.of(2000, 1, 1, 0, 0, 0));
        temp.setId(wrongCategory.getUserID());
        database.getUserDao().insert(temp);
        dao.insert(wrongCategory);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testGetCategories() throws InterruptedException {
        LiveData<List<Category>> categoryLiveData = dao.getAllCategories(user);
        List<Category> retrievedCategories = getOrAwaitValue(categoryLiveData);
        for (Category category : categories) {
            assertTrue(retrievedCategories.contains(category));
        }
    }

    @Test
    public void testGetCategoriesWithMultipleUsers() throws InterruptedException {
        User secondUser = new User("mario_3", "password", LocalDateTime.now());
        secondUser.setId(100);
        database.getUserDao().insert(secondUser);
        Category[] categories2 = new Category[]{
                new Category("Temp1", secondUser.getId(), null),
                new Category("Temp2", secondUser.getId(), null),
                new Category("Temp3", secondUser.getId(), "Temp2")
        };
        dao.insert(categories2);

        LiveData<List<Category>> categoryLiveData1 = dao.getAllCategories(user);
        List<Category> retrievedCategories1 = getOrAwaitValue(categoryLiveData1);

        LiveData<List<Category>> categoryLiveData2 = dao.getAllCategories(secondUser);
        List<Category> retrievedCategories2 = getOrAwaitValue(categoryLiveData2);

        for (Category category : categories) {
            assertTrue(retrievedCategories1.contains(category));
            assertFalse(retrievedCategories2.contains(category));
        }
        for (Category category : categories2) {
            assertFalse(retrievedCategories1.contains(category));
            assertTrue(retrievedCategories2.contains(category));
        }
        assertFalse(retrievedCategories1.contains(wrongCategory));
        assertFalse(retrievedCategories2.contains(wrongCategory));
    }

    @Test
    public void getRootCategories() throws InterruptedException {
        LiveData<List<Category>> categoryLiveData = dao.getRootCategories(user);
        List<Category> retrievedCategories = getOrAwaitValue(categoryLiveData);
        assertEquals(Arrays.stream(categories).filter(c -> c.getParent() == null).count(), retrievedCategories.size());
        for (Category category : categories) {
            if (category.getParent() == null) {
                assertTrue(retrievedCategories.contains(category));
            } else {
                assertFalse(retrievedCategories.contains(category));
            }
        }
        assertFalse(retrievedCategories.contains(wrongCategory));
    }

    @Test
    public void getParent() throws InterruptedException {
        LiveData<Category> parentLiveData = dao.getParent(categories[11]);
        Category parent = getOrAwaitValue(parentLiveData);
        assertEquals(categories[5], parent);
    }

    @Test
    public void getChildCategories() throws InterruptedException {
        Category parent = categories[4];
        LiveData<List<Category>> childLiveData = dao.getChildCategories(parent);
        List<Category> children = getOrAwaitValue(childLiveData);
        assertArrayEquals(
                Arrays.stream(categories).filter(c -> c.getParent() != null && c.getParent().equals(parent.getName())).toArray(Category[]::new),
                children.toArray(Category[]::new)
        );
    }

    @Test
    public void testGetCycles() throws InterruptedException {
        Category current = categories[7];
        Cycle[] cycles = new Cycle[]{
                new Cycle("Valbonesi", user.getId(), current.getName(), 1, CycleFrequency.fromBinaryString("11010110")),
                new Cycle("Chopin", user.getId(), current.getName(), 1, CycleFrequency.fromBinaryString("10001001")),
                new Cycle("Own Music", user.getId(), current.getParent(), 1, CycleFrequency.fromBinaryString("10100000")),
        };
        database.getCycleDao().insert(cycles);

        LiveData<List<Cycle>> cycleLiveData = dao.getCycles(current);
        List<Cycle> retrievedCycles = getOrAwaitValue(cycleLiveData);

        for (Cycle cycle : cycles) {
            if (Objects.equals(cycle.getCategory(), current.getName())) {
                assertTrue(retrievedCycles.contains(cycle));
            } else {
                assertFalse(retrievedCycles.contains(cycle));
            }
        }
    }

    @Test
    public void testGetTodos() throws InterruptedException {
        Category current = categories[0];
        Todo[] todos = new Todo[]{
                new Todo("Study for Exam", user.getId(), current.getName(), 1, LocalDateTime.of(2023, 3, 5, 23, 59)),
                new Todo("Do nothing", user.getId(), null, -1, true, LocalDateTime.now()),
                new Todo("Do Math Homework", user.getId(), current.getName(), 1),
        };
        database.getTodoDao().insert(todos);

        LiveData<List<Todo>> todosLiveData = dao.getTodos(current);
        List<Todo> retrievedTodos = getOrAwaitValue(todosLiveData);

        for (Todo todo : todos) {
            if (Objects.equals(todo.getCategory(), current.getName())) {
                assertTrue(retrievedTodos.contains(todo));
            } else {
                assertFalse(retrievedTodos.contains(todo));
            }
        }
    }

}