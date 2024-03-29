package com.android.circleoflife.database.control;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.android.circleoflife.database.control.daos.AccomplishmentDao;
import com.android.circleoflife.database.control.daos.BaseDao;
import com.android.circleoflife.database.control.daos.CategoryDao;
import com.android.circleoflife.database.control.daos.CycleDao;
import com.android.circleoflife.database.control.daos.LogDao;
import com.android.circleoflife.database.control.daos.TodoDao;
import com.android.circleoflife.database.control.daos.UserDao;
import com.android.circleoflife.database.models.*;
import com.android.circleoflife.database.models.additional.HasUserId;
import com.android.circleoflife.database.models.type_converters.*;

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
                            //.allowMainThreadQueries() // Stops checking for Main Thread.
                            //.fallbackToDestructiveMigration() // Creates new database when migration fails
                            .build();
                }
            }
        }
        return instance;
    }


    /** @noinspection unchecked*/
    <E extends HasUserId> BaseDao<E> getDao(Class<E> entityClass) {
        if (entityClass == null) {
            return null;
        } else if (entityClass.equals(User.class)) {
            return (BaseDao<E>) getUserDao();
        } else if (entityClass.equals(Category.class)) {
            return (BaseDao<E>) getCategoryDao();
        } else if (entityClass.equals(Cycle.class)) {
            return (BaseDao<E>) getCycleDao();
        } else if (entityClass.equals(Todo.class)) {
            return (BaseDao<E>) getTodoDao();
        } else if (entityClass.equals(Accomplishment.class)) {
            return (BaseDao<E>) getAccomplishmentDao();
        } else {
            return null;
        }
    }

}
