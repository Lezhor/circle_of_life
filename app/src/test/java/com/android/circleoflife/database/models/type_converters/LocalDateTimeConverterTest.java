package com.android.circleoflife.database.models.type_converters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests if converting back and forth works with no problem
 */
public class LocalDateTimeConverterTest {

    @Test
    public void testConvertTimeToStringAndBack() {
        LocalDateTime time = LocalDateTime.of(2020, 3, 20, 13, 23, 48, 458352);
        assertEquals(time, LocalDateTimeConverter.localDateTimeFromString(LocalDateTimeConverter.localDateTimeToString(time)));
    }

    @Test
    public void testConvertStringToTimeAndBack() {
        String time = "2023-12-05T12:26:37.084397600";
        assertEquals(time, LocalDateTimeConverter.localDateTimeToString(LocalDateTimeConverter.localDateTimeFromString(time)));
    }

    @Test
    public void testIfStringsAreSortable() {
        List<LocalDateTime> timestamps = new LinkedList<>();
        timestamps.add(LocalDateTime.of(2023, 3, 23, 23, 32, 48, 4253));
        timestamps.add(LocalDateTime.of(2022, 12, 23, 12, 37, 15, 3204));
        timestamps.add(LocalDateTime.of(2023, 1, 3, 3, 57, 26, 16423));
        timestamps.add(LocalDateTime.of(2019, 4, 6, 13, 36, 40, 23423));
        timestamps.add(LocalDateTime.of(2019, 3, 10, 13, 24, 3, 62423));
        timestamps.add(LocalDateTime.of(2022, 10, 18, 20, 19, 23, 923));
        timestamps.add(LocalDateTime.of(2023, 10, 23, 3, 25, 42, 12690));
        timestamps.add(LocalDateTime.of(2023, 11, 3, 10, 51, 23, 8015));

        List<LocalDateTime> converted = timestamps.stream()
                .map(LocalDateTimeConverter::localDateTimeToString)
                .sorted()
                .map(LocalDateTimeConverter::localDateTimeFromString)
                .toList();

        timestamps.sort(LocalDateTime::compareTo);

        for (int i = 0; i < timestamps.size(); i++) {
            assertEquals(timestamps.get(i), converted.get(i));
        }
    }


}