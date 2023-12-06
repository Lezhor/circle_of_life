package com.android.circleoflife.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class Todo {
    // TODO: 06.12.2023 Create attributes for Todo model

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
