package com.android.circleoflife.database.models.helper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * Tests if converting back and forth works with no problem
 */
public class ConvertersTest {


    Converters converters;
    @Before
    public void setUp() {
        converters = new Converters();
    }

    @Test
    public void testConvertTimeToStringAndBack() {
        LocalDateTime time = LocalDateTime.of(2020, 3, 20, 13, 23, 48, 458352);
        assertEquals(time, converters.dateFromString(converters.stringFromDate(time)));
    }

    @Test
    public void testConvertStringToTimeAndBack() {
        String time = "2023-12-05T12:26:37.084397600";
        assertEquals(time, converters.stringFromDate(converters.dateFromString(time)));
    }


}