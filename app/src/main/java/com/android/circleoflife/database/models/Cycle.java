package com.android.circleoflife.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cycles")
public class Cycle {
    // TODO: 06.12.2023 Cycle TODO

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
