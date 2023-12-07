package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This Model-Class is an Entry for room-database. It represents the table `accomplishments`<br>
 * Its primary key is `uid, a_id`, and it has two foreign keys. One to the table `cycle` one to the table `todos`
 */
@Entity(
        tableName = "accomplishments",
        indices = {
                @Index(value = {"uid", "a_id"}, unique = true),
                @Index(value = {"uid", "cycle"}),
                @Index(value = {"uid", "todo"}),
        },
        primaryKeys = {"uid", "a_id"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "uid",
                        childColumns = "uid",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = Cycle.class,
                        parentColumns = {"uid", "cycle_name"},
                        childColumns = {"uid", "cycle"},
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Todo.class,
                        parentColumns = {"uid", "todo_name"},
                        childColumns = {"uid", "todo"},
                        onUpdate = ForeignKey.CASCADE
                )
        }
)
public class Accomplishment {
    @ColumnInfo(name = "a_id")
    private int id;

    @ColumnInfo(name = "uid")
    private int uid;

    @Nullable
    @ColumnInfo(name = "cycle")
    private String cycle;

    @Nullable
    @ColumnInfo(name = "todo")
    private String todo;

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

    public Accomplishment(int id, int uid, @Nullable String cycle, @Nullable String todo, @Nullable String name, @Nullable String description, int productiveness, @NonNull LocalDate date, @Nullable LocalTime timestamp, long durationMillis) {
        this.id = id;
        this.uid = uid;
        this.cycle = cycle;
        this.todo = todo;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Nullable
    public String getCycle() {
        return cycle;
    }

    public void setCycle(@Nullable String cycle) {
        this.cycle = cycle;
    }

    @Nullable
    public String getTodo() {
        return todo;
    }

    public void setTodo(@Nullable String todo) {
        this.todo = todo;
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
}
