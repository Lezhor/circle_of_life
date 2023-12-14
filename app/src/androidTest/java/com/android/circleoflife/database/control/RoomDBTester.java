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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RoomDBTester {

    public final User[] users = new User[]{
            new User("sarah_3", "superCoolPassword", LocalDateTime.of(2023, 11, 6, 20, 46, 19, 366)),
            new User("unnamed_user42", "myPassword", LocalDateTime.of(2022, 8, 23, 14, 34, 23, 164)),
            new User("jefffff", "kdjbalsk", LocalDateTime.of(2023, 3, 5, 8, 44, 43, 931)),
            new User("nobody", "hihihihihi", LocalDateTime.of(2023, 1, 1, 10, 10)),
    };

    public final int[] userIDs = new int[]{
            1, 42, 21, 7
    };

    public final Category[] categories = new Category[]{
            new Category("Uni", userIDs[0], null),
            new Category("IT", userIDs[0], null),
            new Category("Languages", userIDs[0], null),
            new Category("Music", userIDs[0], null),
            new Category("Android Project", userIDs[0], "IT"),
            new Category("日本語", userIDs[0], "Languages"),
            new Category("English", userIDs[0], "Languages"),
            new Category("Piano", userIDs[0], "Music"),
            new Category("Classic", userIDs[0], "Piano"),
            new Category("Android BackEnd", userIDs[0], "Android Project"),
            new Category("Android FrondEnd", userIDs[0], "Android Project"),
            new Category("Duolingo", userIDs[0], "日本語"),

            new Category("Homework", userIDs[1], "Uni"),
            new Category("Uni", userIDs[1], "Education"),
            new Category("Sports", userIDs[1], null),
            new Category("Volleyball", userIDs[1], "Sports"),
            new Category("Education", userIDs[1], null),
            new Category("Reading", userIDs[1], "Education"),
            new Category("Math", userIDs[1], "Education"),

            new Category("Cooking", userIDs[2], null),
            new Category("アニメ", userIDs[2], "Movie-List"),
            new Category("Movie-List", userIDs[2], null),
            new Category("Vacation", userIDs[2], null),
            new Category("Packing List", userIDs[2], "Vacation"),
    };

    public final Cycle[] cycles = new Cycle[]{
            new Cycle("Swimming", userIDs[1], "Sports", 1, CycleFrequency.fromBinaryString("11010100")),
            new Cycle("New Lesson", userIDs[0], "Duolingo", 1, CycleFrequency.fromBinaryString("10101011")),
            new Cycle("gain XP", userIDs[0], "Duolingo", 0, CycleFrequency.fromBinaryString("11111111")),
            new Cycle("Learn Characters", userIDs[0], "日本語", 1, CycleFrequency.fromBinaryString("10000010")),
            new Cycle("Learn a new Recipe", userIDs[2], "Cooking", 1, CycleFrequency.fromBinaryString("10000010")),
            new Cycle("Read TAOCP", userIDs[1], "Reading", 1, CycleFrequency.fromBinaryString("10000010")),
            new Cycle("Piano", userIDs[0], "Piano", 1, CycleFrequency.fromBinaryString("10100011")),
    };

    public final Todo[] todos = new Todo[]{
            new Todo("Tent", userIDs[2], "Packing List", 0),
            new Todo("Learn Integrals", userIDs[1], "Math", 1),
            new Todo("Read Wikipedia Article about Finland", userIDs[2], "Reading", 0),
            new Todo("Learn Chopin Waltz op. 64 C# Minor", userIDs[0], "Piano", 0),
            new Todo("Watch LOTR", userIDs[2], "Movie-List", 0),
            new Todo("Learn JavaScript", userIDs[0], "IT", 1),
            new Todo("Learn Python", userIDs[0], "IT", 1),
            new Todo("Spanish Homework", userIDs[1], "Homework", 1),
    };

    public final Accomplishment[] accomplishments = new Accomplishment[]{
            new Accomplishment(1, userIDs[0], "Piano", null, "Played Piano", "Learned one new page", 1, LocalDate.of(2023, 12, 21), LocalTime.of(14, 15), 45 * 60000),
            new Accomplishment(2, userIDs[0], "New Lesson", null, null, "did 3 lessons", 1, LocalDate.of(2023, 11, 28), LocalTime.of(19, 25), 5 * 60000),
            new Accomplishment(3, userIDs[0], null, "Learn JavaScript", "hello world", "Made short Hello World Lesson", 1, LocalDate.of(2023, 11, 28), LocalTime.of(19, 25), 5 * 60000),
            new Accomplishment(4, userIDs[0], "Piano", null, "Played Piano", "Played everything two times", 1, LocalDate.of(2023, 11, 29), LocalTime.of(18, 5), 10 * 60000),
            new Accomplishment(5, userIDs[0], "Piano", null, "Played Piano", "Played everything once", 1, LocalDate.of(2023, 11, 30), LocalTime.of(17, 40), 5 * 60000),
            new Accomplishment(1, userIDs[1], null, "Learn Integrals", null, "Calculated one integral", 1, LocalDate.of(2023, 10, 31), LocalTime.of(22, 35), 20 * 60000),
            new Accomplishment(2, userIDs[1], null, null, "Travel", "To University", 0, LocalDate.of(2023, 11, 12), LocalTime.of(8, 35), 90 * 60000),
    };


    public static AppDatabase setUpMemoryDB(Context appContext) {
        return Room.inMemoryDatabaseBuilder(appContext.getApplicationContext(), AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    public RoomDBTester(AppDatabase db) {
        initDatabase(db);
    }

    private void initDatabase(AppDatabase db) {
        for (int i = 0; i < users.length; i++) {
            users[i].setId(userIDs[i]);
        }
        db.getUserDao().insert(users);
        db.getCategoryDao().insert(categories);
        db.getCycleDao().insert(cycles);
        db.getTodoDao().insert(todos);
        db.getAccomplishmentDao().insert(accomplishments);
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
