package com.android.circleoflife.logging.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.Copyable;
import com.android.circleoflife.database.models.additional.EntityStringParser;
import com.android.circleoflife.database.models.additional.HasUserId;
import com.android.circleoflife.database.models.type_converters.LocalDateTimeConverter;
import com.android.circleoflife.database.models.type_converters.UUIDConverter;

import java.time.LocalDateTime;
import java.util.UUID;

public class DBLog<E extends HasUserId> {
    final static String SEPARATOR = "dkjLdj4wlkK6lSD3OP";

    private final UUID id;
    private final UUID userID;
    private final E changedObject;
    private final ChangeMode changeMode;
    private final LocalDateTime timestamp;

    /**
     * Converts string to a log. the opposite operation ist {@link #toString(DBLog)}
     *
     * @param str string to be converted
     * @return converted log
     */
    public static DBLog<?> fromString(String str) {
        if (str == null) {
            return null;
        } else {
            String[] split = str.split(SEPARATOR);
            UUID id = UUIDConverter.uuidFromString(split[2]);
            UUID userId = UUIDConverter.uuidFromString(split[3]);
            Object changedObject = EntityStringParser.objectFromString(split[4]);
            ChangeMode mode = ChangeMode.parseFromString(split[5]);
            LocalDateTime timestamp = LocalDateTimeConverter.localDateTimeFromString(split[6]);
            return switch (split[1]) {
                case EntityStringParser.USER -> new DBLog<>(id, userId, (User) changedObject, mode, timestamp);
                case EntityStringParser.CATEGORY -> new DBLog<>(id, userId, (Category) changedObject, mode, timestamp);
                case EntityStringParser.CYCLE -> new DBLog<>(id, userId, (Cycle) changedObject, mode, timestamp);
                case EntityStringParser.TODO -> new DBLog<>(id, userId, (Todo) changedObject, mode, timestamp);
                case EntityStringParser.ACCOMPLISHMENT -> new DBLog<>(id, userId, (Accomplishment) changedObject, mode, timestamp);
                default -> null;
            };
        }
    }

    /**
     * Converts given log to string. the opposite operation is {@link #fromString(String)}
     *
     * @param log log to be converted
     * @return converted string
     */
    public static String toString(DBLog<?> log) {
        if (log == null) {
            return null;
        } else {
            return "log" + SEPARATOR +
                    switch (log.getObjectType()) {
                        case 0 -> EntityStringParser.USER;
                        case 1 -> EntityStringParser.CATEGORY;
                        case 2 -> EntityStringParser.CYCLE;
                        case 3 -> EntityStringParser.TODO;
                        case 4 -> EntityStringParser.ACCOMPLISHMENT;
                        default -> "null";
                    } + SEPARATOR
                    + UUIDConverter.uuidToString(log.id) + SEPARATOR
                    + UUIDConverter.uuidToString(log.userID) + SEPARATOR
                    + EntityStringParser.objectToString(log.changedObject) + SEPARATOR
                    + ChangeMode.parseToString(log.changeMode) + SEPARATOR
                    + LocalDateTimeConverter.localDateTimeToString(log.timestamp);
        }
    }

    /**
     * Private constructor for initializing every attribute directly
     *
     * @param id            id of the log
     * @param userID        userId
     * @param changedObject changedObject
     * @param changeMode    changeMode
     * @param timestamp     timestamp
     */
    public DBLog(UUID id, UUID userID, E changedObject, ChangeMode changeMode, LocalDateTime timestamp) {
        this.id = id;
        this.userID = userID;
        this.changedObject = changedObject;
        this.changeMode = changeMode;
        this.timestamp = timestamp;
    }

    /**
     * Constructor for DBLog. Initializes DBLog
     *
     * @param changedObject object which got changed during the transaction
     * @param changeMode    transaction mode (Insert, Update, Delete)
     */

    public DBLog(@NonNull Copyable<E> changedObject, ChangeMode changeMode) {
        this(UUID.randomUUID(), changedObject, changeMode);
    }

    /**
     * Constructor for DBLog
     *
     * @param id            id
     * @param changedObject changedObject
     * @param changeMode    changeMode
     */
    public DBLog(@NonNull UUID id, @NonNull Copyable<E> changedObject, ChangeMode changeMode) {
        this.id = id;
        this.changedObject = changedObject.copy();
        this.userID = this.changedObject.getUserID();
        this.changeMode = changeMode;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * returns number of type:
     * <pre>
     *     0 - User
     *     1 - Category
     *     2 - Cycle
     *     3 - To do
     *     4 - Accomplishment
     * </pre>
     *
     * @return integer based on object type
     */
    private int getObjectType() {
        if (changedObject instanceof User) {
            return 0;
        } else if (changedObject instanceof Category) {
            return 1;
        } else if (changedObject instanceof Cycle) {
            return 2;
        } else if (changedObject instanceof Todo) {
            return 3;
        } else if (changedObject instanceof Accomplishment) {
            return 4;
        } else {
            return -1;
        }
    }

    public E getChangedObject() {
        return changedObject;
    }

    public ChangeMode getChangeMode() {
        return changeMode;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof DBLog<?> that) {
            return this.id.equals(that.id)
                    && this.userID.equals(that.userID)
                    && this.changedObject.equals(that.changedObject)
                    && this.changeMode == that.changeMode
                    && this.timestamp.equals(that.timestamp);
        }
        return false;
    }

    public enum ChangeMode {
        INSERT, UPDATE, DELETE;

        static String parseToString(ChangeMode mode) {
            return mode.name();
        }

        static ChangeMode parseFromString(String name) {
            return valueOf(name);
        }

    }

    @NonNull
    @Override
    public String toString() {
        return "DBLog { " + changedObject.toString() + ", " + changeMode.name() + ", " + LocalDateTimeConverter.localDateTimeToString(timestamp) + " }";
    }
}
