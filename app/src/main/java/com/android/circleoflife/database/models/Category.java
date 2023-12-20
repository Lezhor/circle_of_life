package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.android.circleoflife.database.validators.StringValidator;

import java.util.UUID;

/**
 * This Model-Class is an Entry for room-database. It represents the table `categories`
 */
@Entity(
        tableName = "categories",
        indices = {
                @Index(value = {"userID", "ID"}, unique = true),
                @Index(value = {"userID", "parentID"})
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
                        childColumns = {"userID", "parentID"},
                        onUpdate = ForeignKey.CASCADE
                )
        },
        inheritSuperIndices = true
)
public class Category {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ID")
    private UUID id;

    @NonNull
    @ColumnInfo(name = "userID")
    private UUID userID;

    @NonNull
    @ColumnInfo(name = "category_name")
    private String name;

    @Nullable
    @ColumnInfo(name = "parentID", defaultValue = "NULL")
    private UUID parentID;

    public Category(@NonNull UUID id, String name, @NonNull UUID userID, @Nullable UUID parentID) {
        this.id = id;
        this.name = StringValidator.validateStringMinLength(name, 1);
        this.userID = userID;
        this.parentID = parentID;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
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
    public UUID getParentID() {
        return parentID;
    }

    public void setParentID(@Nullable UUID parent) {
        if (!this.id.equals(parent))
            this.parentID = parent;
    }

    @Ignore
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Category that) {
            return this.userID.equals(that.userID) && this.id.equals(that.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return "Category[" + getName() + "]";
    }
}
