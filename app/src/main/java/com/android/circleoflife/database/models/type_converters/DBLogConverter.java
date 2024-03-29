package com.android.circleoflife.database.models.type_converters;

import androidx.room.TypeConverter;

import com.android.circleoflife.logging.model.DBLog;

/**
 * Converts String to DBLog and back. Used in Room Database.<br><br>
 * The most important feature of this converter is: When you plug output of a method in the other method you land where you started. Both ways.<br>
 * Used in {@link com.android.circleoflife.database.control.AppDatabase AppDatabase}
 */
public class DBLogConverter {

    /**
     * Converts DBLog to String representation
     * @param log Log
     * @return String representation
     */
    @TypeConverter
    public static String dbLogToString(DBLog<?> log) {
        return DBLog.toString(log);
    }

    /**
     * Converts String to log
     * @param str String representation of log
     * @return converted log
     */
    @TypeConverter
    public static DBLog<?> stringToDBLog(String str) {
        return DBLog.fromString(str);
    }

}
