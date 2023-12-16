package com.android.circleoflife.database.control;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.control.daos.AccomplishmentDao;
import com.android.circleoflife.database.control.daos.CategoryDao;
import com.android.circleoflife.database.control.daos.CycleDao;
import com.android.circleoflife.database.control.daos.LogDao;
import com.android.circleoflife.database.control.daos.TodoDao;
import com.android.circleoflife.database.control.daos.UserDao;
import com.android.circleoflife.database.models.*;
import com.android.circleoflife.database.models.type_converters.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AppDatabase. offers the getDao() method which can perform actions on the database.
 */
@Database(
        entities = {
                User.class,
                Category.class,
                Cycle.class,
                Todo.class,
                Accomplishment.class,
                LogEntity.class
        },
        version = 2
)
@TypeConverters({LocalDateTimeConverter.class, DBLogConverter.class, CycleFrequencyConverter.class, UUIDConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";

    abstract UserDao getUserDao();

    abstract CategoryDao getCategoryDao();

    abstract CycleDao getCycleDao();

    abstract TodoDao getTodoDao();

    abstract AccomplishmentDao getAccomplishmentDao();

    abstract LogDao getLogDao();

    /**
     * Name of the Database
     */
    public final static String DATABASE_NAME = "CircleOfLifeDB";

    private static volatile AppDatabase instance;

    /**
     * Singleton getInstance() method.
     * @return only existing instance of AppDatabase
     */
    static AppDatabase getInstance(Context applicationContext) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    applicationContext.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            .addCallback(defaultInit)
                            //.allowMainThreadQueries() // Stops checking for Main Thread.
                            //.fallbackToDestructiveMigration() // Creates new database when migration fails
                            .build();
                }
            }
        }
        return instance;
    }

    private static final RoomDatabase.Callback defaultInit = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            User user = new User(UUID.randomUUID(), "john_doe", "super_cool_password", LocalDateTime.now());
            Category category = new Category(UUID.randomUUID(), "Sample Category", user.getId(), null);
            Log.d(TAG, "Callback: inserting sample user: " + user);
            UserDao userDao = getInstance(App.getApplicationContext()).getUserDao();
            CategoryDao categoryDao = getInstance(App.getApplicationContext()).getCategoryDao();
            ExecutorService service = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });
            service.execute(() -> {
                userDao.insert(user);
                categoryDao.insert(category);
            });
        }
    };

}
