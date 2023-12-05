package com.android.circleoflife.database.control;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.android.circleoflife.database.models.*;
import com.android.circleoflife.database.models.helper.Converters;

@Database(entities = {User.class}, version = 1)
@TypeConverters({Converters.class}) // TODO: 06.12.2023 Add Log Converter
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao getDao();
}
