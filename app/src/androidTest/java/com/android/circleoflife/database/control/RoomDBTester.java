package com.android.circleoflife.database.control;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.android.circleoflife.database.models.*;
import com.android.circleoflife.database.models.additional.CycleFrequency;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RoomDBTester {

    public final User[] users = new User[]{
            new User(UUID.randomUUID(), "sarah_3", "superCoolPassword", LocalDateTime.of(2023, 11, 6, 20, 46, 19, 366)),
            new User(UUID.randomUUID(), "unnamed_user42", "myPassword", LocalDateTime.of(2022, 8, 23, 14, 34, 23, 164)),
            new User(UUID.randomUUID(), "jefffff", "kdjbalsk", LocalDateTime.of(2023, 3, 5, 8, 44, 43, 931)),
            new User(UUID.randomUUID(), "nobody", "hihihihihi", LocalDateTime.of(2023, 1, 1, 10, 10)),
    };

    private Category[] categories;

    public final Category[] getCategories() {
        if (categories == null) {
            List<Category> list = new LinkedList<>();

            list.add(new Category(UUID.randomUUID(), "Uni", users[0].getId(), null));
            list.add(new Category(UUID.randomUUID(), "IT", users[0].getId(), null));
            list.add(new Category(UUID.randomUUID(), "Languages", users[0].getId(), null));
            list.add(new Category(UUID.randomUUID(), "Music", users[0].getId(), null));
            list.add(new Category(UUID.randomUUID(), "Android Project", users[0].getId(), list.get(2).getId()));
            list.add(new Category(UUID.randomUUID(), "日本語", users[0].getId(), list.get(2).getId()));
            list.add(new Category(UUID.randomUUID(), "English", users[0].getId(), list.get(2).getId()));
            list.add(new Category(UUID.randomUUID(), "Piano", users[0].getId(), list.get(3).getId()));
            list.add(new Category(UUID.randomUUID(), "Classic", users[0].getId(), list.get(list.size() - 1).getId()));
            list.add(new Category(UUID.randomUUID(), "Android BackEnd", users[0].getId(), list.get(4).getId()));
            list.add(new Category(UUID.randomUUID(), "Android FrondEnd", users[0].getId(), list.get(4).getId()));
            list.add(new Category(UUID.randomUUID(), "Duolingo", users[0].getId(), list.get(5).getId()));

            list.add(new Category(UUID.randomUUID(), "Education", users[1].getId(), null));
            list.add(new Category(UUID.randomUUID(), "Uni", users[1].getId(), list.get(list.size() - 1).getId()));
            list.add(new Category(UUID.randomUUID(), "Homework", users[1].getId(), list.get(list.size() - 1).getId()));
            list.add(new Category(UUID.randomUUID(), "Sports", users[1].getId(), null));
            list.add(new Category(UUID.randomUUID(), "Volleyball", users[1].getId(), list.get(list.size() - 1).getId()));
            list.add(new Category(UUID.randomUUID(), "Reading", users[1].getId(), list.get(list.size() - 5).getId()));
            list.add(new Category(UUID.randomUUID(), "Math", users[1].getId(), list.get(list.size() - 6).getId()));

            list.add(new Category(UUID.randomUUID(), "Cooking", users[2].getId(), null));
            list.add(new Category(UUID.randomUUID(), "Movie-List", users[2].getId(), null));
            list.add(new Category(UUID.randomUUID(), "アニメ", users[2].getId(), list.get(list.size() - 1).getId()));
            list.add(new Category(UUID.randomUUID(), "Vacation", users[2].getId(), null));
            list.add(new Category(UUID.randomUUID(), "Packing List", users[2].getId(), list.get(list.size() - 1).getId()));

            categories = list.toArray(Category[]::new);
        }
        return categories;
    }

    public Category getCategory(User user, String name) {
        return Arrays.stream(getCategories())
                .filter(c -> c.getUserID().equals(user.getId()))
                .filter(c -> c.getName().equals(name))
                .reduce(null, (a, b) -> b);
    }

    private Cycle[] cycles;

    public final Cycle[] getCycles() {
        if (cycles == null) {
            List<Cycle> list = new LinkedList<>();
            list.add(new Cycle(UUID.randomUUID(), "Swimming", users[1].getId(), getCategory(users[1], "Sports").getId(), 1, CycleFrequency.fromBinaryString("11010100")));
            list.add(new Cycle(UUID.randomUUID(), "New Lesson", users[0].getId(), getCategory(users[0], "Duolingo").getId(), 1, CycleFrequency.fromBinaryString("10101011")));
            list.add(new Cycle(UUID.randomUUID(), "gain XP", users[0].getId(), getCategory(users[0], "Duolingo").getId(), 0, CycleFrequency.fromBinaryString("11111111")));
            list.add(new Cycle(UUID.randomUUID(), "Learn Characters", users[0].getId(), getCategory(users[0], "日本語").getId(), 1, CycleFrequency.fromBinaryString("10000010")));
            list.add(new Cycle(UUID.randomUUID(), "Learn a new Recipe", users[2].getId(), getCategory(users[2], "Cooking").getId(), 1, CycleFrequency.fromBinaryString("10000010")));
            list.add(new Cycle(UUID.randomUUID(), "Read TAOCP", users[1].getId(), getCategory(users[1], "Reading").getId(), 1, CycleFrequency.fromBinaryString("10000010")));
            list.add(new Cycle(UUID.randomUUID(), "Piano", users[0].getId(), getCategory(users[0], "Piano").getId(), 1, CycleFrequency.fromBinaryString("10100011")));
            list.add(new Cycle(UUID.randomUUID(), "Piano", users[2].getId(), getCategory(users[2], "アニメ").getId(), 1, CycleFrequency.fromBinaryString("10001010")));
            list.add(new Cycle(UUID.randomUUID(), "Do Nothing", users[2].getId(), null, -1, CycleFrequency.fromBinaryString("11111111")));
            cycles = list.toArray(Cycle[]::new);
        }
        return cycles;
    }

    public Cycle getCycle(User user, String name) {
        return Arrays.stream(getCycles())
                .filter(c -> c.getUserID().equals(user.getId()))
                .filter(c -> c.getName().equals(name))
                .reduce(null, (a, b) -> b);
    }

    private Todo[] todos;

    public final Todo[] getTodos() {
        if (todos == null) {
            List<Todo> list = new LinkedList<>();
            list.add(new Todo(UUID.randomUUID(), "Tent", users[2].getId(), getCategory(users[2], "Packing List").getId(), 0));
            list.add(new Todo(UUID.randomUUID(), "Learn Integrals", users[1].getId(), getCategory(users[1], "Math").getId(), 1));
            list.add(new Todo(UUID.randomUUID(), "Read Wikipedia Article about Finland", users[1].getId(), getCategory(users[1], "Reading").getId(), 0));
            list.add(new Todo(UUID.randomUUID(), "Learn Chopin Waltz op. 64 C# Minor", users[0].getId(), getCategory(users[0], "Piano").getId(), 0));
            list.add(new Todo(UUID.randomUUID(), "Watch LOTR", users[2].getId(), getCategory(users[2], "Movie-List").getId(), 0));
            list.add(new Todo(UUID.randomUUID(), "Learn JavaScript", users[0].getId(), getCategory(users[0], "IT").getId(), 1));
            list.add(new Todo(UUID.randomUUID(), "Learn Python", users[0].getId(), getCategory(users[0], "IT").getId(), 1));
            list.add(new Todo(UUID.randomUUID(), "Spanish Homework", users[1].getId(), getCategory(users[1], "Homework").getId(), 1));
            todos = list.toArray(Todo[]::new);
        }
        return todos;
    }

    public Todo getTodo(User user, String name) {
        return Arrays.stream(getTodos())
                .filter(c -> c.getUserID().equals(user.getId()))
                .filter(c -> c.getName().equals(name))
                .reduce(null, (a, b) -> b);
    }

    private Accomplishment[] accomplishments;

    public final Accomplishment[] getAccomplishments() {
        if (accomplishments == null) {
            List<Accomplishment> list = new LinkedList<>();
            list.add(new Accomplishment(UUID.randomUUID(), users[0].getId(), getCycle(users[0], "Piano").getId(), null, "Played Piano", "Learned one new page", 1, LocalDate.of(2023, 12, 21), LocalTime.of(14, 15), 45 * 60000));
            list.add(new Accomplishment(UUID.randomUUID(), users[0].getId(), getCycle(users[0], "New Lesson").getId(), null, null, "did 3 lessons", 1, LocalDate.of(2023, 11, 28), LocalTime.of(19, 25), 5 * 60000));
            list.add(new Accomplishment(UUID.randomUUID(), users[0].getId(), null, getTodo(users[0], "Learn JavaScript").getId(), "hello world", "Made short Hello World Lesson", 1, LocalDate.of(2023, 11, 28), LocalTime.of(19, 25), 5 * 60000));
            list.add(new Accomplishment(UUID.randomUUID(), users[0].getId(), getCycle(users[0], "Piano").getId(), null, "Played Piano", "Played everything two times", 1, LocalDate.of(2023, 11, 29), LocalTime.of(18, 5), 10 * 60000));
            list.add(new Accomplishment(UUID.randomUUID(), users[0].getId(), getCycle(users[0], "Piano").getId(), null, "Played Piano", "Played everything once", 1, LocalDate.of(2023, 11, 30), LocalTime.of(17, 40), 5 * 60000));
            list.add(new Accomplishment(UUID.randomUUID(), users[1].getId(), null, getTodo(users[1], "Learn Integrals").getId(), null, "Calculated one integral", 1, LocalDate.of(2023, 10, 31), LocalTime.of(22, 35), 20 * 60000));
            list.add(new Accomplishment(UUID.randomUUID(), users[1].getId(), null, null, "Travel", "To University", 0, LocalDate.of(2023, 11, 12), LocalTime.of(8, 35), 90 * 60000));
            list.add(new Accomplishment(UUID.randomUUID(), users[0].getId(), null, null, "Help", "Helped dad with his stuff", 0, LocalDate.of(2023, 11, 24), LocalTime.of(13, 15), 40 * 60000));
            list.add(new Accomplishment(UUID.randomUUID(), users[2].getId(), getCycle(users[2], "Piano").getId(), null, null, "Learned one song", 0, LocalDate.of(2023, 12, 13), LocalTime.of(19, 50), 10 * 60000));
            accomplishments = list.toArray(Accomplishment[]::new);
        }
        return accomplishments;
    }


    public static AppDatabase setUpMemoryDB(Context appContext) {
        return Room.inMemoryDatabaseBuilder(appContext.getApplicationContext(), AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    private final AppDatabase db;

    public RoomDBTester(Context context) {
        db = setUpMemoryDB(context);
        initDatabase(db);
    }

    public AppDatabase getDb() {
        return db;
    }

    private void initDatabase(AppDatabase db) {
        db.getUserDao().insert(users);
        db.getCategoryDao().insert(getCategories());
        db.getCycleDao().insert(getCycles());
        db.getTodoDao().insert(getTodos());
        db.getAccomplishmentDao().insert(getAccomplishments());
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
