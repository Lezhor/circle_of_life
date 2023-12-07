package com.android.circleoflife.database.models.type_converters;

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
    public LocalDateTime localDateTimeFromString(String str) {
        return LocalDateTime.parse(str);
    }

    /**
     * Converts LocalDateTime to String
     * @param time timestamp
     * @return String representation
     * @see LocalDateTime#toString()
     */
    @TypeConverter
    public String localDateTimeToString(LocalDateTime time) {
        return time.toString();
    }

    /**
     * Converts String to LocalDate
     * @param str String
     * @return converted LocalDate
     * @see LocalDate#parse(CharSequence)
     */
    @TypeConverter
    public LocalDate localDateFromString(String str) {
        return LocalDate.parse(str);
    }

    /**
     * Converts LocalDate to String
     * @param date timestamp
     * @return String representation
     * @see LocalDate#toString()
     */
    @TypeConverter
    public String localDateToString(LocalDate date) {
        return date.toString();
    }

    /**
     * Converts String to LocalTime
     * @param str String
     * @return converted LocalTime
     * @see LocalTime#parse(CharSequence)
     */
    @TypeConverter
    public LocalTime localTimeFromString(String str) {
        return LocalTime.parse(str);
    }

    /**
     * Converts LocalTime to String
     * @param time timestamp
     * @return String representation
     * @see LocalTime#toString()
     */
    @TypeConverter
    public String localDateToString(LocalTime time) {
        return time.toString();
    }

}
