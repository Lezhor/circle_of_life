package com.android.circleoflife.database.models.type_converters;

import androidx.room.TypeConverter;

import com.android.circleoflife.logging.model.DBLog;

/**
 * Converts String to DBLog and back. Used in Room Database.<br><br>
 * The most important feature of this converter is: When you plug output of a method in the other method you land where you started. Both ways.<br>
 * Used in {@link com.android.circleoflife.database.control.AppDatabase AppDatabase}
 */
public class DBLogConverter {
    // TODO: 29.12.2023 Make methods static

    /**
     * Converts DBLog to String representation
     * @param log Log
     * @return String representation
     * @see com.android.circleoflife.logging.serializing.LogSerializer#dbLogToString(DBLog)
     */
    @TypeConverter
    public String dbLogToString(DBLog<?> log) {
        return DBLog.toString(log);
    }

    /**
     * Converts String to log
     * @param str String representation of log
     * @return converted log
     * @see com.android.circleoflife.logging.serializing.LogSerializer#stringToDBLog(String)
     */
    @TypeConverter
    public DBLog<?> stringToDBLog(String str) {
        return DBLog.fromString(str);
    }

}
