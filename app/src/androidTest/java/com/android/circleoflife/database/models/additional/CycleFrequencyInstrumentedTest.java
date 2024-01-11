package com.android.circleoflife.database.models.additional;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CycleFrequencyInstrumentedTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void testToStringConsoleOutput() {
        for (int i = 0; i < 10; i++) {
            CycleFrequency frequency = new CycleFrequency(i);
            String stringRepresentation = frequency.toString();
            System.out.println("Frequency{" + frequency.toBinaryString() + "}: '" + stringRepresentation + "'");
        }
        for (int i = 20; i < 50; i += 3) {
            CycleFrequency frequency = new CycleFrequency(i);
            String stringRepresentation = frequency.toString();
            System.out.println("Frequency{" + frequency.toBinaryString() + "}: '" + stringRepresentation + "'");
        }
        for (int i = 50; i < 128; i += 7) {
            CycleFrequency frequency = new CycleFrequency(i);
            String stringRepresentation = frequency.toString();
            System.out.println("Frequency{" + frequency.toBinaryString() + "}: '" + stringRepresentation + "'");
        }
    }

}
