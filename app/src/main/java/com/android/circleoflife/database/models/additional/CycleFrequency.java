package com.android.circleoflife.database.models.additional;

import androidx.annotation.NonNull;

import com.android.circleoflife.Application;
import com.android.circleoflife.R;

import java.util.Arrays;

public class CycleFrequency {

    public final static int MASK_MONDAY     = 0b00000001;
    public final static int MASK_TUESDAY    = 0b00000010;
    public final static int MASK_WEDNESDAY  = 0b00000100;
    public final static int MASK_THURSDAY   = 0b00001000;
    public final static int MASK_FRIDAY     = 0b00010000;
    public final static int MASK_SATURDAY   = 0b00100000;
    public final static int MASK_SUNDAY     = 0b01000000;

    private static final int[] MASKS_DAYS_WEEK = { MASK_MONDAY, MASK_TUESDAY, MASK_WEDNESDAY, MASK_THURSDAY, MASK_FRIDAY };
    private static final int[] MASKS_DAYS_WEEKEND = { MASK_SATURDAY, MASK_SUNDAY };
    private static final int[] MASKS_DAYS_ALL = { MASK_MONDAY, MASK_TUESDAY, MASK_WEDNESDAY, MASK_THURSDAY, MASK_FRIDAY, MASK_SATURDAY, MASK_SUNDAY };

    public final static int MASK_END_CYCLE  = 0b10000000;



    private int value;

    public CycleFrequency(int... days) {
        value = Arrays.stream(days).reduce(MASK_END_CYCLE, (a, b) -> a | b);
    }

    @NonNull
    @Override
    public String toString() {
        // TODO: 06.12.2023 String Representation of Frequency - e.g. "every day", "every monday" etc.
        return CycleFrequency.countDaysAWeek(this) + " days a week";
    }

    public static int countDaysAWeek(CycleFrequency frequency) {
        return countDaysAWeek(frequency, 0);
    }

    public static int countDaysAWeek(CycleFrequency frequency, int week) {
        int f = frequency.value >> week * 8;
        int counter = 0;
        for (int maskDay : MASKS_DAYS_ALL) {
            if ((f & maskDay) > 0)
                counter++;
        }
        return counter;
    }

    /**
     * Returns string representation of dayMask
     * @param mask daymask. e.gl {@link CycleFrequency#MASK_THURSDAY}
     * @return String from {@link Application#getResources() SystemResources}.getString()
     */
    public static String getString(int mask) {
        if (Arrays.stream(MASKS_DAYS_ALL).noneMatch(day -> mask == day)) {
            return "";
        }
        String[] days_of_week = Application.getResources().getStringArray(R.array.days_of_week);
        return switch (mask) {
            case MASK_MONDAY -> days_of_week[0];
            case MASK_TUESDAY -> days_of_week[1];
            case MASK_WEDNESDAY -> days_of_week[2];
            case MASK_THURSDAY -> days_of_week[3];
            case MASK_FRIDAY -> days_of_week[4];
            case MASK_SATURDAY -> days_of_week[5];
            case MASK_SUNDAY -> days_of_week[6];
            default -> Application.getResources().getString(R.string.no_string);
        };
    }

    // TODO: 06.12.2023 Simplifly methods: e.g. if all bits set - shorten to one week. (Remove redundancy)

}
