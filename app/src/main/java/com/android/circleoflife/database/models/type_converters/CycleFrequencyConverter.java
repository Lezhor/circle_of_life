package com.android.circleoflife.database.models.type_converters;

import androidx.room.TypeConverter;

import com.android.circleoflife.database.models.additional.CycleFrequency;

/**
 * Converts String to {@link CycleFrequency} and back. Used in Room Database.<br><br>
 * The most important feature of this converter is: When you plug output of a method in the other method you land where you started. Both ways.<br>
 * Used in {@link com.android.circleoflife.database.control.AppDatabase AppDatabase}
 */
public class CycleFrequencyConverter {

    /**
     * Converts CycleFrequency to its binary representation
     * @param frequency frequency
     * @return binary representation
     * @see CycleFrequency#toBinaryString()
     */
    @TypeConverter
    public static String frequecyToString(CycleFrequency frequency) {
        return frequency.toBinaryString();
    }

    /**
     * Converts given binary string to frequency
     * @param binaryString binary string
     * @return CycleFrequency
     * @see CycleFrequency#fromBinaryString(String)
     */
    @TypeConverter
    public static CycleFrequency stringToFrequency(String binaryString) {
        return CycleFrequency.fromBinaryString(binaryString);
    }
}
