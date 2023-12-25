package com.android.circleoflife.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.android.circleoflife.database.models.additional.Copyable;
import com.android.circleoflife.database.models.additional.Nameable;
import com.android.circleoflife.database.models.type_converters.UUIDConverter;
import com.android.circleoflife.database.validators.StringValidator;

import java.util.Objects;
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
public class Category implements Nameable, Copyable<Category>, Parcelable {

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

    /**
     * Constructor for cloning
     *
     * @param that other category
     */
    @Ignore
    public Category(@NonNull Category that) {
        this(that.id, that.name, that.userID, that.parentID);
    }

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
    @Override
    public String getName() {
        return name;
    }

    @Override
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

    /**
     * Compares every attribute and returns true if all matching
     *
     * @param that category
     * @return true if all attributes matching
     */
    public boolean equalsAllParams(Category that) {
        return this.userID.equals(that.userID)
                && this.id.equals(that.id)
                && this.name.equals(that.name)
                && Objects.equals(this.parentID, that.parentID);
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


    // Parcelable:


    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel out, int flags) {
        out.writeString(UUIDConverter.uuidToString(getId()));
        out.writeString(getName());
        out.writeString(UUIDConverter.uuidToString(getUserID()));
        out.writeString(UUIDConverter.uuidToString(getParentID()));
    }

    @Ignore
    public static final Parcelable.Creator<Category> CREATOR = new Creator<>() {
        @Override
        public Category createFromParcel(Parcel in) {
            UUID id = UUIDConverter.uuidFromString(in.readString());
            String name = in.readString();
            UUID userID = UUIDConverter.uuidFromString(in.readString());
            UUID parentID = UUIDConverter.uuidFromString(in.readString());
            if (id != null && name != null && userID != null) {
                return new Category(id, name, userID, parentID);
            }
            return null;
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @NonNull
    @Override
    public Category copy() {
        return new Category(this);
    }
}
