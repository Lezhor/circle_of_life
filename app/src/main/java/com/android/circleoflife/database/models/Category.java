package com.android.circleoflife.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    // TODO: 06.12.2023 Categories TODO

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
