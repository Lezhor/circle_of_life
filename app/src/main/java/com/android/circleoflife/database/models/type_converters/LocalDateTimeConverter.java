package com.android.circleoflife.database.models.type_converters;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    public static LocalDateTime localDateTimeFromString(String str) {
        if (str == null || str.equals("null")) {
            return null;
        } else {
            return LocalDateTime.parse(str);
        }
    }

    /**
     * Converts LocalDateTime to String
     * @param time timestamp
     * @return String representation
     * @see LocalDateTime#toString()
     */
    @Nullable
    @TypeConverter
    public static String localDateTimeToString(@Nullable LocalDateTime time) {
        if (time == null) {
            return null;
        } else {
            return time.toString();
        }
    }

    /**
     * Converts String to LocalDate
     * @param str String
     * @return converted LocalDate
     * @see LocalDate#parse(CharSequence)
     */
    @TypeConverter
    public static LocalDate localDateFromString(String str) {
        if (str == null) {
            return null;
        }
        return LocalDate.parse(str);
    }

    /**
     * Converts LocalDate to String
     * @param date timestamp
     * @return String representation
     * @see LocalDate#toString()
     */
    @TypeConverter
    public static String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.toString();
    }

    /**
     * Converts String to LocalTime
     * @param str String
     * @return converted LocalTime
     * @see LocalTime#parse(CharSequence)
     */
    @TypeConverter
    public static LocalTime localTimeFromString(String str) {
        if (str == null) {
            return null;
        }
        return LocalTime.parse(str);
    }

    /**
     * Converts LocalTime to String
     * @param time timestamp
     * @return String representation
     * @see LocalTime#toString()
     */
    @TypeConverter
    public static String localTimeToString(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.toString();
    }

}
