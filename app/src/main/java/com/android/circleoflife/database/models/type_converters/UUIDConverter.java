package com.android.circleoflife.database.models.type_converters;

import androidx.room.TypeConverter;

import java.util.UUID;

/**
 * Converts UUID to string and back. Used as typeconverter in {@link com.android.circleoflife.database.control.AppDatabase}
 */
public class UUIDConverter {

    /**
     * Converts UUID to String. null maps to null
     * @param uuid uuid
     * @return String representation of uuid
     */
    @TypeConverter
    public static String uuidToString(UUID uuid) {

        return uuid == null ? null : uuid.toString();
    }

    /**
     * Converts String to UUID. null maps to null
     * @param str string representation of uuid
     * @return uuid
     */
    @TypeConverter
    public static UUID uuidFromString(String str) {
        return str == null ? null : UUID.fromString(str);
    }

}
