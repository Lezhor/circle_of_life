package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * This Model-Class is an Entry for room-database. It represents the table `todos`
 */
@Entity(
        tableName = "todos",
        indices = {
                @Index(value = {"userID", "categoryID"}),
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
                        entity = Category.class,
                        parentColumns = {"userID", "ID"},
                        childColumns = {"userID", "categoryID"},
                        onUpdate = ForeignKey.CASCADE
                )
        }
)
public class Todo {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ID")
    private UUID id;

    @NonNull
    @ColumnInfo(name = "todo_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "userID")
    private UUID userID;

    @Nullable
    @ColumnInfo(name = "categoryID", defaultValue = "NULL")
    private UUID categoryID;

    @ColumnInfo(name = "productiveness")
    private int productive;

    @ColumnInfo(name = "done")
    private boolean done;

    @Nullable
    @ColumnInfo(name = "due_date", defaultValue = "NULL")
    private LocalDateTime dueDate;

    @Ignore
    public Todo(@NonNull UUID id, @NonNull String name, @NonNull UUID userID, @Nullable UUID categoryID, int productive) {
        this(id, name, userID, categoryID, productive, false, null);
    }

    @Ignore
    public Todo(@NonNull UUID id, @NonNull String name, @NonNull UUID userID, @Nullable UUID categoryID, int productive, @Nullable LocalDateTime dueDate) {
        this(id, name, userID, categoryID, productive, false, dueDate);
    }

    public Todo(@NonNull UUID id, @NonNull String name, @NonNull UUID userID, @Nullable UUID categoryID, int productive, boolean done, @Nullable LocalDateTime dueDate) {
        this.id = id;
        this.name = name;
        this.userID = userID;
        this.categoryID = categoryID;
        this.productive = productive;
        this.done = done;
        this.dueDate = dueDate;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(@NonNull UUID userID) {
        this.userID = userID;
    }

    @Nullable
    public UUID getCategory() {
        return categoryID;
    }

    public void setCategory(@Nullable UUID categoryID) {
        this.categoryID = categoryID;
    }

    public int getProductive() {
        return productive;
    }

    public void setProductive(int productive) {
        this.productive = productive;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Nullable
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(@Nullable LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Todo that) {
            return this.userID.equals(that.userID) && this.id.equals(that.id);
        }
        return false;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }
}
