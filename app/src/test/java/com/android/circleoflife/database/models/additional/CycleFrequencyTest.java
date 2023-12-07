package com.android.circleoflife.database.models.additional;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CycleFrequencyTest {
    // TODO: 07.12.2023 Test CycleFrequency

    CycleFrequency frequency;

    @Before
    public void setUp() {
        frequency = new CycleFrequency(CycleFrequency.MASK_MONDAY, CycleFrequency.MASK_WEDNESDAY, CycleFrequency.MASK_FRIDAY);
    }

    @Test
    public void testCountDaysAWeek() {
        assertEquals(3, CycleFrequency.countDaysAWeek(frequency));
    }

    @Test
    public void testToBinaryConversion() {
        String binary = frequency.toBinaryString();
        assertEquals("10010101", binary);
        CycleFrequency converted = CycleFrequency.fromBinaryString(binary);
        assertEquals(frequency, converted);
    }

    @Test
    public void testToStringShouldTakeTooLong() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> future = service.submit(() -> frequency.toString());
        try {
           future.get(200, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException e) {
            fail();
        } catch (TimeoutException ignored) {
        } finally {
            future.cancel(true);
        }
    }

}