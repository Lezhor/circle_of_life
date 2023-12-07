package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.android.circleoflife.database.models.additional.CycleFrequency;
import com.android.circleoflife.database.validators.IntegerValidator;

/**
 * This Model-Class is an Entry for room-database. It represents the table `cycles`<br>
 * Its primary key is `uid, cycle_name`, and it has a foreign key `uid, category`
 * pointing to table `categories`
 */
@Entity(
        tableName = "cycles",
        indices = {
                @Index(value = {"uid", "cycle_name"}, unique = true),
                @Index(value = {"uid", "category"}),
        },
        primaryKeys = {"uid", "cycle_name"},
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
public class Cycle {

    @NonNull
    @ColumnInfo(name = "cycle_name")
    private String name;

    @ColumnInfo(name = "uid")
    private int userID;

    @ColumnInfo(name = "category", defaultValue = "NULL")
    private String category;

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
     * Constructor for Cycle. Sets value archived to false.
     * @param name name
     * @param userID userID
     * @param category category
     * @param productiveness productiveness
     * @param frequency frequency
     */
    @Ignore
    public Cycle(@NonNull String name, int userID, @Nullable String category, int productiveness, CycleFrequency frequency) {
        this(name, userID, category, productiveness, frequency, false);
    }

    public Cycle(@NonNull String name, int userID, @Nullable String category, int productiveness, CycleFrequency frequency, boolean archived) {
        this.name = name;
        this.userID = userID;
        this.category = category;
        setProductiveness(productiveness);
        this.frequency = frequency;
        this.archived = archived;
    }

    // GETTER AND SETTER

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
