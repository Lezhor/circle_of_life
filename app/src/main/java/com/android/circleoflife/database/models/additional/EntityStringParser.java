package com.android.circleoflife.database.models.additional;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.circleoflife.database.models.*;
import com.android.circleoflife.database.models.type_converters.CycleFrequencyConverter;
import com.android.circleoflife.database.models.type_converters.LocalDateTimeConverter;
import com.android.circleoflife.database.models.type_converters.UUIDConverter;

import java.util.Arrays;

public final class EntityStringParser {

    public static final String SEPARATOR = "dkbKBj9dkjkKJd";

    /**
     * serializes category to string
     * @param category category
     * @return serialized string
     */
    public static String categoryToString(Category category) {
        return "category" + SEPARATOR
                + UUIDConverter.uuidToString(category.getId()) + SEPARATOR
                + category.getName() + SEPARATOR
                + UUIDConverter.uuidToString(category.getUserID()) + SEPARATOR
                + UUIDConverter.uuidToString(category.getParentID());
    }

    /**
     * deserializes category from string
     * @param str string
     * @return deserialized category
     */
    public static Category categoryFromString(String str) {
        String[] split = replaceNullStringWithNull(str.split(SEPARATOR));
        return new Category(
                UUIDConverter.uuidFromString(split[1]),
                split[2],
                UUIDConverter.uuidFromString(split[3]),
                UUIDConverter.uuidFromString(split[4])
        );
    }

    /**
     * serializes cycle to string
     * @param cycle cycle
     * @return serialized string
     */
    public static String cycleToString(Cycle cycle) {
        return "cycle" + SEPARATOR
                + UUIDConverter.uuidToString(cycle.getId()) + SEPARATOR
                + cycle.getName() + SEPARATOR
                + UUIDConverter.uuidToString(cycle.getUserID()) + SEPARATOR
                + UUIDConverter.uuidToString(cycle.getCategoryID()) + SEPARATOR
                + cycle.getProductiveness() + SEPARATOR
                + CycleFrequencyConverter.frequecyToString(cycle.getFrequency());
    }

    /**
     * deserializes cycle from string
     * @param str string
     * @return deserialized cycle
     */
    public static Cycle cycleFromString(String str) {
        String[] split = replaceNullStringWithNull(str.split(SEPARATOR));
        return new Cycle(
                UUIDConverter.uuidFromString(split[1]),
                split[2],
                UUIDConverter.uuidFromString(split[3]),
                UUIDConverter.uuidFromString(split[4]),
                Integer.parseInt(split[5]),
                CycleFrequencyConverter.stringToFrequency(split[6])
        );
    }

    /**
     * serializes cycle to string
     * @return serialized string
     */
    public static String todoToString(Todo todo) {
        return "todo" + SEPARATOR
                + UUIDConverter.uuidToString(todo.getId()) + SEPARATOR
                + todo.getName() + SEPARATOR
                + UUIDConverter.uuidToString(todo.getUserID()) + SEPARATOR
                + UUIDConverter.uuidToString(todo.getCategoryID()) + SEPARATOR
                + todo.getProductive() + SEPARATOR
                + todo.isDone() + SEPARATOR
                + LocalDateTimeConverter.localDateTimeToString(todo.getDueDate());
    }

    /**
     * deserializes todó from string
     * @param str string
     * @return deserialized todó
     */
    public static Todo todoFromString(String str) {
        String[] split = replaceNullStringWithNull(str.split(SEPARATOR));
        return new Todo(
                UUIDConverter.uuidFromString(split[1]),
                split[2],
                UUIDConverter.uuidFromString(split[3]),
                UUIDConverter.uuidFromString(split[4]),
                Integer.parseInt(split[5]),
                split[6].equals("true"),
                LocalDateTimeConverter.localDateTimeFromString(split[7])
        );
    }

    /**
     * serializes cycle to string
     * @return serialized string
     */
    public static String accomplishmentToString(Accomplishment accomplishment) {
        return "accomplishment" + SEPARATOR
                + UUIDConverter.uuidToString(accomplishment.getId()) + SEPARATOR
                + UUIDConverter.uuidToString(accomplishment.getUserID()) + SEPARATOR
                + UUIDConverter.uuidToString(accomplishment.getCycleID()) + SEPARATOR
                + UUIDConverter.uuidToString(accomplishment.getTodoID()) + SEPARATOR
                + replaceNullWithNullString(accomplishment.getName()) + SEPARATOR
                + replaceNullWithNullString(accomplishment.getDescription()) + SEPARATOR
                + accomplishment.getProductiveness() + SEPARATOR
                + LocalDateTimeConverter.localDateToString(accomplishment.getDate()) + SEPARATOR
                + LocalDateTimeConverter.localTimeToString(accomplishment.getTimestamp()) + SEPARATOR
                + accomplishment.getDurationMillis();
    }

    /**
     * deserializes todó from string
     * @param str string
     * @return deserialized todó
     */
    public static Accomplishment accomplishmentFromString(String str) {
        String[] split = replaceNullStringWithNull(str.split(SEPARATOR));
        return new Accomplishment(
                UUIDConverter.uuidFromString(split[1]),
                UUIDConverter.uuidFromString(split[2]),
                UUIDConverter.uuidFromString(split[3]),
                UUIDConverter.uuidFromString(split[4]),
                split[5],
                split[6],
                Integer.parseInt(split[7]),
                LocalDateTimeConverter.localDateFromString(split[8]),
                LocalDateTimeConverter.localTimeFromString(split[9]),
                Long.parseLong(split[10])
        );
    }

    /**
     * If str equals 'null' returns <code>null</code>
     * @param str string
     * @return string or null
     */
    @Nullable
    private static String replaceNullStringWithNull(String str) {
        return str == null || str.equals("null") ? null : str;
    }

    /**
     * Maps whole array to null if needed
     * @param strings string array
     * @return new string array with method {@link #replaceNullStringWithNull(String)} called on each entry
     */
    private static String[] replaceNullStringWithNull(String[] strings) {
        return Arrays.stream(strings)
                .map(EntityStringParser::replaceNullStringWithNull)
                .toArray(String[]::new);
    }

    /**
     * If str is null, returns String 'null' else returns str
     * @param str passed string
     * @return string or 'null'-string
     */
    @NonNull
    private static String replaceNullWithNullString(@Nullable String str) {
        return str == null ? "null" : str;
    }

}
