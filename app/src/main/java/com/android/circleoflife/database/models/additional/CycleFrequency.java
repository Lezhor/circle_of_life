package com.android.circleoflife.database.models.additional;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.circleoflife.application.App;
import com.android.circleoflife.R;

import java.time.LocalDateTime;
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

    /**
     * Gets the mask wich corresponds to todays weekday
     * @return mask of todays weekday
     */
    public static int getTodayMask() {
        return switch (LocalDateTime.now().getDayOfWeek()) {
            case MONDAY -> MASK_MONDAY;
            case TUESDAY -> MASK_TUESDAY;
            case WEDNESDAY -> MASK_WEDNESDAY;
            case THURSDAY -> MASK_THURSDAY;
            case FRIDAY -> MASK_FRIDAY;
            case SATURDAY -> MASK_SATURDAY;
            case SUNDAY -> MASK_SUNDAY;
        };
    }

    /**
     * Constructor for cloning
     * @param that other frequency
     */
    public CycleFrequency(CycleFrequency that) {
        this.value = that.value;
    }

    /**
     * Sets up CycleFrequency.<br>
     * Example use:
     * <pre>
     *     {@code new CycleFrequency(CycleFrequency.MASK_MONDAY, CycleFrequency.MASK_WEDNESDAY);}
     * </pre>
     * @param days varargs masks
     */
    public CycleFrequency(int... days) {
        value = Arrays.stream(days).reduce(MASK_END_CYCLE, (a, b) -> a | b);
    }

    @NonNull
    @Override
    public String toString() {
        // TODO: 06.12.2023 String Representation of Frequency - e.g. "every day", "every monday" etc.
        switch (countDaysAWeek(this)) {
            case 0 -> {
                return "";
            }
            case 2 -> {
                if (maskIsSet(MASK_SATURDAY) && maskIsSet(MASK_SUNDAY)) {
                    return App.getResources().getString(R.string.frequency_on_weekends);
                }
            }
            case 5 -> {
                if (!maskIsSet(MASK_SATURDAY) && !maskIsSet(MASK_SUNDAY)) {
                    return App.getResources().getString(R.string.frequency_on_weekdays);
                }
            }
            case 7 -> {
                return App.getResources().getString(R.string.frequency_every_day);
            }
            default -> {
            }
        }
        return App.getResources().getString(
                R.string.frequency_on_preposition,
                (Arrays.stream(CycleFrequency.MASKS_DAYS_ALL)
                .boxed()
                .filter(this::maskIsSet)
                .map(CycleFrequency::getDayStringWithOnPreposition)
                .reduce("", (a, b) -> a + b + ", ") + ",").replace(", ,", "")
        );
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof CycleFrequency that) {
            return this.value == that.value;
        } else {
            return false;
        }
    }

    public String toBinaryString() {
        return Integer.toBinaryString(value);
    }

    public static CycleFrequency fromBinaryString(String binary) throws NumberFormatException {
        return new CycleFrequency(Integer.parseInt(binary, 2));
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

    public boolean maskIsSet(int dayMask) {
        return (value & dayMask) > 0;
    }

    public void setMask(int dayMask) {
        value = value | dayMask;
    }

    public void resetMask(int dayMask) {
        value = (value | dayMask) ^ dayMask;
    }

    /**
     * If day with daymask was true, is set to false, and vice versa.
     * @param dayMask dayMask.
     */
    public void toggleMask(int dayMask) {
        value = value ^ dayMask;
    }

    /**
     * Returns string representation of dayMask
     * @param mask daymask. e.gl {@link CycleFrequency#MASK_THURSDAY}
     * @return String from {@link App#getResources() SystemResources}.getString()
     */
    public static String getDayString(int mask) {
        if (Arrays.stream(MASKS_DAYS_ALL).noneMatch(day -> mask == day)) {
            return "";
        }
        String[] days_of_week = App.getResources().getStringArray(R.array.days_of_week);
        return days_of_week[getDayIndex(mask)];
    }

    /**
     * Returns string representation of dayMask from the array R.array.days_of_week_on_preposition
     * @param mask daymask. e.gl {@link CycleFrequency#MASK_THURSDAY}
     * @return String from {@link App#getResources() SystemResources}.getString()
     */
    public static String getDayStringWithOnPreposition(int mask) {
        if (Arrays.stream(MASKS_DAYS_ALL).noneMatch(day -> mask == day)) {
            return "";
        }
        String[] days_of_week = App.getResources().getStringArray(R.array.days_of_week_on_preposition);
        return days_of_week[getDayIndex(mask)];
    }

    /**
     * Returns index of daymask. e.g.
     * <pre>
     *     MASK_MONDAY -> 0
     *     MASK_TUESDAY -> 1
     *     ...
     *     MASK_SUNDAY -> 6
     * </pre>
     * Can be used for string arrays from String-Resource-Files
     * @param mask dayMask
     * @return index
     */
    private static int getDayIndex(int mask) {
        return switch (mask) {
            case MASK_MONDAY -> 0;
            case MASK_TUESDAY -> 1;
            case MASK_WEDNESDAY -> 2;
            case MASK_THURSDAY -> 3;
            case MASK_FRIDAY -> 4;
            case MASK_SATURDAY -> 5;
            case MASK_SUNDAY -> 6;
            default -> throw new IllegalStateException("Unexpected mask: " + mask);
        };
    }

    // TODO: 06.12.2023 Simplifly methods: e.g. if all bits set - shorten to one week. (Remove redundancy)

}
