package com.android.circleoflife.logging.model;
import androidx.annotation.NonNull;

import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.Copyable;
import com.android.circleoflife.database.models.additional.HasUserId;

import java.util.UUID;

public class DBLog<E extends HasUserId> {

    private final UUID id;
    private final UUID userID;
    private final E changedObject;
    private final ChangeMode changeMode;

    /**
     * Converts string to a log. the opposite operation ist {@link #toString(DBLog)}
     * @param str string to be converted
     * @return converted log
     */
    public static DBLog<?> fromString(String str) {
        if (str == null) {
            return null;
        } else {
            // TODO: 29.12.2023 Parse Log from string
            return null;
        }
    }

    /**
     * Converts given log to string. the opposite operation is {@link #fromString(String)}
     * @param log log to be converted
     * @return converted string
     */
    public static String toString(DBLog<?> log) {
        if (log == null) {
            return null;
        } else {
            // TODO: 29.12.2023 Parse log to string
            return "";
        }
    }

    public DBLog(@NonNull Copyable<E> changedObject, ChangeMode changeMode) {
        this(UUID.randomUUID(), changedObject, changeMode);
    }

    /**
     * Constructor for DBLog
     * @param id id
     * @param changedObject changedObject
     * @param changeMode changeMode
     */
    public DBLog(@NonNull UUID id, @NonNull Copyable<E> changedObject, ChangeMode changeMode) {
        this.id = id;
        this.changedObject = changedObject.copy();
        this.userID = this.changedObject.getUserID();
        this.changeMode = changeMode;
    }

    /**
     * True if objects are of same instance
     * @param that other DBLog
     * @return true if same instance type
     */
    public boolean sameObjectType(DBLog<?> that) {
        return this.getObjectType() == that.getObjectType();
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

    public enum ChangeMode {
        INSERT, UPDATE, DELETE
    }


}
