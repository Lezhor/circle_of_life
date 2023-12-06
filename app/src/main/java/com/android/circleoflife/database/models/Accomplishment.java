package com.android.circleoflife.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accomplishments")
public class Accomplishment {
    // TODO: 06.12.2023 Create attributes for Accomplishment model

    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
