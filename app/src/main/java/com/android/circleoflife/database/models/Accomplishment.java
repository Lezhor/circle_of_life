package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * This Model-Class is an Entry for room-database. It represents the table `accomplishments`<br>
 * Its primary key is `userID, a_id`, and it has two foreign keys. One to the table `cycleID` one to the table `todos`
 */
@Entity(
        tableName = "accomplishments",
        indices = {
                @Index(value = {"userID", "cycleID"}),
                @Index(value = {"userID", "todoID"}),
        },
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userID",
                        childColumns = "userID",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = Cycle.class,
                        parentColumns = {"userID", "ID"},
                        childColumns = {"userID", "cycleID"},
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Todo.class,
                        parentColumns = {"userID", "ID"},
                        childColumns = {"userID", "todoID"},
                        onUpdate = ForeignKey.CASCADE
                )
        }
)
public class Accomplishment {
    
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ID")
    private UUID id;

    @NonNull
    @ColumnInfo(name = "userID")
    private UUID userID;

    @Nullable
    @ColumnInfo(name = "cycleID")
    private UUID cycleID;

    @Nullable
    @ColumnInfo(name = "todoID")
    private UUID todoID;

    @Nullable
    @ColumnInfo(name = "name")
    private String name;

    @Nullable
    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "productiveness")
    private int productiveness;

    @NonNull
    @ColumnInfo(name = "date")
    private LocalDate date;

    @Nullable
    @ColumnInfo(name = "timestamp")
    private LocalTime timestamp;

    @ColumnInfo(name = "duration")
    private long durationMillis;

    public Accomplishment(@NonNull UUID id, @NonNull UUID userID, @Nullable UUID cycleID, @Nullable UUID todoID, @Nullable String name, @Nullable String description, int productiveness, @NonNull LocalDate date, @Nullable LocalTime timestamp, long durationMillis) {
        this.id = id;
        this.userID = userID;
        this.cycleID = cycleID;
        this.todoID = todoID;
        this.name = name;
        this.description = description;
        this.productiveness = productiveness;
        this.date = date;
        this.timestamp = timestamp;
        this.durationMillis = durationMillis;
    }

    /**
     * Returns localdatetime. If time is not set, the time will be 00:00:00
     * @return dateTime
     */
    @Ignore
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.of(date, timestamp != null ? timestamp : LocalTime.of(0, 0, 0));
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    @NonNull
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(@NonNull UUID userID) {
        this.userID = userID;
    }

    @Nullable
    public UUID getCycleID() {
        return cycleID;
    }

    public void setCycleID(@Nullable UUID cycleID) {
        this.cycleID = cycleID;
    }

    @Nullable
    public UUID getTodoID() {
        return todoID;
    }

    public void setTodoID(@Nullable UUID todoID) {
        this.todoID = todoID;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public int getProductiveness() {
        return productiveness;
    }

    public void setProductiveness(int productiveness) {
        this.productiveness = productiveness;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@NonNull LocalDate date) {
        this.date = date;
    }

    @Nullable
    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@Nullable LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Accomplishment that) {
            return this.userID.equals(that.userID) && this.id.equals(that.id);
        }
        return false;
    }
}
