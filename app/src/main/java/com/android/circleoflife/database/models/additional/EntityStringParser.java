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
    public static final String USER = "user";
    public static final String CATEGORY = "category";
    public static final String CYCLE = "cycle";
    public static final String TODO = "todo";
    public static final String ACCOMPLISHMENT = "accomplishment";

    /**
     * Checks which class the passed object is and calls the corresponding method
     * @param object object
     * @return string representing object
     */
    public static String objectToString(Object object) {
        if (object instanceof User user) {
            return userToString(user);
        } else if (object instanceof Category category) {
            return categoryToString(category);
        } else if (object instanceof Cycle cycle) {
            return cycleToString(cycle);
        } else if (object instanceof Todo todo) {
            return todoToString(todo);
        } else if (object instanceof Accomplishment accomplishment) {
            return accomplishmentToString(accomplishment);
        } else {
            return null;
        }
    }

    /**
     * Checks which class the passed object is and calls the corresponding method
     * @param str string
     * @return string representing object
     */
    public static Object objectFromString(String str) {
        return switch (str.split(SEPARATOR)[0]) {
            case USER -> userFromString(str);
            case CATEGORY -> categoryFromString(str);
            case CYCLE -> cycleFromString(str);
            case TODO -> todoFromString(str);
            case ACCOMPLISHMENT -> accomplishmentFromString(str);
            default -> null;
        };
    }

    /**
     * serializes category to string
     * @param user category
     * @return serialized string
     */
    public static String userToString(User user) {
        return USER + SEPARATOR
                + UUIDConverter.uuidToString(user.getId()) + SEPARATOR
                + user.getUsername() + SEPARATOR
                + user.getPassword() + SEPARATOR
                + LocalDateTimeConverter.localDateTimeToString(user.getTimeOfCreation());
    }

    /**
     * deserializes category from string
     * @param str string
     * @return deserialized category
     */
    public static User userFromString(String str) {
        String[] split = replaceNullStringWithNull(str.split(SEPARATOR));
        return new User(
                UUIDConverter.uuidFromString(split[1]),
                split[2],
                split[3],
                LocalDateTimeConverter.localDateTimeFromString(split[4])
        );
    }

    /**
     * serializes category to string
     * @param category category
     * @return serialized string
     */
    public static String categoryToString(Category category) {
        return CATEGORY + SEPARATOR
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
        return CYCLE + SEPARATOR
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
     * serializes todó to string
     * @return serialized string
     */
    public static String todoToString(Todo todo) {
        return TODO + SEPARATOR
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
        return ACCOMPLISHMENT + SEPARATOR
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
