package com.android.circleoflife.database.control;

import static com.android.circleoflife.database.control.RoomDBTester.*;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.circleoflife.database.control.daos.CategoryDao;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CategoryDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private CategoryDao dao;
    private Category[] categories;
    private User user;


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
                new Category("Android FrondEnd", user.getId(), "Android Project"),
                new Category("Android BackEnd", user.getId(), "Android Project"),
        };
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertAndGetCategories() throws InterruptedException {
        dao.insert(categories);
        LiveData<List<Category>> categoryLiveData = dao.getAllCategories(user);
        List<Category> retrievedCategories = getOrAwaitValue(categoryLiveData);
        for (Category category : categories) {
            assertTrue(retrievedCategories.contains(category));
        }
    }

    @Test
    public void getRootCategories() {
    }

    @Test
    public void getParent() {
    }

    @Test
    public void getChildCategories() {
    }
}