package com.android.circleoflife.database.control;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.android.circleoflife.database.models.*;
import com.android.circleoflife.database.models.type_converters.LocalDateTimeConverter;

@Database(
        entities = {
                User.class,
                Category.class,
                Cycle.class,
                Todo.class,
                Accomplishment.class,
                LogModel.class
        },
        version = 1,
        exportSchema = true
)
@TypeConverters({LocalDateTimeConverter.class}) // TODO: 06.12.2023 Add Log Converter
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao getDao();
}
