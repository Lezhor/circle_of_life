package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.android.circleoflife.database.models.additional.Copyable;
import com.android.circleoflife.database.models.additional.CycleFrequency;
import com.android.circleoflife.database.models.additional.HasUserId;
import com.android.circleoflife.database.models.additional.Nameable;
import com.android.circleoflife.database.validators.IntegerValidator;

import java.util.Objects;
import java.util.UUID;

/**
 * This Model-Class is an Entry for room-database. It represents the table `cycles`
 */
@Entity(
        tableName = "cycles",
        indices = {
                @Index(value = {"userID", "ID"}, unique = true),
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
public class Cycle implements Nameable, HasUserId, Copyable<Cycle> {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ID")
    private UUID id;

    @NonNull
    @ColumnInfo(name = "userID")
    private UUID userID;

    @NonNull
    @ColumnInfo(name = "cycle_name")
    private String name;

    @Nullable
    @ColumnInfo(name = "categoryID", defaultValue = "NULL")
    private UUID categoryID;

    /**
     * Value ranging from -1 to 1
     */
    @ColumnInfo(name = "productiveness")
    private int productiveness;

    @ColumnInfo(name = "frequency")
    private CycleFrequency frequency;

    @ColumnInfo(name = "archived")
    private boolean archived;

    /**
     * Constructor for cloning
     * @param that to be cloned
     */
    @Ignore
    public Cycle(Cycle that) {
        this(that.id, that.name, that.userID, that.categoryID, that.productiveness, that.frequency, that.archived);
    }

    /**
     * Constructor for Cycle. Sets value archived to false.
     * @param name name
     * @param userID userID
     * @param categoryID category
     * @param productiveness productiveness
     * @param frequency frequency
     */
    @Ignore
    public Cycle(@NonNull UUID id, @NonNull String name, @NonNull UUID userID, @Nullable UUID categoryID, int productiveness, CycleFrequency frequency) {
        this(id, name, userID, categoryID, productiveness, frequency, false);
    }

    public Cycle(@NonNull UUID id, @NonNull String name, @NonNull UUID userID, @Nullable UUID categoryID, int productiveness, CycleFrequency frequency, boolean archived) {
        this.id = id;
        this.name = name;
        this.userID = userID;
        this.categoryID = categoryID;
        setProductiveness(productiveness);
        this.frequency = frequency;
        this.archived = archived;
    }

    // GETTER AND SETTER

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
    @Override
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(@NonNull UUID userID) {
        this.userID = userID;
    }

    @Nullable
    public UUID getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(@Nullable UUID categoryID) {
        this.categoryID = categoryID;
    }

    public int getProductiveness() {
        return productiveness;
    }

    public void setProductiveness(int productiveness) {
        this.productiveness = IntegerValidator.validateIntBetweenBounds(productiveness, -1, 1);
    }

    public CycleFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(CycleFrequency frequency) {
        this.frequency = frequency;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Cycle that) {
            return this.userID.equals(that.userID) && this.id.equals(that.id);
        }
        return false;
    }


    /**
     * Compares every attribute and returns true if all matching
     * @param that cycle
     * @return true if all attributes matching
     */
    public boolean equalsAllParams(Cycle that) {
        return this.userID.equals(that.userID)
                && this.id.equals(that.id)
                && this.name.equals(that.name)
                && Objects.equals(this.categoryID, that.categoryID)
                && this.productiveness == that.productiveness
                && this.frequency.equals(that.frequency)
                && this.archived == that.archived;
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
    public Cycle copy() {
        return new Cycle(this);
    }
}
