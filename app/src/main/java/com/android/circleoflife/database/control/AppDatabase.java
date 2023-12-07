package com.android.circleoflife.database.control;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.android.circleoflife.database.models.*;
import com.android.circleoflife.database.models.type_converters.*;

@Database(
        entities = {
                User.class,
                Category.class,
                Cycle.class,
                Todo.class,
                Accomplishment.class,
                LogModel.class
        },
        version = 1
)
@TypeConverters({LocalDateTimeConverter.class, DBLogConverter.class, CycleFrequencyConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao getDao();
}
