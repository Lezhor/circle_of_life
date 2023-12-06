package com.android.circleoflife.database.models.type_converters;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;

/**
 * Converts String to LocalDateTime and back. Used in Room Database.<br><br>
 * The most important feature of this converter is: When you plug output of a method in the other method you land where you started. Both ways.<br>
 * Used in {@link com.android.circleoflife.database.control.AppDatabase AppDatabase}
 */
public class LocalDateTimeConverter {

    /**
     * Converts String to LocalDateTime
     * @param str String
     * @return converted LocalDateTime
     * @see LocalDateTime#parse(CharSequence)
     */
    @TypeConverter
    public LocalDateTime dateFromString(String str) {
        return LocalDateTime.parse(str);
    }

    /**
     * Converts LocalDateTime to String
     * @param time timestamp
     * @return String representation
     * @see LocalDateTime#toString()
     */
    @TypeConverter
    public String stringFromDate(LocalDateTime time) {
        return time.toString();
    }

}
