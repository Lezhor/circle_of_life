package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import java.time.LocalDateTime;


/**
 * This Model-Class is an Entry for room-database. It represents the table `todos`<br>
 * Its primary key is `uid, todo_name`, and it has a foreign key `uid, category`
 * pointing to table `categories`
 */
@Entity(
        tableName = "todos",
        indices = {
                @Index(value = {"uid", "todo_name"}, unique = true),
                @Index(value = {"uid", "category"}),
        },
        primaryKeys = {"uid", "todo_name"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "uid",
                        childColumns = "uid",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = {"uid", "category_name"},
                        childColumns = {"uid", "category"},
                        onUpdate = ForeignKey.CASCADE
                )
        }
)
public class Todo {

    @NonNull
    @ColumnInfo(name = "todo_name")
    private String name;

    @ColumnInfo(name = "uid")
    private int userID;

    @Nullable
    @ColumnInfo(name = "category", defaultValue = "NULL")
    private String category;

    @ColumnInfo(name = "productiveness")
    private int productive;

    @ColumnInfo(name = "done")
    private boolean done;

    @Nullable
    @ColumnInfo(name = "due_date", defaultValue = "NULL")
    private LocalDateTime dueDate;

    @Ignore
    public Todo(@NonNull String name, int userID, @Nullable String category, int productive) {
        this(name, userID, category, productive, false, null);
    }

    @Ignore
    public Todo(@NonNull String name, int userID, @Nullable String category, int productive, @Nullable LocalDateTime dueDate) {
        this(name, userID, category, productive, false, dueDate);
    }

    public Todo(@NonNull String name, int userID, @Nullable String category, int productive, boolean done, @Nullable LocalDateTime dueDate) {
        this.name = name;
        this.userID = userID;
        this.category = category;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Nullable
    public String getCategory() {
        return category;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
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
}
