package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.android.circleoflife.database.validators.StringValidator;

/**
 * This Model-Class is an Entry for room-database. It represents the table `categories`<br>
 * Its primary key is `uid, category_name`, and it has a foreign key `uid, parent_category`
 * pointing to the same table `categories`
 */
@Entity(
        tableName = "categories",
        indices = {
                @Index(value = {"uid", "category_name"}, unique = true),
                @Index(value = {"uid", "parent_category"})
        },
        primaryKeys = {"uid", "category_name"},
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
                        childColumns = {"uid", "parent_category"},
                        onUpdate = ForeignKey.CASCADE
                )
        },
        inheritSuperIndices = true
)
public class Category {

    @NonNull
    @ColumnInfo(name = "category_name")
    private String name;

    @ColumnInfo(name = "uid")
    private int userID;

    @Nullable
    @ColumnInfo(name = "parent_category", defaultValue = "NULL")
    private String parent;

    public Category(String name, int userID, @Nullable String parent) {
        this.name = StringValidator.validateStringMinLength(name, 1);
        setUserID(userID);
        setParent(parent);
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
    public String getParent() {
        return parent;
    }

    public void setParent(@Nullable String parent) {
        if (!this.name.equals(parent))
            this.parent = parent;
    }

    @Ignore
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Category that) {
            return this.name.equals(that.name) && this.userID == that.userID;
        }
        return false;
    }
}
